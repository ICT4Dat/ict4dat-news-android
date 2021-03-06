package at.ict4d.ict4dnews.screens.ict4d

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IntRange
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.FragmentTabbedIct4dBinding
import at.ict4d.ict4dnews.screens.base.BaseFragment
import at.ict4d.ict4dnews.screens.ict4d.ict4d.ICT4DFragment
import at.ict4d.ict4dnews.screens.ict4d.ict4dat.ICT4DatFragment
import at.ict4d.ict4dnews.utils.recordActionBreadcrumb
import com.google.android.material.tabs.TabLayout

class TabbedICT4DFragment : BaseFragment<ICT4DViewModel, FragmentTabbedIct4dBinding>(
    R.layout.fragment_tabbed_ict4d,
    ICT4DViewModel::class
) {

    private var sectionsPagerAdapter: TabbedICT4DSectionsPagerAdapter? = null

    companion object {

        @JvmStatic
        fun newInstance() = TabbedICT4DFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = super.onCreateView(inflater, container, savedInstanceState)

        sectionsPagerAdapter = TabbedICT4DSectionsPagerAdapter(childFragmentManager)

        binding.viewpager.adapter = sectionsPagerAdapter

        binding.viewpager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout))
        binding.tabLayout.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(binding.viewpager))

        return rootView
    }

    inner class TabbedICT4DSectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(@IntRange(from = 0, to = 1) position: Int): Fragment {
            recordActionBreadcrumb("getItem", this, mapOf("position" to "$position"))

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
