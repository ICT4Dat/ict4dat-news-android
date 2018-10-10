package at.ict4d.ict4dnews.screens.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.FragmentMoreBinding
import at.ict4d.ict4dnews.extensions.browseCustomTab
import at.ict4d.ict4dnews.extensions.contactDevelopers
import at.ict4d.ict4dnews.screens.base.BaseFragment
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.share

class MoreFragment : BaseFragment<MoreViewModel, FragmentMoreBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_more

    override fun getViewModel(): Class<MoreViewModel> = MoreViewModel::class.java

    private var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = super.onCreateView(inflater, container, savedInstanceState)
        binding.fragment = this
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        this.menu = menu
        inflater?.inflate(R.menu.menu_more_settings, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.menu_licences -> {
                activity?.let { startActivity(it.intentFor<OssLicensesMenuActivity>()) }
                OssLicensesMenuActivity.setActivityTitle(getString(R.string.open_source_licences_title))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun rateApplication() {
    }

    fun shareApplication() {
        activity?.share("", "")
    }

    fun contactUs() {
        context?.contactDevelopers(getString(R.string.url_contact_us))
    }

    fun githubProject() {
        context?.browseCustomTab(getString(R.string.url_github_project))
    }

    fun paul() {
        context?.browseCustomTab(getString(R.string.url_paul))
    }

    fun raja() {
        context?.browseCustomTab(getString(R.string.url_raja))
    }

    fun noah() {
        context?.browseCustomTab(getString(R.string.url_noah))
    }

    fun chloe() {
        context?.browseCustomTab(getString(R.string.url_chloe))
    }

    companion object {

        @JvmStatic
        fun newInstance() = MoreFragment()
    }
}
