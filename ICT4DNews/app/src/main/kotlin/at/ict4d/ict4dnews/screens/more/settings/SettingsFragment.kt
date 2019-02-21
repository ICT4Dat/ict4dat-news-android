package at.ict4d.ict4dnews.screens.more.settings

import android.content.Context
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import at.ict4d.ict4dnews.BuildConfig
import at.ict4d.ict4dnews.ICT4DNewsApplication
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.utils.LastUpdateResponseForUi
import at.ict4d.ict4dnews.utils.NewsUpdateTimeManager
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import org.jetbrains.anko.intentFor
import javax.inject.Inject

class SettingsFragment : PreferenceFragmentCompat() {

    @Inject
    lateinit var updateTimeManager: NewsUpdateTimeManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ICT4DNewsApplication.instance.component.inject(this)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.setting_preferences, rootKey)
        findPreference(getString(R.string.current_app_version_key)).summary = BuildConfig.VERSION_NAME

        val result = updateTimeManager.provideTextHelperBasedOnLastNewsUpdateDate()
        findPreference(getString(R.string.last_news_update_key)).summary = when (result) {
            is LastUpdateResponseForUi.NewsNeverUpdated -> getString(R.string.never)
            is LastUpdateResponseForUi.NewsUpdatedAt -> result.date
        }

        findPreference(getString(R.string.open_source_licences_key)).setOnPreferenceClickListener {
            activity?.let { startActivity(it.intentFor<OssLicensesMenuActivity>()) }
            OssLicensesMenuActivity.setActivityTitle(getString(R.string.open_source_licences_title))
            true
        }
    }
}