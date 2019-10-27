package at.ict4d.ict4dnews.utils

import androidx.annotation.Nullable
import androidx.lifecycle.LifecycleOwner
import io.sentry.Sentry
import io.sentry.event.Breadcrumb
import io.sentry.event.BreadcrumbBuilder

const val SENTRY_HANDLER_CATEGORY_LIFECYCLE = "Lifecycle"
const val SENTRY_HANDLER_CATEGORY_NETWORK = "Network"
const val SENTRY_HANDLER_CATEGORY_CLICK = "Navigation"
const val SENTRY_HANDLER_CATEGORY_ACTION = "Action"

fun recordNavigationBreadcrumb(message: String, context: Any, @Nullable data: Map<String, String>? = null) {
    Sentry.getContext().recordBreadcrumb(
        BreadcrumbBuilder()
            .setCategory(SENTRY_HANDLER_CATEGORY_CLICK)
            .setType(Breadcrumb.Type.NAVIGATION)
            .setLevel(Breadcrumb.Level.INFO)
            .setMessage("${context::class.java.simpleName}: $message")
            .setData(data)
            .build()
    )
}

fun recordActionBreadcrumb(message: String, context: Any, @Nullable data: Map<String, String>? = null) {
    Sentry.getContext().recordBreadcrumb(
        BreadcrumbBuilder()
            .setCategory(SENTRY_HANDLER_CATEGORY_ACTION)
            .setType(Breadcrumb.Type.USER)
            .setLevel(Breadcrumb.Level.INFO)
            .setMessage("${context::class.java.simpleName}: $message")
            .setData(data)
            .build()
    )
}

fun recordNetworkBreadcrumb(message: String, context: Any, @Nullable data: Map<String, String>? = null) {
    Sentry.getContext().recordBreadcrumb(
        BreadcrumbBuilder()
            .setCategory(SENTRY_HANDLER_CATEGORY_NETWORK)
            .setType(Breadcrumb.Type.DEFAULT)
            .setLevel(Breadcrumb.Level.INFO)
            .setMessage("${context::class.java.simpleName}: $message")
            .setData(data)
            .build()
    )
}

fun recordLifecycleBreadcrumb(message: String, lifecycleOwner: LifecycleOwner) {
    Sentry.getContext().recordBreadcrumb(
        BreadcrumbBuilder()
            .setCategory(SENTRY_HANDLER_CATEGORY_LIFECYCLE)
            .setType(Breadcrumb.Type.DEFAULT)
            .setLevel(Breadcrumb.Level.INFO)
            .setMessage("${lifecycleOwner::class.java.simpleName}: $message")
            .build()
    )
}
