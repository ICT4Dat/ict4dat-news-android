package at.ict4d.ict4dnews.screens.ict4d

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.FragmentTabbedIct4dBinding
import at.ict4d.ict4dnews.screens.base.BaseFragment
import at.ict4d.ict4dnews.screens.ict4d.ict4d.ICT4DFragment
import at.ict4d.ict4dnews.screens.ict4d.ict4dat.ICT4DatFragment
import at.ict4d.ict4dnews.utils.recordActionBreadcrumb
import com.google.android.material.tabs.TabLayoutMediator

class TabbedICT4DFragment : BaseFragment<FragmentTabbedIct4dBinding>(
    R.layout.fragment_tabbed_ict4d
) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = super.onCreateView(inflater, container, savedInstanceState)
        binding.viewpager.adapter = TabbedICT4DSectionsPagerAdapter(requireActivity())
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.nav_ict4dat)
                1 -> getString(R.string.nav_ict4d)
                else -> throw IllegalArgumentException("position of tab not valid")
            }
        }.attach()
    }

    inner class TabbedICT4DSectionsPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            recordActionBreadcrumb("getItem", this, mapOf("position" to "$position"))

            return when (position) {
                0 -> ICT4DatFragment.newInstance()
                1 -> ICT4DFragment.newInstance()
                else -> throw IllegalArgumentException("position of tab not valid")
            }
        }
    }
}
