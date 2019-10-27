package at.ict4d.ict4dnews.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import io.reactivex.disposables.CompositeDisposable

open class RXLifecycleObserver(
    private val compositeDisposable: CompositeDisposable
) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun disposeAll() {
        compositeDisposable.dispose()
    }
}
