package at.ict4d.ict4dnews.server

import at.ict4d.ict4dnews.extensions.stripHtml
import at.ict4d.ict4dnews.models.AuthorModel
import at.ict4d.ict4dnews.models.MediaModel
import at.ict4d.ict4dnews.models.NewsModel
import at.ict4d.ict4dnews.models.wordpress.SelfHostedWPPost
import at.ict4d.ict4dnews.models.wordpress.WordpressAuthor
import at.ict4d.ict4dnews.models.wordpress.WordpressMedia
import at.ict4d.ict4dnews.persistence.IPersistenceManager
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class Server @Inject constructor(
    private val apiRSSService: ApiRSSService,
    private val apiJsonSelfHostedWPService: ApiJsonSelfHostedWPService,
    private val persistenceManager: IPersistenceManager
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

    override fun loadICT4DatJsonFeed(): Disposable {

        return apiJsonSelfHostedWPService.getJsonICT4DatNews()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe({ serverPosts: List<SelfHostedWPPost> ->

                    // Query for the authors
                    val serverAuthors: MutableList<WordpressAuthor> = mutableListOf()
                    for (post in serverPosts.distinctBy { it.serverAuthorID }) {
                        val author = apiJsonSelfHostedWPService.getJsonICT4DatAuthorByID(serverAuthorID = post.serverAuthorID).execute().body()

                        author?.let {
                            serverAuthors.add(author)
                        }
                    }

                    // Query for all media elements per each post
                    val serverMedia: MutableList<WordpressMedia> = mutableListOf()
                    for (post in serverPosts) {
                        val postMedia = apiJsonSelfHostedWPService.getJsonICT4DatMediaForPost(post.serverID).execute().body()

                        if (postMedia != null) {
                            serverMedia += postMedia
                        }
                    }

                    // Set up foreign keys for media
                    serverMedia.map { m -> m.postLink = serverPosts.find { post -> post.serverID == m.serverPostID }?.link ?: "" }
                    serverMedia.map { m -> m.authorLink = serverAuthors.find { author -> author.server_id == m.serverAuthorID }?.link ?: "" }

                    // Set up foreign key for posts
                    serverPosts.map { post -> post.authorLink = serverAuthors.find { author -> author.server_id == post.serverAuthorID }?.link ?: "" }
                    serverPosts.map { post -> post.featuredMediaLink = serverMedia.find { media -> media.serverPostID == post.serverID }?.linkRaw ?: "" }

                    // Map to local models
                    val authors = serverAuthors.map { AuthorModel(it) }
                    val news = serverPosts.map { NewsModel(it) }
                    val media = serverMedia.map { MediaModel(it) }

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
                }, {
                    Timber.e("Error in ICT4D.at Call")
                    Timber.e(it)
                })
    }
}