package at.ict4d.ict4dnews.lifecycle

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import at.ict4d.ict4dnews.extensions.filterObservableAndSetThread
import at.ict4d.ict4dnews.utils.RxEventBus
import at.ict4d.ict4dnews.utils.ServerErrorMessage
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.toast
import timber.log.Timber

class RXErrorEventBusLifecycleObserver(
    private val activity: AppCompatActivity,
    private val compositeDisposable: CompositeDisposable,
    private val eventBus: RxEventBus
) : RXLifecycleObserver(compositeDisposable) {

    private var eventBusObserver: Disposable? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun registerEventBus() {
        eventBusObserver =
            eventBus.filterObservableAndSetThread<ServerErrorMessage>(subscribeThread = Schedulers.single())
                .subscribe {
                    if (!activity.isFinishing && !activity.isChangingConfigurations) {
                        activity.toast(it.message)
                    }
                    Timber.e(it.throwable)
                }
        compositeDisposable.add(eventBusObserver as Disposable)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun disconnectEventBus() {
        eventBusObserver?.dispose()
    }
}
