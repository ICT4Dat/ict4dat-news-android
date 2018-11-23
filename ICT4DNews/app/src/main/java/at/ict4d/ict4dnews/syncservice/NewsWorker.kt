package at.ict4d.ict4dnews.syncservice

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import at.ict4d.ict4dnews.ICT4DNewsApplication
import at.ict4d.ict4dnews.server.IServer
import at.ict4d.ict4dnews.utils.NewsRefreshDoneMessage
import at.ict4d.ict4dnews.utils.RxEventBus
import at.ict4d.ict4dnews.utils.ServerErrorMessage
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import java.util.concurrent.CountDownLatch
import javax.inject.Inject

class NewsWorker(context: Context, workParams: WorkerParameters) : Worker(context, workParams) {

    @Inject
    protected lateinit var server: IServer

    @Inject
    protected lateinit var rxEventBus: RxEventBus

    private val compositeDisposable = CompositeDisposable()

    private var isNewsLoadedSuccessfully: Boolean = false

    private val countDownLatch: CountDownLatch = CountDownLatch(1)

    // TODO("Remove all of these log statements")

    init {
        ICT4DNewsApplication.instance.component.inject(this)

        compositeDisposable.add(rxEventBus.filteredObservable(NewsRefreshDoneMessage::class.java).subscribe {
            isNewsLoadedSuccessfully = true
            Timber.d("News updated successfully latch count is ----> ${countDownLatch.count}")
            countDownLatch.countDown()
        })

        compositeDisposable.add(rxEventBus.filteredObservable(ServerErrorMessage::class.java).subscribe {
            isNewsLoadedSuccessfully = false
            Timber.d("Failed to update news")
            countDownLatch.countDown()
        })
    }

    override fun doWork(): Result {
        Timber.d("Start the doWork status of news loading ----> $isNewsLoadedSuccessfully")

        server.loadAllNewsFromAllActiveBlogs()
        if (countDownLatch.count > 0) {
            Timber.d("Going to call await for this thread")
            countDownLatch.await()
        }
        Timber.d("End the doWork status of news loading ----> $isNewsLoadedSuccessfully --- count down of latch is ---> ${countDownLatch.count}")

        return if (isNewsLoadedSuccessfully) Result.SUCCESS else Result.RETRY
    }
}