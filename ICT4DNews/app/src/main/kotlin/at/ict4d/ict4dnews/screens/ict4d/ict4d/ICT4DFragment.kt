package at.ict4d.ict4dnews.screens.ict4d.ict4d

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.FragmentIct4dBinding
import at.ict4d.ict4dnews.extensions.browseCustomTabWithUrl
import at.ict4d.ict4dnews.screens.base.BaseFragment

class ICT4DFragment : BaseFragment<FragmentIct4dBinding>(
    R.layout.fragment_ict4d,
    hasToolbar = false
) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        binding.fragment = this

        return view
    }

    fun ict4dWikipedia() {
        requireActivity().browseCustomTabWithUrl(getString(R.string.url_ict4d_wikipedia))
    }

    companion object {

        @JvmStatic
        fun newInstance() = ICT4DFragment()
    }
}
