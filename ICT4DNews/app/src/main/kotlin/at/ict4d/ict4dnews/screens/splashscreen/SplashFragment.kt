package at.ict4d.ict4dnews.screens.splashscreen

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.FragmentSplashBinding
import at.ict4d.ict4dnews.extensions.handleApiResponse
import at.ict4d.ict4dnews.screens.base.BaseFragment
import at.ict4d.ict4dnews.screens.util.showOwnershipAlertDialog
import at.ict4d.ict4dnews.utils.recordNavigationBreadcrumb

class SplashFragment : BaseFragment<SplashViewModel, FragmentSplashBinding>(
    R.layout.fragment_splash,
    SplashViewModel::class,
    hasToolbar = false
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showOwnershipAlertDialog(requireContext()) {
            model.blogs.observe(viewLifecycleOwner) { resource ->

                handleApiResponse(resource)

                if (resource.data != null && resource.data.isNotEmpty()) {
                    recordNavigationBreadcrumb("pop", this)
                    findNavController().popBackStack()
                } else {
                    binding.splashProgressBar.isVisible = true
                    binding.ict4datLogo.isVisible = true
                }
            }
        }
    }
}
