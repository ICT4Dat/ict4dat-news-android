package at.ict4d.ict4dnews.server.repositories

import at.ict4d.ict4dnews.extensions.stripHtml
import at.ict4d.ict4dnews.extensions.toLocalDateTimeFromRFCString
import at.ict4d.ict4dnews.models.Author
import at.ict4d.ict4dnews.models.Blog
import at.ict4d.ict4dnews.models.FeedType
import at.ict4d.ict4dnews.models.Media
import at.ict4d.ict4dnews.models.News
import at.ict4d.ict4dnews.models.wordpress.WordpressAuthor
import at.ict4d.ict4dnews.models.wordpress.WordpressMedia
import at.ict4d.ict4dnews.persistence.database.AppDatabase
import at.ict4d.ict4dnews.persistence.database.dao.AuthorDao
import at.ict4d.ict4dnews.persistence.database.dao.MediaDao
import at.ict4d.ict4dnews.persistence.database.dao.NewsDao
import at.ict4d.ict4dnews.server.api.ApiJsonSelfHostedWPService
import at.ict4d.ict4dnews.server.api.ApiRssService
import at.ict4d.ict4dnews.server.utils.NewsUpdateResource
import at.ict4d.ict4dnews.server.utils.Resource
import at.ict4d.ict4dnews.server.utils.Status
import at.ict4d.ict4dnews.utils.recordNetworkBreadcrumb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import timber.log.Timber
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val SELF_HOSTED_URL_ENDING = "wp-json/wp/v2/posts"

