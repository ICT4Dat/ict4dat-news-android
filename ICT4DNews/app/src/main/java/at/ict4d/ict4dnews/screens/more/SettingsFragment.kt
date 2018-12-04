package at.ict4d.ict4dnews.screens.more

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import at.ict4d.ict4dnews.BuildConfig
import at.ict4d.ict4dnews.ICT4DNewsApplication
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.persistence.sharedpreferences.ISharedPrefs
import org.threeten.bp.LocalDate
import javax.inject.Inject

class SettingsFragment : PreferenceFragmentCompat() {

    @Inject
    protected lateinit var sharedPrefs: ISharedPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        ICT4DNewsApplication.instance.component.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.setting_preferences, rootKey)
        findPreference(getString(R.string.current_app_version_key)).summary = BuildConfig.VERSION_NAME

        // TODO("It will work when we change lastAutomaticUpdateLocalDate to lastAutomaticUpdateLocalDateAndTime")
        val lastAutomaticNewsUpdateLocalDate = sharedPrefs.lastAutomaticNewsUpdateLocalDate.get()
        findPreference(getString(R.string.last_news_update_key)).summary =
            if (lastAutomaticNewsUpdateLocalDate == LocalDate.now()) {
                getString(R.string.never)
            } else {
                lastAutomaticNewsUpdateLocalDate.toString()
            }
    }
}