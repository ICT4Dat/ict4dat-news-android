package at.ict4d.ict4dnews.server

import at.ict4d.ict4dnews.ICT4DNewsApplication
import at.ict4d.ict4dnews.models.wordpress.SelfHostedWPPost
import at.ict4d.ict4dnews.models.wordpress.WordpressAuthor
import at.ict4d.ict4dnews.models.wordpress.WordpressMedia
import at.ict4d.ict4dnews.persistence.IPersistenceManager
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class Server : IServer {

    @Inject
    protected lateinit var apiRSSService: ApiRSSService

    @Inject
    protected lateinit var apiJsonSelfHostedWPService: ApiJsonSelfHostedWPService

    @Inject
    protected lateinit var persistenceManager: IPersistenceManager

    init {
        ICT4DNewsApplication.component.inject(this)
    }

    override fun loadICT4DatRSSFeed(): Disposable {
        return apiRSSService.getRssICT4DatNews()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(
                        { channel ->
                            Timber.d("*** $channel")
                        },
                        { error: Throwable? ->
                            Timber.e(error)
                        })
    }

    override fun loadICT4DatJsonFeed(): Disposable {
        return Flowable.zip(
                apiJsonSelfHostedWPService.getJsonICT4DatAuthors(),
                apiJsonSelfHostedWPService.getJsonICT4DatNews(),
                BiFunction { authors: List<WordpressAuthor>, posts: List<SelfHostedWPPost> ->

                    // Set up forgein key for posts
                    posts.map { post -> post.authorLink = authors.find { author -> author.server_id == post.serverAuthor }?.link ?: "" }

                    // Query for all media elements per each post
                    val media: MutableList<WordpressMedia> = mutableListOf()
                    for (post in posts) {
                        val postMedia = apiJsonSelfHostedWPService.getJsonICT4DatMediaForPost(post.serverID).execute().body()

                        if (postMedia != null) {
                            media += postMedia
                        }
                    }

                    // Set up forgein keys for media
                    media.map { m -> m.postLink = posts.find { post -> post.serverID == m.serverPostID }?.link ?: "" }
                    media.map { m -> m.authorLink = authors.find { author -> author.server_id == m.serverAuthor }?.link ?: "" }

                    // Timber.d("*** $authors")
                    // Timber.d("*** $posts")
                    //Timber.d("*** $media")
                    persistenceManager.insertAllWordpressAuthors(authors)
                    persistenceManager.insertAllSelfHostedWPPosts(posts)
                    persistenceManager.insertAllWordpressMedia(media)
                }
        )
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe({
                    Timber.d("onNext: $it")
                }, {
                    Timber.e("Error in ICT4D.at Call", it)
                }, {
                    Timber.d("onComplete ICT4D.at")
                })
    }
}