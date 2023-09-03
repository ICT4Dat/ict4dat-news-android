package at.ict4d.ict4dnews.screens.welcome.summary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.extensions.navigateSafe
import at.ict4d.ict4dnews.ui.theme.AppTheme
import at.ict4d.ict4dnews.utils.recordNavigationBreadcrumb
import org.koin.androidx.viewmodel.ext.android.viewModel

class WelcomeSummaryFragment : Fragment() {

    private val model by viewModel<WelcomeSummaryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AppTheme {
                    WelcomeSummaryScreen(
                        viewModel = model,
                        onNavigateToBlogsAndSources = {
                            recordNavigationBreadcrumb(
                                "actionWelcomeSummaryFragmentToBlogAndSourceFragment",
                                this
                            )
                            findNavController().navigateSafe(
                                R.id.welcomeSummaryFragment,
                                WelcomeSummaryFragmentDirections.actionWelcomeSummaryFragmentToBlogAndSourceFragment(true)
                            )
                        },
                        onPopBackToStart = {
                            recordNavigationBreadcrumb(
                                "popBackStack to newsListFragment",
                                this
                            )
                            findNavController().popBackStack(R.id.newsListFragment, false)
                        }
                    )
                }
            }
        }
    }
}
