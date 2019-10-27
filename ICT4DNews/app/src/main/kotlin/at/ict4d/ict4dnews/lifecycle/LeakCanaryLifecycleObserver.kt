package at.ict4d.ict4dnews.lifecycle

import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import at.ict4d.ict4dnews.ICT4DNewsApplication

class LeakCanaryLifecycleObserver(private val context: Context, private val toWatch: Any) :
    DefaultLifecycleObserver {

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        ICT4DNewsApplication.getRefWatcher(context).watch(toWatch)
    }
}
