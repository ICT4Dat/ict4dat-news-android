package at.ict4d.ict4dnews.screens.more

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.annotation.StringRes
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.FragmentMoreBinding
import at.ict4d.ict4dnews.extensions.browseCustomTab
import at.ict4d.ict4dnews.screens.base.BaseFragment
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import org.jetbrains.anko.email
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.share

class MoreFragment : BaseFragment<MoreViewModel, FragmentMoreBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_more

    override fun getViewModel(): Class<MoreViewModel> = MoreViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = super.onCreateView(inflater, container, savedInstanceState)
        binding.fragment = this
        binding.headline.text = getString(R.string.headline_more, String(Character.toChars(0x2764)))
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
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
        val uri = Uri.parse("market://details?id=${context?.packageName}")

        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        } else {
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        }

        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=${context?.packageName}")
                )
            )
        }
    }

    fun shareApplication() {
        activity?.share(getString(R.string.share_app_text, "http://play.google.com/store/apps/details?id=${context?.packageName}"), getString(R.string.app_name))
    }

    fun openUrlInCustomTab(@StringRes stringRes: Int) {
        context?.browseCustomTab(getString(stringRes))
    }

    fun contactUs() {
        context?.email(
            getString(R.string.contact_email),
            getString(R.string.contact_mail_subject),
            getString(R.string.contact_mail_text)
        )
    }
}
