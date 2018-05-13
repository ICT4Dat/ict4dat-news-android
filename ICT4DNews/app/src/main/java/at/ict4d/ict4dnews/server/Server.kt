package at.ict4d.ict4dnews.server

import at.ict4d.ict4dnews.ICT4DNewsApplication
import at.ict4d.ict4dnews.models.wordpress.WordpressAuthor
import at.ict4d.ict4dnews.persistence.IPersistenceManager
import io.reactivex.disposables.Disposable
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
        return apiJsonSelfHostedWPService.getJsonICT4DatAuthors().flatMap { authors: List<WordpressAuthor> ->
            Timber.d("*** $authors")
            persistenceManager.insertAllAuthors(authors)
            return@flatMap apiJsonSelfHostedWPService.getJsonICT4DatNews()
        }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(
                        { posts ->
                            Timber.d("*** $posts")
                            persistenceManager.insertAllSelfHostedWPPosts(posts)
                        },
                        { error: Throwable? ->
                            Timber.e(error)
                        }, {
                    Timber.d("Upadate ICT4D.at complete")
                })
    }
}