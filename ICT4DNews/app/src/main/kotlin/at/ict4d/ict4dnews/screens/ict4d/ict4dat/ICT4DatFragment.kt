package at.ict4d.ict4dnews.screens.ict4d.ict4dat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.FragmentIct4datBinding
import at.ict4d.ict4dnews.extensions.browseCustomTabWithUrl
import at.ict4d.ict4dnews.screens.base.BaseFragment

class ICT4DatFragment :
    BaseFragment<FragmentIct4datBinding>(
        R.layout.fragment_ict4dat,
        hasToolbar = false,
    ) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val rootView = super.onCreateView(inflater, container, savedInstanceState)

        binding.fragment = this

        return rootView
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

    fun openX() {
        requireActivity().browseCustomTabWithUrl(getString(R.string.url_ict4dat_x))
    }

    fun openMastodon() {
        requireActivity().browseCustomTabWithUrl(getString(R.string.url_ict4dat_mastodon))
    }

    companion object {
        @JvmStatic
        fun newInstance() = ICT4DatFragment()
    }
}
