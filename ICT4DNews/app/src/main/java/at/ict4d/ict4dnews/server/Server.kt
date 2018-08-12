package at.ict4d.ict4dnews.server

import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.extensions.stripHtml
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
import org.threeten.bp.LocalDateTime
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

    override fun loadICT4DatRSSFeed(): Disposable {
        return apiRSSService.getRssICT4DatNews("") // TODO: fix URL
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                { channel ->
                    // TODO: implement and save to DB
                    Timber.d("*** $channel")
                },
                { error: Throwable? ->
                    Timber.e(error)
                })
    }

    override fun loadAllNewsFromAllActiveBlogs(): Disposable {

        val blogs = persistenceManager.getAllActiveBlogs()

        val requests = ArrayList<Single<*>>()

        for (blog in blogs) {
            if (blog.feedType == FeedType.SELF_HOSTED_WP_BLOG) {
                Timber.d(blog.url)
                requests.add(apiJsonSelfHostedWPService.getJsonNewsOfURL(blog.url + "/wp-json/wp/v2/posts")
                    .onErrorReturn { emptyList() }
                )
            } else if (blog.feedType == FeedType.RSS || blog.feedType == FeedType.WORDPRESS_COM) {
                /*
                requests.add(apiRSSService.getRssICT4DatNews(blog.url)
                    .onErrorReturn { RSSFeed(null) }
                )*/
            }
        }

        return Single.zip(requests) { serverResult ->
            Timber.d("Result: ${serverResult.size}")
            Timber.d("Result: ${serverResult[0]}")

            for (item in serverResult) {

                val serverResultBlogList = item as ArrayList<*>

                if (serverResultBlogList.isNotEmpty()) {
                    val firstItem = serverResultBlogList.first()
                    if (firstItem is SelfHostedWPPost) {
                        handleSelfHostedWPBlogList(blogs, serverResultBlogList)
                    } else if (firstItem is RSSFeed) {
                        // TODO: set up rss feed response and save to DB
                    }
                }
            }
            rxEventBus.post(NewsRefreshDoneMessage())
        }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({
                Timber.d("**** done: $it")
            }, {
                Timber.e("**** Error in downloading news from all active posts")
                handleError(it, NewsRefreshDoneMessage())
            })
    }

    private fun handleSelfHostedWPBlogList(databaseBlogList: List<Blog>, serverResultBlog: ArrayList<*>) {

        if (serverResultBlog.isNotEmpty() && serverResultBlog.first() is SelfHostedWPPost) {

            // set blog URL
            val dbBlog = databaseBlogList.find { dbBlog -> (serverResultBlog.first() as SelfHostedWPPost).link.contains(dbBlog.url) }
            serverResultBlog.map { b -> (b as SelfHostedWPPost).blogLink = dbBlog?.url ?: "" }

            // Query for the authors
            val serverAuthors: MutableList<WordpressAuthor> = mutableListOf()
            for (post in serverResultBlog.distinctBy { (it as SelfHostedWPPost).serverAuthorID }) {
                try {
                    val author = apiJsonSelfHostedWPService.getJsonNewsAuthorByID(dbBlog?.url + "wp-json/wp/v2/users/${(post as SelfHostedWPPost).serverAuthorID}/").execute().body()

                    author?.let {
                        serverAuthors.add(author)
                    }
                } catch (e: Throwable) {
                    Timber.e("Error in downloading an author from a self-hosted Wordpress blog", e)
                    rxEventBus.post(ServerErrorMessage(R.string.http_exception_error_message, e))
                    return
                }
            }

            // Query for all media elements per each post
            val serverMedia: MutableList<WordpressMedia> = mutableListOf()
            for (post in serverResultBlog) {
                try {
                    val postMedia = apiJsonSelfHostedWPService.getJsonNewsMediaForPost(dbBlog?.url + "wp-json/wp/v2/media?parent=${(post as SelfHostedWPPost).serverID}").execute().body()

                    if (postMedia != null) {
                        serverMedia += postMedia
                    }
                } catch (e: Throwable) {
                    Timber.e("Error in downloading media from a self-hosted Wordpress blog", e)
                    rxEventBus.post(ServerErrorMessage(R.string.http_exception_error_message, e))
                    return
                }
            }

            // Set up foreign keys for media
            serverMedia.map { m ->
                m.postLink = (serverResultBlog.find { post -> (post as SelfHostedWPPost).serverID == m.serverPostID } as? SelfHostedWPPost)?.link
            }
            serverMedia.map { m ->
                m.authorLink = serverAuthors.find { author -> author.server_id == m.serverAuthorID }?.link
            }

            // Set up foreign key for posts
            serverResultBlog.map { post ->
                (post as SelfHostedWPPost).authorLink = serverAuthors.find { author -> author.server_id == post.serverAuthorID }?.link
            }
            serverResultBlog.map { post ->
                (post as SelfHostedWPPost).featuredMediaLink = serverMedia.find { media -> media.serverPostID == post.serverID }?.linkRaw
                    ?: ""
            }

            // Map to local models
            val authors = serverAuthors.map { Author(it) }
            val news = serverResultBlog.map { News((it as SelfHostedWPPost)) }
            val media = serverMedia.map { Media(it) }

            // Strip HTML
            news.map { n ->
                n.title?.let {
                    n.title = it.stripHtml()
                }
                n.description?.let {
                    n.description = it.stripHtml()
                }
            }

            media.map { m ->
                m.description?.let {
                    m.description = it.stripHtml()
                }
            }

            Timber.d("$serverResultBlog")
            Timber.d("$serverAuthors")
            Timber.d("$serverMedia")

            persistenceManager.insertAllAuthors(authors)
            persistenceManager.insertAllNews(news)
            persistenceManager.insertAllMedia(media)
        }
    }

    override fun loadICT4DatJsonFeed(newsAfterDateTime: LocalDateTime): Disposable {

        return apiJsonSelfHostedWPService.getJsonICT4DatNews(newsAfterDate = newsAfterDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({ serverPosts: List<SelfHostedWPPost> ->

                // Query for the authors
                val serverAuthors: MutableList<WordpressAuthor> = mutableListOf()
                for (post in serverPosts.distinctBy { it.serverAuthorID }) {
                    try {
                        val author =
                            apiJsonSelfHostedWPService.getJsonICT4DatAuthorByID(serverAuthorID = post.serverAuthorID)
                                .execute().body()

                        author?.let {
                            serverAuthors.add(author)
                        }
                    } catch (e: Throwable) {
                        Timber.e("Error in downloading an author from a self-hosted Wordpress blog", e)
                        rxEventBus.post(ServerErrorMessage(R.string.http_exception_error_message, e))
                        return@subscribe
                    }
                }

                // Query for all media elements per each post
                val serverMedia: MutableList<WordpressMedia> = mutableListOf()
                for (post in serverPosts) {
                    try {
                        val postMedia =
                            apiJsonSelfHostedWPService.getJsonICT4DatMediaForPost(post.serverID).execute().body()

                        if (postMedia != null) {
                            serverMedia += postMedia
                        }
                    } catch (e: Throwable) {
                        Timber.e("Error in downloading media from a self-hosted Wordpress blog", e)
                        rxEventBus.post(ServerErrorMessage(R.string.http_exception_error_message, e))
                        return@subscribe
                    }
                }

                // Set up foreign keys for media
                serverMedia.map { m ->
                    m.postLink = serverPosts.find { post -> post.serverID == m.serverPostID }?.link ?: ""
                }
                serverMedia.map { m ->
                    m.authorLink = serverAuthors.find { author -> author.server_id == m.serverAuthorID }?.link ?: ""
                }

                // Set up foreign key for posts
                serverPosts.map { post ->
                    post.authorLink = serverAuthors.find { author -> author.server_id == post.serverAuthorID }?.link
                        ?: ""
                }
                serverPosts.map { post ->
                    post.featuredMediaLink = serverMedia.find { media -> media.serverPostID == post.serverID }?.linkRaw
                        ?: ""
                }

                // Map to local models
                val authors = serverAuthors.map { Author(it) }
                val news = serverPosts.map { News(it) }
                val media = serverMedia.map { Media(it) }

                // Strip HTML
                news.map { n ->
                    n.title?.let {
                        n.title = it.stripHtml()
                    }
                    n.description?.let {
                        n.description = it.stripHtml()
                    }
                }

                media.map { m ->
                    m.description?.let {
                        m.description = it.stripHtml()
                    }
                }

                Timber.d("$serverPosts")
                Timber.d("$serverAuthors")
                Timber.d("$serverMedia")

                persistenceManager.insertAllAuthors(authors)
                persistenceManager.insertAllNews(news)
                persistenceManager.insertAllMedia(media)
                rxEventBus.post(NewsRefreshDoneMessage())
            }, {
                Timber.e("Error in Self Hosted Wordpress Blog Call")
                handleError(it, NewsRefreshDoneMessage())
            })
    }

    override fun loadBlogs(): Disposable {
        return apiICT4DatNews.getBlogs()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({

                Timber.d("downloaded ${it.size} blogs from ICT4D.at")
                it.map { blog -> blog.active = true }
                persistenceManager.insertAll(it)
            }, {
                Timber.e("Error in Blogs Call")
                handleError(it, BlogsRefreshDoneMessage())
            })
    }

    private fun handleError(error: Throwable?, message: Any) {
        Timber.e(error)
        when (error) {
            is HttpException -> rxEventBus.post(ServerErrorMessage(R.string.http_exception_error_message, error))
            else -> rxEventBus.post(ServerErrorMessage(R.string.server_error_message, error))
        }
        rxEventBus.post(message)
    }
}