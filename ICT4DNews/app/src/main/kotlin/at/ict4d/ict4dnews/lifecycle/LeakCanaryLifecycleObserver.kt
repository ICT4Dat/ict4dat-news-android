package at.ict4d.ict4dnews.lifecycle

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.content.Context
import at.ict4d.ict4dnews.ICT4DNewsApplication

class LeakCanaryLifecycleObserver(private val context: Context, private val toWatch: Any) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun addWatcher() {
        ICT4DNewsApplication.getRefWatcher(context).watch(toWatch)
    }
}