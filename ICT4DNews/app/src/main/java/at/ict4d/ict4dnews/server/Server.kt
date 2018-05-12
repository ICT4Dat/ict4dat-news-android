package at.ict4d.ict4dnews.server

import at.ict4d.ict4dnews.ICT4DNewsApplication
import at.ict4d.ict4dnews.persistence.database.dao.SelfHostedWPPostDao
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
    protected lateinit var selfHostedWPPostDao: SelfHostedWPPostDao

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
        return apiJsonSelfHostedWPService.getJSONICT4DatNews()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(
                        { posts ->
                            Timber.d("*** $posts")
                            selfHostedWPPostDao.insertAll(posts)
                        },
                        { error: Throwable? ->
                            Timber.e(error)
                        })
    }
}