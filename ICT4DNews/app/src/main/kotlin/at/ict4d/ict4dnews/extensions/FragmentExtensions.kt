package at.ict4d.ict4dnews.extensions

import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.server.utils.Resource
import at.ict4d.ict4dnews.server.utils.Status
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import timber.log.Timber

fun <T> Fragment.handleApiResponse(
    resource: Resource<T>,
    swipeRefreshLayout: SwipeRefreshLayout? = null,
    progressbar: ProgressBar? = null,
    errorTextView: TextView? = null,
    showErrorDialogs: Boolean = true
) {

    swipeRefreshLayout?.isRefreshing = resource.status == Status.LOADING
    progressbar?.isVisible = resource.status == Status.LOADING
    errorTextView?.isVisible = (resource.status == Status.ERROR || resource.status == Status.SUCCESS) && (resource.data == null || (resource.data as? List<*>).isNullOrEmpty())

    when (resource.status) {
        Status.SUCCESS -> Timber.d("${this::class.java.simpleName} repository update success with Status ${resource.status}")
        Status.ERROR -> {
            if (showErrorDialogs) {

                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.error_dialog_title)
                    .setMessage(R.string.error_dialog_msg)
                    .setPositiveButton(R.string.dialog_ok, null)
                    .show()
            }

            Timber.w(resource.throwable, "Response Code: ${resource.responseCode}")
        }
        Status.LOADING -> Timber.d("${this::class.java.simpleName} repository loading with Status ${resource.status}")
    }
}
