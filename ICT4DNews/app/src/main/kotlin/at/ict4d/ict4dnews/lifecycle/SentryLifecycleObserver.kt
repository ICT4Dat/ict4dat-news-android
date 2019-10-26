package at.ict4d.ict4dnews.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import at.ict4d.ict4dnews.utils.recordLifecycleBreadcrumb

class SentryLifecycleObserver(val lifecycleOwner: LifecycleOwner) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun addOnCreateBreadcrumb() {
        recordLifecycleBreadcrumb("OnCreate", lifecycleOwner)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun addOnResumeBreadcrumb() {
        recordLifecycleBreadcrumb("OnResume", lifecycleOwner)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun addOnPauseBreadcrumb() {
        recordLifecycleBreadcrumb("OnPause", lifecycleOwner)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun addOnDestroyBreadcrumb() {
        recordLifecycleBreadcrumb("OnDestroy", lifecycleOwner)
    }
}
