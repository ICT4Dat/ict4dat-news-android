package at.ict4d.ict4dnews.screens.ict4d.ict4d

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.FragmentIct4dBinding
import at.ict4d.ict4dnews.extensions.browseCustomTab
import at.ict4d.ict4dnews.screens.base.BaseFragment
import at.ict4d.ict4dnews.screens.ict4d.ICT4DViewModel
import org.jetbrains.anko.share

class ICT4DFragment : BaseFragment<ICT4DViewModel, FragmentIct4dBinding>() {

    override fun getToolbarTitleResId(): Int = R.string.nav_ict4d

    override fun getLayoutId(): Int = R.layout.fragment_ict4d

    override fun getViewModel(): Class<ICT4DViewModel> = ICT4DViewModel::class.java

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        binding.fragment = this

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_ict4d, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {

            R.id.menu_ict4d_share -> {
                activity?.share(getString(R.string.share_ict4d, getString(R.string.url_ict4d_wikipedia)))
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    fun ict4dWikipedia() {
        context?.browseCustomTab(getString(R.string.url_ict4d_wikipedia))
    }

    companion object {

        @JvmStatic
        fun newInstance() = ICT4DFragment()
    }
}
