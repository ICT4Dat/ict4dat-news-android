package at.ict4d.ict4dnews.screens.ict4d.ict4d

import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.FragmentIct4dBinding
import at.ict4d.ict4dnews.screens.base.BaseFragment

class ICT4DFragment : BaseFragment<FragmentIct4dBinding>(
    R.layout.fragment_ict4d,
    hasToolbar = false,
) {
    companion object {
        @JvmStatic
        fun newInstance() = ICT4DFragment()
    }
}
