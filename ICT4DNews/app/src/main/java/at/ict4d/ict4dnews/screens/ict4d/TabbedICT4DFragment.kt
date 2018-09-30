package at.ict4d.ict4dnews.screens.ict4d

import android.os.Bundle
import android.support.annotation.IntRange
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.FragmentTabbedIct4dBinding
import at.ict4d.ict4dnews.screens.base.BaseNavigationFragment
import at.ict4d.ict4dnews.screens.ict4d.ict4d.ICT4DFragment
import at.ict4d.ict4dnews.screens.ict4d.ict4dat.ICT4DatFragment

class TabbedICT4DFragment : BaseNavigationFragment<ICT4DViewModel, FragmentTabbedIct4dBinding>() {

    override fun getMenuItemId(): Int = R.id.navigation_ict4d

    override fun getToolbarTitleResId(): Int = R.string.nav_ict4d

    override fun getLayoutId(): Int = R.layout.fragment_tabbed_ict4d

    override fun getViewModel(): Class<ICT4DViewModel> = ICT4DViewModel::class.java

    private var sectionsPagerAdapter: TabbedICT4DSectionsPagerAdapter? = null

    companion object {

        @JvmStatic
        fun newInstance() = TabbedICT4DFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = super.onCreateView(inflater, container, savedInstanceState)

        sectionsPagerAdapter = TabbedICT4DSectionsPagerAdapter(childFragmentManager)

        binding.viewpager.adapter = sectionsPagerAdapter
        
        binding.viewpager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout))
        binding.tabLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(binding.viewpager))

        return rootView
    }

    inner class TabbedICT4DSectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(@IntRange(from = 0, to = 1) position: Int): Fragment {
            return when (position) {
                0 -> ICT4DatFragment.newInstance()
                1 -> ICT4DFragment.newInstance()
                else -> throw IllegalArgumentException("position tab not valid")
            }
        }

        override fun getCount(): Int {
            return 2
        }
    }
}
