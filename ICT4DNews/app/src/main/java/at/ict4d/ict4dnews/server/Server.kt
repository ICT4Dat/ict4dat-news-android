package at.ict4d.ict4dnews.server

import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.extensions.toLocalDateTimeFromRFCString
import at.ict4d.ict4dnews.models.Author
import at.ict4d.ict4dnews.models.Blog
import at.ict4d.ict4dnews.models.FeedType
import at.ict4d.ict4dnews.models.Media
import at.ict4d.ict4dnews.models.News
import at.ict4d.ict4dnews.models.rss.RSSFeed
import at.ict4d.ict4dnews.models.wordpress.SelfHostedWPPost
import at.ict4d.ict4dnews.models.wordpress.WordpressAuthor
import at.ict4d.ict4dnews.models.wordpress.WordpressMedia
import at.ict4d.ict4dnews.persistence.IPersistenceManager
import at.ict4d.ict4dnews.utils.BlogsRefreshDoneMessage
import at.ict4d.ict4dnews.utils.NewsRefreshDoneMessage
import at.ict4d.ict4dnews.utils.RxEventBus
import at.ict4d.ict4dnews.utils.ServerErrorMessage
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

class Server @Inject constructor(
    private val apiRSSService: ApiRSSService,
    private val apiJsonSelfHostedWPService: ApiJsonSelfHostedWPService,
    private val apiICT4DatNews: ApiICT4DatNews,
    private val persistenceManager: IPersistenceManager,
    private val rxEventBus: RxEventBus
) : IServer {

    /**
     * @see IServer
     */
    override fun loadAllNewsFromAllActiveBlogs(): Disposable {

        val blogs = persistenceManager.getAllActiveBlogs()

        val requests = ArrayList<Single<*>>()
        blogs.forEach { blog ->
            if (blog.feedType == FeedType.SELF_HOSTED_WP_BLOG) {
                Timber.d(blog.feed_url)
                requests.add(apiJsonSelfHostedWPService.getJsonNewsOfURL(
                    blog.feed_url + "wp-json/wp/v2/posts",
                    newsAfterDate = persistenceManager.getLatestNewsPublishedDate(blogID = blog.feed_url).format(
                        DateTimeFormatter.ISO_LOCAL_DATE_TIME
                    )
                    )
                    .onErrorReturn { emptyList() }
                )
            } else if (blog.feedType == FeedType.RSS || blog.feedType == FeedType.WORDPRESS_COM) {
                requests.add(apiRSSService.getRssNews(blog.feed_url)
                    .onErrorReturn { RSSFeed(null) }
                )
            }
        }

        return Single.zip(requests) { serverResult ->
            Timber.d("Result: ${serverResult.size}")
            Timber.d("Result: ${serverResult[0]}")

            serverResult.forEach {

                if (it is List<*> && it.isNotEmpty() && it.first() is SelfHostedWPPost) {
                    handleSelfHostedWPBlogList(blogs, it)
                } else if (it is RSSFeed) {
                    handleRSSList(blogs, it)
                }
            }
            persistenceManager.getLastAutomaticNewsUpdateLocalDate().set(LocalDate.now())
            rxEventBus.post(NewsRefreshDoneMessage())
        }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({
                Timber.d("**** done: $it")
            }, {
                Timber.e(it, "**** Error in downloading news from all active posts")
                handleError(it, NewsRefreshDoneMessage())
            })
    }

    private fun handleSelfHostedWPBlogList(databaseBlogList: List<Blog>, serverResultBlog: List<*>) {

        if (serverResultBlog.isNotEmpty() && serverResultBlog.first() is SelfHostedWPPost) {

            // set blog URL
            val dbBlog =
                databaseBlogList.find { dbBlog -> (serverResultBlog.first() as SelfHostedWPPost).link.contains(dbBlog.feed_url) }
            serverResultBlog.map { b -> (b as SelfHostedWPPost).blogLink = dbBlog?.feed_url ?: "" }

            // Query for the authors
            val serverAuthors: MutableList<WordpressAuthor> = mutableListOf()
            serverResultBlog.distinctBy { (it as SelfHostedWPPost).serverAuthorID }.forEach { post ->
                try {
                    val author =
                        apiJsonSelfHostedWPService.getJsonNewsAuthorByID(dbBlog?.feed_url + "wp-json/wp/v2/users/${(post as SelfHostedWPPost).serverAuthorID}/")
                            .execute().body()

                    author?.let {
                        serverAuthors.add(author)
                    }
                } catch (e: Throwable) {
                    Timber.e(e, "Error in downloading an author from a self-hosted Wordpress blog")
                    rxEventBus.post(ServerErrorMessage(R.string.http_exception_error_message, e))
                    return
                }
            }

            // Query for all media elements per each post
            val serverMedia: MutableList<WordpressMedia> = mutableListOf()
            serverResultBlog.forEach {
                try {
                    val postMedia =
                        apiJsonSelfHostedWPService.getJsonNewsMediaForPost(dbBlog?.feed_url + "wp-json/wp/v2/media?parent=${(it as SelfHostedWPPost).serverID}")
                            .execute().body()

                    if (postMedia != null) {
                        serverMedia += postMedia
                    }
                } catch (e: Throwable) {
                    Timber.e(e, "Error in downloading media from a self-hosted Wordpress blog")
                    rxEventBus.post(ServerErrorMessage(R.string.http_exception_error_message, e))
                    return
                }
            }

            // Set up foreign keys for media
            serverMedia.map { m ->
                m.postLink =
                    (serverResultBlog.find { post -> (post as SelfHostedWPPost).serverID == m.serverPostID } as? SelfHostedWPPost)?.link
            }
            serverMedia.map { m ->
                m.authorLink = serverAuthors.find { author -> author.server_id == m.serverAuthorID }?.link
            }

            // Set up foreign key for posts
            serverResultBlog.map { post ->
                (post as SelfHostedWPPost).authorLink =
                    serverAuthors.find { author -> author.server_id == post.serverAuthorID }?.link
            }
            serverResultBlog.map { post ->
                (post as SelfHostedWPPost).featuredMediaLink = serverMedia.find { media -> media.serverPostID == post.serverID }?.linkRaw
                    ?: ""
            }

            // Map to local models
            val authors = serverAuthors.map { Author(it) }
            val news = serverResultBlog.map { News((it as SelfHostedWPPost)) }
            val media = serverMedia.map { Media(it) }

            Timber.d("$serverResultBlog")
            Timber.d("$serverAuthors")
            Timber.d("$serverMedia")

            persistenceManager.insertAllAuthors(authors)
            persistenceManager.insertAllNews(news)
            persistenceManager.insertAllMedia(media)
        }
    }

    private fun handleRSSList(databaseBlogList: List<Blog>, rssFeed: RSSFeed) {
        Timber.d("$rssFeed")

        rssFeed.channel?.let { channel ->

            val endIndex = Math.min(rssFeed.channel?.feedItems?.first()?.link?.length ?: 0, 15)

            databaseBlogList.find { dbBlog ->
                rssFeed.channel?.feedItems?.first()?.link?.substring(0, endIndex)?.equals(
                    dbBlog.feed_url.substring(0, endIndex)
                ) ?: false
            }?.let { blog ->

                val author = Author(blog, channel)

                val mediaList = mutableListOf<Media>()
                val newsList = mutableListOf<News>()
                channel.feedItems?.let { items ->
                    items.forEach { item ->

                        item.link?.let { itemLink ->

                            newsList.add(
                                News(
                                    itemLink,
                                    author.link,
                                    0,
                                    channel.image?.url,
                                    item.title,
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
                }

                Timber.d("$mediaList")

                persistenceManager.insertAuthor(author)
                persistenceManager.insertAllNews(newsList)
                persistenceManager.insertAllMedia(mediaList)
            }
        }
    }

    /**
     * @see IServer
     */
    override fun loadBlogs(): Disposable {
        return apiICT4DatNews.getBlogs()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({ newBlogs ->

                newBlogs.map { blog -> if (blog.logoURL == "null") blog.logoURL = null }

                val oldBlogs = persistenceManager.getAllBlogsAsList()
                newBlogs.map { blog ->
                    blog.active = oldBlogs.find { blog.feed_url == it.feed_url }?.active ?: true
                }

                persistenceManager.insertAll(newBlogs)

                Timber.d("downloaded ${newBlogs.size} blogs from ICT4D.at")
            }, {
                Timber.e(it, "Error in Blogs Call")
                handleError(it, BlogsRefreshDoneMessage())
            })
    }

    private fun handleError(error: Throwable?, message: Any) {
        when (error) {
            is HttpException -> rxEventBus.post(ServerErrorMessage(R.string.http_exception_error_message, error))
            else -> rxEventBus.post(ServerErrorMessage(R.string.server_error_message, error))
        }
        rxEventBus.post(message)
    }
}