package at.ict4d.ict4dnews.screens.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.compose.ui.platform.ComposeView
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

class MoreFragment2 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                AppTheme {
                    MoreScreen(
                        onRateApp = { rateApp() },
                        onShareApp = { shareApp() },
                        onContactUs = { contactUs() },
                        onMenuSettingsSelected = { onMenuSettingsSelected() },
                        onOpenGithubProject = { openUrlInCustomTab(R.string.url_github_project) }
                    )
                }
            }
        }
    }

    private fun rateApp() {
        requireActivity().openGooglePlayApp()
    }

    private fun shareApp() {
        recordActionBreadcrumb("shareApplication", this)
        requireActivity().share(
            text = getString(
                R.string.share_app_text,
                "http://play.google.com/store/apps/details?id=${context?.packageName}"
            )
        )
    }

    private fun contactUs() {
        recordActionBreadcrumb("contactUs", this)

        requireActivity().email(
            email = getString(R.string.contact_email),
            subject = getString(R.string.contact_mail_subject),
            text = getString(R.string.contact_mail_text)
        )
    }

    private fun openUrlInCustomTab(@StringRes stringRes: Int) {
        requireActivity().browseCustomTabWithUrl(getString(stringRes))
    }

    private fun onMenuSettingsSelected() {
        recordNavigationBreadcrumb("R.id.menu_settings", this)
        findNavController().navigateSafe(
            currentDestination = R.id.moreFragment,
            destinationId = R.id.settingsFragment
        )
    }
}
