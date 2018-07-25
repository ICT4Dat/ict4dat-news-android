package at.ict4d.ict4dnews.screens.ict4d


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.FragmentIct4dBinding
import at.ict4d.ict4dnews.screens.base.BaseNavigationFragment
import org.jetbrains.anko.browse

class ICT4DFragment : BaseNavigationFragment<ICT4DViewModel, FragmentIct4dBinding>() {

    override fun getMenuItemId(): Int = R.id.navigation_ict4d

    override fun getToolbarTitleResId(): Int = R.string.nav_ict4d

    override fun getLayoutId(): Int = R.layout.fragment_ict4d

    override fun getViewModel(): Class<ICT4DViewModel> = ICT4DViewModel::class.java

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        binding.fragment = this

        return view
    }

    fun ict4dWikipedia() {
        //TODO("open ict4d wiki link in browser")
        activity?.browse("http://google.com", true)
    }

    fun otherIct4dSource() {
        //TODO("open other ict4d source link in browser")
        activity?.browse("http://google.com", true)
    }

    companion object {

        @JvmStatic
        fun newInstance() = ICT4DFragment()
    }
}
