package at.ict4d.ict4dnews.lifecycle

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import io.reactivex.disposables.CompositeDisposable

open class RXLifecycleObserver(
    private val compositeDisposable: CompositeDisposable
) : DefaultLifecycleObserver {

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        compositeDisposable.dispose()
    }
}
