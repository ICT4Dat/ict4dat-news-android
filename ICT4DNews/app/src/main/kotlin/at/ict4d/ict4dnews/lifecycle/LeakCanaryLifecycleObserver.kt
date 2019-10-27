package at.ict4d.ict4dnews.lifecycle

import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import at.ict4d.ict4dnews.ICT4DNewsApplication

class LeakCanaryLifecycleObserver(private val context: Context, private val toWatch: Any) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun addWatcher() {
        ICT4DNewsApplication.getRefWatcher(context).watch(toWatch)
    }
}
