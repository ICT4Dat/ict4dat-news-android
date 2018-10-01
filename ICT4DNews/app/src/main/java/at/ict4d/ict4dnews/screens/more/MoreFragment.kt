package at.ict4d.ict4dnews.screens.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.FragmentMoreBinding
import at.ict4d.ict4dnews.screens.base.BaseFragment

class MoreFragment : BaseFragment<MoreViewModel, FragmentMoreBinding>() {

    override fun getToolbarTitleResId(): Int = R.string.nav_more

    override fun getLayoutId(): Int = R.layout.fragment_more

    override fun getViewModel(): Class<MoreViewModel> = MoreViewModel::class.java

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    companion object {

        @JvmStatic
        fun newInstance() = MoreFragment()
    }
}
