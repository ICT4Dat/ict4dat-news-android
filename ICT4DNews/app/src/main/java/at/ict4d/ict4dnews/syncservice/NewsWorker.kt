package at.ict4d.ict4dnews.syncservice

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import at.ict4d.ict4dnews.ICT4DNewsApplication
import at.ict4d.ict4dnews.server.IServer
import at.ict4d.ict4dnews.utils.NewsRefreshDoneMessage
import at.ict4d.ict4dnews.utils.RxEventBus
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

class NewsWorker(context: Context, workParams: WorkerParameters) : Worker(context, workParams) {

    @Inject
    protected lateinit var server: IServer

    @Inject
    protected lateinit var rxEventBus: RxEventBus

    private val compositeDisposable = CompositeDisposable()

    init {
        ICT4DNewsApplication.instance.component.inject(this)
        compositeDisposable.add(rxEventBus.filteredObservable(NewsRefreshDoneMessage::class.java).subscribe {
            Timber.d("News updated successfully")
        })
    }

    override fun doWork(): Result {
        Timber.d("Start the doWork")
        server.loadAllNewsFromAllActiveBlogs()
        Timber.d("End the doWork")

        return Result.SUCCESS
    }
}