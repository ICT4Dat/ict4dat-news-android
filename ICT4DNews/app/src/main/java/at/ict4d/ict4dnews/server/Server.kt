package at.ict4d.ict4dnews.server

import at.ict4d.ict4dnews.extensions.stripHtml
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

class Server @Inject constructor(
        private val apiRSSService: ApiRSSService,
        private val apiJsonSelfHostedWPService: ApiJsonSelfHostedWPService,
        private val persistenceManager: IPersistenceManager
) : IServer {

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

                    // Strip HTML
                    posts.map { post ->
                        post.content[SelfHostedWPPost.SERIALIZED_RENDERED]?.let {
                            post.content[SelfHostedWPPost.SERIALIZED_RENDERED] = it.stripHtml()
                        }
                        post.title[SelfHostedWPPost.SERIALIZED_RENDERED]?.let {
                            post.title[SelfHostedWPPost.SERIALIZED_RENDERED] = it.stripHtml()
                        }
                    }
                    // Timber.d("*** $authors")
                    // Timber.d("*** $posts")
                    // Timber.d("*** $media")
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
                    Timber.e(it)
                }, {
                    Timber.d("onComplete ICT4D.at")
                })
    }
}