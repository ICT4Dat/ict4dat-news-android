package at.ict4d.ict4dnews.lifecycle

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import at.ict4d.ict4dnews.utils.recordLifecycleBreadcrumb

class SentryLifecycleObserver(private val lifecycleOwner: LifecycleOwner) :
    DefaultLifecycleObserver {

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        recordLifecycleBreadcrumb("OnCreate", lifecycleOwner)
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        recordLifecycleBreadcrumb("OnResume", lifecycleOwner)
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        recordLifecycleBreadcrumb("OnPause", lifecycleOwner)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        recordLifecycleBreadcrumb("OnDestroy", lifecycleOwner)
    }
}
