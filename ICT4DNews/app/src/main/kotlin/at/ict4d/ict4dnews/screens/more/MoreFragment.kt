package at.ict4d.ict4dnews.screens.more

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
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
import at.ict4d.ict4dnews.extensions.browseCustomTab
import at.ict4d.ict4dnews.extensions.navigateSafe
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
        binding.headline.text = getString(R.string.headline_more, String(Character.toChars(0x2764)))
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
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
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    fun rateApplication() {

        val uri = Uri.parse("market://details?id=${context?.packageName}")
        recordActionBreadcrumb("rateApplication", this, mapOf("uri" to uri.toString()))

        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)

        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=${context?.packageName}")
                )
            )
        }
    }

    fun shareApplication() {
        recordActionBreadcrumb("shareApplication", this)

        startActivity(
            Intent.createChooser(
                Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(
                        Intent.EXTRA_TEXT,
                        getString(
                            R.string.share_app_text,
                            "http://play.google.com/store/apps/details?id=${context?.packageName}"
                        )
                    )
                    type = "text/plain"
                },
                getString(R.string.share)
            )
        )
    }

    fun openUrlInCustomTab(@StringRes stringRes: Int) {
        context?.browseCustomTab(getString(stringRes))
    }

    fun contactUs() {
        recordActionBreadcrumb("contactUs", this)

        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:") // Only email apps handle this.
            putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.contact_email)))
            putExtra(Intent.EXTRA_SUBJECT, arrayOf(getString(R.string.contact_mail_subject)))
            putExtra(Intent.EXTRA_TEXT, arrayOf(getString(R.string.contact_mail_text)))
        }
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        }
    }
}
