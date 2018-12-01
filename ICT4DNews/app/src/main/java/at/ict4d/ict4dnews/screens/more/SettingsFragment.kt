package at.ict4d.ict4dnews.screens.more

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import at.ict4d.ict4dnews.BuildConfig
import at.ict4d.ict4dnews.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.setting_preferences, rootKey)
        findPreference(getString(R.string.current_app_version_key)).summary = BuildConfig.VERSION_NAME
    }
}