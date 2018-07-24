package at.ict4d.ict4dnews.lifecycle

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

open class RXLifecycleObserver @Inject constructor(
    private val compositeDisposable: CompositeDisposable
) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun disposeAll() {
        compositeDisposable.dispose()
    }
}