class NewsRepository(
    private val newsDao: NewsDao,
    private val apiRssService: ApiRssService,
    private val apiJsonSelfHostedWPService: ApiJsonSelfHostedWPService,
    private val blogsRepository: BlogsRepository,
    private val database: AppDatabase,
    private val authorDao: AuthorDao,
    private val mediaDao: MediaDao
) {

    fun getAllActiveNews(query: String) = newsDao.getAllActiveNews(query).flowOn(Dispatchers.IO)

    fun getCountOfNews() = newsDao.getCountOfNews().flowOn(Dispatchers.IO)

    fun requestLatestNewsByDate(latestNewsDate: LocalDateTime) = newsDao.getLatestNewsByDate(latestNewsDate).flowOn(Dispatchers.IO)

    fun getLatestNewsPublishedDate(blogID: String) = newsDao.getLatestPublishedDateOfBlog(blogID)
        .map { it ?: LocalDateTime.now().minusYears(10) } // if database is empty then today minus 10 years per default
        .flowOn(Dispatchers.IO)

    fun getLatestNewsPublishedDate() = newsDao.getLatestNewsPublishedDate()
        .map { it ?: LocalDateTime.now().minusYears(10) } // if database is empty then today minus 10 years per default
        .flowOn(Dispatchers.IO)

    suspend fun updateAllActiveNewsWithFlow(): Flow<NewsUpdateResource> {

        recordNetworkBreadcrumb("loadAllNewsFromAllActiveBlogs", this)

        return flow {

            val activeBlogs = blogsRepository.getAllActiveBlogs().first()

            val successfulBlogs = mutableListOf<Blog>()
            val failedBlogs = mutableListOf<Blog>()

            activeBlogs.forEach { blog ->

                emit(
                    NewsUpdateResource(
                        Status.LOADING,
                        Resource.loading(blog),
                        successfulBlogs,
                        failedBlogs,
                        activeBlogs.count()
                    )
                )

                recordNetworkBreadcrumb("Update Blog", this, mapOf("Blog ID" to blog.feed_url))
                Timber.d("Updating ${blog.name}")
                val resource = if (blog.feedType == FeedType.SELF_HOSTED_WP_BLOG) {
                    handleSelfHostedWpBlog(blog)
                } else { // Wordpress.com or RSS
                    handleRSSBlog(blog)
                }

                Timber.d("${blog.name} was ${resource.status}")
                val emitResource = if (resource.status == Status.SUCCESS) {
                    successfulBlogs.add(blog)
                    Resource.success(blog, resource.responseCode)
                } else {
                    failedBlogs.add(blog)
                    Resource.error(resource.throwable ?: UnknownError(), blog, resource.responseCode)
                }

                emit(
                    NewsUpdateResource(
                        Status.LOADING,
                        emitResource,
                        successfulBlogs,
                        failedBlogs,
                        activeBlogs.count()
                    )
                )
            }

            emit(
                NewsUpdateResource(
                    Status.SUCCESS,
                    null,
                    successfulBlogs,
                    failedBlogs,
                    activeBlogs.count()
                )
            )
        }.flowOn(Dispatchers.IO)
    }

    suspend fun downloadAllNews(): NewsUpdateResource = withContext(Dispatchers.IO) {

        val activeBlogs = blogsRepository.getAllActiveBlogs().first()

        val successfulBlogs = mutableListOf<Blog>()
        val failedBlogs = mutableListOf<Blog>()

        activeBlogs.forEach { blog ->

            recordNetworkBreadcrumb("Update Blog", this, mapOf("Blog ID" to blog.feed_url))
            Timber.d("Updating ${blog.name}")
            val resource = if (blog.feedType == FeedType.SELF_HOSTED_WP_BLOG) {
                handleSelfHostedWpBlog(blog)
            } else { // Wordpress.com or RSS
                handleRSSBlog(blog)
            }

            Timber.d("${blog.name} was ${resource.status}")
            if (resource.status == Status.SUCCESS) {
                successfulBlogs.add(blog)
            } else {
                failedBlogs.add(blog)
            }
        }

        NewsUpdateResource(
            Status.SUCCESS,
            null,
            successfulBlogs,
            failedBlogs,
            activeBlogs.count()
        )
    }

    private suspend fun handleSelfHostedWpBlog(blog: Blog): Resource<Triple<List<News>, List<Author>, List<Media>>> {

        return try {

            val latestNewsDate = getLatestNewsPublishedDate(blog.feed_url).first().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            val response = apiJsonSelfHostedWPService.getJsonNewsOfUrl(blog.feed_url + SELF_HOSTED_URL_ENDING, newsAfterDate = latestNewsDate)

            val selfHostedWpPostList = response.body()

            if (response.isSuccessful && selfHostedWpPostList != null) {

                // set blog URL
                selfHostedWpPostList.map { it.blogLink = blog.feed_url }

                // Query for the authors
                val serverAuthors: MutableList<WordpressAuthor> = mutableListOf()
                selfHostedWpPostList.distinctBy { it.serverAuthorID }.forEach { post ->

                    try {
                        val author = apiJsonSelfHostedWPService.getJsonNewsAuthorByID(blog.feed_url + "wp-json/wp/v2/users/${post.serverAuthorID}/").body()

                        author?.let {
                            serverAuthors.add(author)
                        }
                    } catch (e: Exception) {
                        Timber.w(e, "Error in downloading an author from a self-hosted Wordpress blog")
                    }
                }

                // Query for all media elements per each post
                val serverMedia: MutableList<WordpressMedia> = mutableListOf()
                selfHostedWpPostList.forEach {
                    try {
                        val postMedia = apiJsonSelfHostedWPService.getJsonNewsMediaForPost(blog.feed_url + "wp-json/wp/v2/media?parent=${it.serverID}").body()

                        if (postMedia != null) {
                            serverMedia += postMedia
                        }
                    } catch (e: Throwable) {
                        Timber.w(e, "Error in downloading media from a self-hosted Wordpress blog")
                    }
                }

                // Set up foreign keys for media
                serverMedia.map { m ->
                    m.postLink =
                        selfHostedWpPostList.find { post -> post.serverID == m.serverPostID }?.link
                }
                serverMedia.map { m ->
                    m.authorLink = serverAuthors.find { author -> author.server_id == m.serverAuthorID }?.link
                }

                // Set up foreign key for posts
                selfHostedWpPostList.map { post ->
                    post.authorLink = serverAuthors.find { author -> author.server_id == post.serverAuthorID }?.link
                }
                selfHostedWpPostList.map { post ->
                    post.featuredMediaLink = serverMedia.find { media -> media.serverPostID == post.serverID }?.linkRaw ?: ""
                }

                // Map to local models
                val authors = serverAuthors.map { Author(it) }
                val news = selfHostedWpPostList.map { News(it) }
                val media = serverMedia.map { Media(it) }

                Timber.d("$selfHostedWpPostList")
                Timber.d("$serverAuthors")
                Timber.d("$serverMedia")

                streamLineElementsInContent(news)

                insertAuthorsNewsAndMedia(authors, news, media)

                Resource.success(Triple(news, authors, media), response.code())
            } else {
                Resource.error(UnknownError(), null, response.code())
            }
        } catch (e: Exception) {
            Timber.w(e, "Error in downloading self hosted Wordpress blog")
            Resource.error(e, null, null)
        }
    }

    private suspend fun handleRSSBlog(blog: Blog): Resource<Triple<List<News>, List<Author>, List<Media>>> {

        return try {

            val response = apiRssService.getRssNews(blog.feed_url)

            val channel = response.body()?.channel
            if (response.isSuccessful && channel != null) {

                val author = Author(blog, channel)

                val mediaList = mutableListOf<Media>()
                val newsList = mutableListOf<News>()

                channel.feedItems?.forEach { item ->

                    item.link?.let { itemLink ->

                        newsList.add(
                            News(
                                itemLink,
                                author.link,
                                0,
                                channel.image?.url,
                                item.title?.stripHtml(),
                                item.description,
                                item.pubDate?.toLocalDateTimeFromRFCString(),
                                blog.feed_url
                            )
                        )

                        if (blog.feedType == FeedType.WORDPRESS_COM) { // RSS feeds only have one media

                            newsList.last().description = item.wpContent

                            item.wpRSSMedia?.let { media ->

                                media.forEach {
                                    it.url?.let { url ->
                                        mediaList.add(
                                            Media(
                                                url,
                                                0,
                                                itemLink,
                                                author.link,
                                                it.medium,
                                                null,
                                                null,
                                                null
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                streamLineElementsInContent(newsList)

                Timber.d("$mediaList")
                insertAuthorsNewsAndMedia(listOf(author), newsList, mediaList)

                Resource.success(Triple(newsList, listOf(author), mediaList), response.code())
            } else {
                Resource.error(UnknownError(), null, response.code())
            }
        } catch (e: Exception) {
            Timber.w(e, "Error in downloading self hosted Wordpress blog")
            Resource.error(e, null, null)
        }
    }

    /**
     * Parses HTML and sets width of elements like images or iframes to the width of the phone
     */
    private fun streamLineElementsInContent(news: List<News>) {
        news.forEach { oneNewsItem ->
            oneNewsItem.description?.let { html ->
                val doc = Jsoup.parse(html)

                doc.select("img").attr("width", "100%")
                doc.select("figure").attr("style", "width: 80%")
                doc.select("iframe").attr("style", "width: 100%")

                oneNewsItem.description = doc.html()
            }
        }
    }

    private fun insertAuthorsNewsAndMedia(authors: List<Author>, news: List<News>, media: List<Media>) {
        database.runInTransaction {
            authorDao.insertAll(authors)
            newsDao.insertAll(news)
            mediaDao.insertAll(media)
        }
    }
}
