package at.ict4d.ict4dnews.screens.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.extensions.browseCustomTabWithUrl
import at.ict4d.ict4dnews.extensions.email
import at.ict4d.ict4dnews.extensions.navigateSafe
import at.ict4d.ict4dnews.extensions.openGooglePlayApp
import at.ict4d.ict4dnews.extensions.share
import at.ict4d.ict4dnews.ui.theme.AppTheme
import at.ict4d.ict4dnews.utils.recordActionBreadcrumb
import at.ict4d.ict4dnews.utils.recordNavigationBreadcrumb

class MoreFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                AppTheme {
                    MoreScreen(
                        onRateApp = { rateApp() },
                        onShareApp = { shareApp() },
                        onContactUs = { contactUs() },
                        onOpenUrl = { openUrlInCustomTab(it) },
                        onMenuSettingsSelected = { onMenuSettingsSelected() },
                        onOpenGithubProject = { openUrlInCustomTab(R.string.url_github_project) },
                    )
                }
            }
        }
    }

    private fun rateApp() {
        recordActionBreadcrumb("openGooglePlayApp", this)
        requireActivity().openGooglePlayApp()
    }

    private fun shareApp() {
        recordActionBreadcrumb("shareApplication", this)
        requireActivity().share(
            getString(
                R.string.share_app_text,
                "http://play.google.com/store/apps/details?id=${context?.packageName}",
            ),
        )
    }

    private fun contactUs() {
        recordActionBreadcrumb("contactUs", this)

        requireActivity().email(
            email = getString(R.string.contact_email),
            subject = getString(R.string.contact_mail_subject),
            text = getString(R.string.contact_mail_text),
        )
    }

    private fun openUrlInCustomTab(
        @StringRes stringRes: Int,
    ) {
        recordActionBreadcrumb(getString(stringRes), this)
        requireActivity().browseCustomTabWithUrl(getString(stringRes))
    }

    private fun onMenuSettingsSelected() {
        recordNavigationBreadcrumb("R.id.menu_settings", this)
        findNavController().navigateSafe(
            currentDestination = R.id.moreFragment,
            MoreFragmentDirections.actionMoreFragmentToSettingsFragment(),
        )
    }
}
