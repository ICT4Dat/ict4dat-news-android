package at.ict4d.ict4dnews.utils

import androidx.annotation.Nullable
import androidx.lifecycle.LifecycleOwner
import io.sentry.Breadcrumb
import io.sentry.Sentry
import io.sentry.SentryLevel

const val SENTRY_HANDLER_CATEGORY_LIFECYCLE = "Lifecycle"
const val SENTRY_HANDLER_CATEGORY_NETWORK = "Network"
const val SENTRY_HANDLER_CATEGORY_NAVIGATION = "Navigation"
const val SENTRY_HANDLER_CATEGORY_ACTION = "Action"

fun recordNavigationBreadcrumb(
    message: String,
    context: Any,
    @Nullable data: Map<String, String>? = null
) = Sentry.addBreadcrumb(Breadcrumb().apply {
    category = SENTRY_HANDLER_CATEGORY_NAVIGATION
    level = SentryLevel.INFO
    setMessage("${context::class.java.simpleName}: $message")
    data?.forEach {
        setData(it.key, it.value)
    }
})

fun recordActionBreadcrumb(
    message: String,
    context: Any,
    @Nullable data: Map<String, String>? = null
) = Sentry.addBreadcrumb(Breadcrumb().apply {
    category = SENTRY_HANDLER_CATEGORY_ACTION
    level = SentryLevel.INFO
    setMessage("${context::class.java.simpleName}: $message")
    data?.forEach {
        setData(it.key, it.value)
    }
})

fun recordNetworkBreadcrumb(
    message: String,
    context: Any,
    @Nullable data: Map<String, String>? = null
) = Sentry.addBreadcrumb(Breadcrumb().apply {
    category = SENTRY_HANDLER_CATEGORY_NETWORK
    level = SentryLevel.INFO
    setMessage("${context::class.java.simpleName}: $message")
    data?.forEach {
        setData(it.key, it.value)
    }
})

fun recordLifecycleBreadcrumb(message: String, lifecycleOwner: LifecycleOwner) =
    Sentry.addBreadcrumb(Breadcrumb().apply {
        category = SENTRY_HANDLER_CATEGORY_LIFECYCLE
        level = SentryLevel.INFO
        setMessage("${lifecycleOwner::class.java.simpleName}: $message")
    })
