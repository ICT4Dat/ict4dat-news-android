package at.ict4d.ict4dnews.screens.welcome.setup

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.FragmentWelcomeSetupBinding
import at.ict4d.ict4dnews.extensions.handleApiResponse
import at.ict4d.ict4dnews.extensions.navigateSafe
import at.ict4d.ict4dnews.screens.base.BaseFragment
import at.ict4d.ict4dnews.screens.util.showOwnershipAlertDialog
import at.ict4d.ict4dnews.utils.recordNavigationBreadcrumb
import org.koin.androidx.viewmodel.ext.android.viewModel

class WelcomeSetupFragment : BaseFragment<FragmentWelcomeSetupBinding>(
    R.layout.fragment_welcome_setup,
    hasToolbar = false
) {

    private val model by viewModel<WelcomeSetupViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showOwnershipAlertDialog(requireContext()) {
            model.blogs.observe(viewLifecycleOwner) { resource ->

                handleApiResponse(resource)

                if (!resource.data.isNullOrEmpty()) {
                    recordNavigationBreadcrumb(
                        "actionWelcomeSetupFragmentToWelcomeSummaryFragment",
                        this
                    )
                    findNavController().navigateSafe(
                        R.id.welcomeSetupFragment,
                        WelcomeSetupFragmentDirections.actionWelcomeSetupFragmentToWelcomeSummaryFragment()
                    )
                } else {
                    binding.setupProgressBar.isVisible = true
                    binding.ict4datLogo.isVisible = true
                }
            }
        }
    }
}
