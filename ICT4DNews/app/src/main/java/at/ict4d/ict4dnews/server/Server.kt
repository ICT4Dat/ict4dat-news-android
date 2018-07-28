package at.ict4d.ict4dnews.server

import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.extensions.stripHtml
import at.ict4d.ict4dnews.models.Author
import at.ict4d.ict4dnews.models.Media
import at.ict4d.ict4dnews.models.News
import at.ict4d.ict4dnews.models.wordpress.SelfHostedWPPost
import at.ict4d.ict4dnews.models.wordpress.WordpressAuthor
import at.ict4d.ict4dnews.models.wordpress.WordpressMedia
import at.ict4d.ict4dnews.persistence.IPersistenceManager
import at.ict4d.ict4dnews.utils.NewsRefreshDoneMessage
import at.ict4d.ict4dnews.utils.RxEventBus
import at.ict4d.ict4dnews.utils.ServerErrorMessage
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
    private val persistenceManager: IPersistenceManager,
    private val rxEventBus: RxEventBus
) : IServer {

    override fun loadICT4DatRSSFeed(): Disposable {
        return apiRSSService.getRssICT4DatNews()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                { channel ->
                    Timber.d("*** $channel")
                },
                { error: Throwable? ->
                    Timber.e(error)
                })
    }

    override fun loadICT4DatJsonFeed(newsAfterDateTime: LocalDateTime): Disposable {

        return apiJsonSelfHostedWPService.getJsonICT4DatNews(newsAfterDate = newsAfterDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({ serverPosts: List<SelfHostedWPPost> ->

                // Query for the authors
                val serverAuthors: MutableList<WordpressAuthor> = mutableListOf()
                for (post in serverPosts.distinctBy { it.serverAuthorID }) {
                    val author =
                        apiJsonSelfHostedWPService.getJsonICT4DatAuthorByID(serverAuthorID = post.serverAuthorID)
                            .execute().body()

                    author?.let {
                        serverAuthors.add(author)
                    }
                }

                // Query for all media elements per each post
                val serverMedia: MutableList<WordpressMedia> = mutableListOf()
                for (post in serverPosts) {
                    val postMedia =
                        apiJsonSelfHostedWPService.getJsonICT4DatMediaForPost(post.serverID).execute().body()

                    if (postMedia != null) {
                        serverMedia += postMedia
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
                    post.authorLink = serverAuthors.find { author -> author.server_id == post.serverAuthorID }?.link ?:
                        ""
                }
                serverPosts.map { post ->
                    post.featuredMediaLink = serverMedia.find { media -> media.serverPostID == post.serverID }?.linkRaw ?:
                        ""
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
                Timber.e("Error in ICT4D.at Call")
                Timber.e(it)
                when (it) {
                    is HttpException -> rxEventBus.post(ServerErrorMessage(R.string.http_exception_error_message, it))
                    else ->
                        rxEventBus.post(ServerErrorMessage(R.string.server_error_message, it))
                }
            })
    }
}