package at.ict4d.ict4dnews.screens.ict4d.ict4dat

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.FragmentIct4datBinding
import at.ict4d.ict4dnews.extensions.browseCustomTab
import at.ict4d.ict4dnews.screens.base.BaseFragment
import at.ict4d.ict4dnews.screens.ict4d.ICT4DViewModel
import org.jetbrains.anko.share

class ICT4DatFragment :
    BaseFragment<ICT4DViewModel, FragmentIct4datBinding>(
        R.layout.fragment_ict4dat,
        ICT4DViewModel::class,
        hasToolbar = false
    ) {

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = super.onCreateView(inflater, container, savedInstanceState)

        binding.fragment = this

        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_ict4dat, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {

            R.id.menu_ict4dat_share -> {
                activity?.share(getString(R.string.share_ict4dat, getString(R.string.url_ict4dat)))
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    fun openProjects() {
        context?.browseCustomTab(getString(R.string.url_ict4dat_projects))
    }

    fun openAboutUs() {
        context?.browseCustomTab(getString(R.string.url_ict4dat_about_us))
    }

    fun openFacebook() {
        context?.browseCustomTab(getString(R.string.url_ict4dat_facebook))
    }

    fun openTwitter() {
        context?.browseCustomTab(getString(R.string.url_ict4dat_twitter))
    }

    fun openMastodon() {
        context?.browseCustomTab(getString(R.string.url_ict4dat_mastodon))
    }

    companion object {

        @JvmStatic
        fun newInstance() = ICT4DatFragment()
    }
}
