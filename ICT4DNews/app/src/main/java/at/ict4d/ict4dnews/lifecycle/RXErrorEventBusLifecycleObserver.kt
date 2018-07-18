package at.ict4d.ict4dnews.lifecycle

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import android.support.v7.app.AppCompatActivity
import at.ict4d.ict4dnews.ICT4DNewsApplication
import at.ict4d.ict4dnews.utils.RxEventBus
import at.ict4d.ict4dnews.utils.ServerErrorMessage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class RXErrorEventBusLifecycleObserver(
    private val activity: AppCompatActivity,
    private val compositeDisposable: CompositeDisposable
) : RXLifecycleObserver(compositeDisposable) {

    @Inject
    protected lateinit var eventBus: RxEventBus

    private var eventBusObserver: Disposable? = null

    init {
        ICT4DNewsApplication.component.inject(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun registerEventBus() {
        eventBusObserver = eventBus.filteredObservable(ServerErrorMessage::class.java)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (!activity.isFinishing && !activity.isChangingConfigurations) {
                        // TODO: show dialog here
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