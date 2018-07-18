package at.ict4d.ict4dnews.screens.ict4dat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.FragmentIct4datBinding
import at.ict4d.ict4dnews.screens.base.BaseNavigationFragment

class ICT4DatFragment : BaseNavigationFragment<ICT4DatViewModel, FragmentIct4datBinding>() {

    override fun getMenuItemId(): Int = R.id.navigation_ict4dat

    override fun getToolbarTitleResId(): Int = R.string.nav_ict4dat

    override fun getLayoutId(): Int = R.layout.fragment_ict4dat

    override fun getViewModel(): Class<ICT4DatViewModel> = ICT4DatViewModel::class.java

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    companion object {

        @JvmStatic
        fun newInstance() = ICT4DatFragment()
    }
}
