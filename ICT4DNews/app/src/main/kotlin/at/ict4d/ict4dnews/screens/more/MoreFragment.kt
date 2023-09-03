package at.ict4d.ict4dnews.screens.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.FragmentMoreBinding
import at.ict4d.ict4dnews.extensions.browseCustomTabWithUrl
import at.ict4d.ict4dnews.extensions.email
import at.ict4d.ict4dnews.extensions.navigateSafe
import at.ict4d.ict4dnews.extensions.openGooglePlayApp
import at.ict4d.ict4dnews.extensions.share
import at.ict4d.ict4dnews.screens.base.BaseFragment
import at.ict4d.ict4dnews.utils.recordActionBreadcrumb
import at.ict4d.ict4dnews.utils.recordNavigationBreadcrumb

class MoreFragment :
    BaseFragment<FragmentMoreBinding>(R.layout.fragment_more) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = super.onCreateView(inflater, container, savedInstanceState)
        binding.fragment = this
        binding.include.toolbar.title = getString(R.string.headline_more, String(Character.toChars(0x2764)))
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    // Add menu items here
                    menuInflater.inflate(R.menu.menu_more_settings, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem) = when (menuItem.itemId) {
                    R.id.menu_settings -> {
                        recordNavigationBreadcrumb("R.id.menu_settings", this)
                        findNavController().navigateSafe(
                            R.id.moreFragment,
                            MoreFragmentDirections.actionActionMoreToSettingsFragment()
                        )
                        true
                    }

                    else -> false
                }
            },
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )
    }

    fun rateApplication() {
        requireActivity().openGooglePlayApp()
    }

    fun shareApplication() {
        recordActionBreadcrumb("shareApplication", this)
        requireActivity().share(
            text = getString(
                R.string.share_app_text,
                "http://play.google.com/store/apps/details?id=${context?.packageName}"
            )
        )
    }

    fun openUrlInCustomTab(@StringRes stringRes: Int) {
        requireActivity().browseCustomTabWithUrl(getString(stringRes))
    }

    fun contactUs() {
        recordActionBreadcrumb("contactUs", this)

        requireActivity().email(
            email = getString(R.string.contact_email),
            subject = getString(R.string.contact_mail_subject),
            text = getString(R.string.contact_mail_text)
        )
    }
}
