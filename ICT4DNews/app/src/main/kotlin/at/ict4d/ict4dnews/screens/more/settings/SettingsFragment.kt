package at.ict4d.ict4dnews.screens.more.settings

import android.content.Context
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import at.ict4d.ict4dnews.BuildConfig
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.lifecycle.SentryLifecycleObserver
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import org.jetbrains.anko.intentFor

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        lifecycle.addObserver(SentryLifecycleObserver(this))
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.setting_preferences, rootKey)

        findPreference<Preference>(getString(R.string.pref_key_current_app_version))?.summary = BuildConfig.VERSION_NAME

        findPreference<Preference>(getString(R.string.pref_key_open_source_licences))?.setOnPreferenceClickListener {
            activity?.let { startActivity(it.intentFor<OssLicensesMenuActivity>()) }
            OssLicensesMenuActivity.setActivityTitle(getString(R.string.open_source_licences_title))
            true
        }
    }
}