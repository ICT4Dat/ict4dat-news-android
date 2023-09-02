package at.ict4d.ict4dnews.screens.ict4d.ict4dat

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.FragmentIct4datBinding
import at.ict4d.ict4dnews.extensions.browseCustomTabWithUrl
import at.ict4d.ict4dnews.screens.base.BaseFragment

class ICT4DatFragment :
    BaseFragment<FragmentIct4datBinding>(
        R.layout.fragment_ict4dat,
        hasToolbar = false
    ) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = super.onCreateView(inflater, container, savedInstanceState)

        binding.fragment = this

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    // Add menu items here
                    menuInflater.inflate(R.menu.menu_ict4dat, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem) = when (menuItem.itemId) {
                    R.id.menu_ict4dat_share -> {
                        startActivity(
                            Intent.createChooser(
                                Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(
                                        Intent.EXTRA_TEXT,
                                        getString(R.string.share_ict4dat, getString(R.string.url_ict4dat))
                                    )
                                    type = "text/plain"
                                },
                                getString(R.string.share)
                            )
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

    fun openProjects() {
        requireActivity().browseCustomTabWithUrl(getString(R.string.url_ict4dat_projects))
    }

    fun openAboutUs() {
        requireActivity().browseCustomTabWithUrl(getString(R.string.url_ict4dat_about_us))
    }

    fun openFacebook() {
        requireActivity().browseCustomTabWithUrl(getString(R.string.url_ict4dat_facebook))
    }

    fun openTwitter() {
        requireActivity().browseCustomTabWithUrl(getString(R.string.url_ict4dat_twitter))
    }

    fun openMastodon() {
        requireActivity().browseCustomTabWithUrl(getString(R.string.url_ict4dat_mastodon))
    }

    companion object {

        @JvmStatic
        fun newInstance() = ICT4DatFragment()
    }
}
