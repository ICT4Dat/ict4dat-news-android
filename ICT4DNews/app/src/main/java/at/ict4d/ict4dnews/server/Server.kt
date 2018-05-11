package at.ict4d.ict4dnews.server

import at.ict4d.ict4dnews.ICT4DNewsApplication
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class Server : IServer {

    @Inject
    protected lateinit var apiRSSService: ApiRSSService

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
}