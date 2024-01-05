package at.ict4d.ict4dnews.screens.more.settings

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import at.ict4d.ict4dnews.BuildConfig
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.extensions.openNotificationSettings
import at.ict4d.ict4dnews.lifecycle.SentryLifecycleObserver
import at.ict4d.ict4dnews.persistence.sharedpreferences.SharedPrefs
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject

class SettingsFragment : PreferenceFragmentCompat() {
    private val sharedPrefs by inject<SharedPrefs>()

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { isGranted: Boolean ->
            if (isGranted) {
                sharedPrefs.isAutomaticNewsUpdateEnabled.set(true)
            } else {
                val pref = preferenceManager.findPreference<SwitchPreference>(getString(R.string.pref_key_is_auto_sync_enabled_switch_preference))
                pref?.isChecked = false
            }
        }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        lifecycle.addObserver(SentryLifecycleObserver(this))
    }

    override fun onCreatePreferences(
        savedInstanceState: Bundle?,
        rootKey: String?,
    ) {
        setPreferencesFromResource(R.xml.setting_preferences, rootKey)

        findPreference<Preference>(getString(R.string.pref_key_current_app_version))?.summary = BuildConfig.VERSION_NAME

        findPreference<Preference>(getString(R.string.pref_key_open_source_licences))?.setOnPreferenceClickListener {
            startActivity(Intent(requireActivity(), OssLicensesMenuActivity::class.java))
            OssLicensesMenuActivity.setActivityTitle(getString(R.string.open_source_licences_title))
            true
        }

        val autoUpdatePref = findPreference<SwitchPreference>(getString(R.string.pref_key_is_auto_sync_enabled_switch_preference))
        autoUpdatePref?.isChecked = sharedPrefs.isAutomaticNewsUpdateEnabled.get()
        autoUpdatePref?.setOnPreferenceClickListener {
            val oldValue = sharedPrefs.isAutomaticNewsUpdateEnabled.get()

            if (oldValue) {
                sharedPrefs.isAutomaticNewsUpdateEnabled.set(false)
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    when {
                        ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.POST_NOTIFICATIONS,
                        ) == PackageManager.PERMISSION_GRANTED -> {
                            sharedPrefs.isAutomaticNewsUpdateEnabled.set(true)
                        }

                        shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                            val pref = preferenceManager.findPreference<SwitchPreference>(getString(R.string.pref_key_is_auto_sync_enabled_switch_preference))
                            pref?.isChecked = false
                            view?.let {
                                val snackbar = Snackbar.make(it, getString(R.string.runtime_permission_notification_not_granted), Snackbar.LENGTH_LONG)
                                snackbar.setAction(getString(R.string.runtime_permission_notification_open_settings)) {
                                    requireActivity().openNotificationSettings()
                                }
                                snackbar.show()
                            }
                        }

                        else -> {
                            // You can directly ask for the permission.
                            // The registered ActivityResultCallback gets the result of this request.

                            requestPermissionLauncher.launch(
                                Manifest.permission.POST_NOTIFICATIONS,
                            )
                        }
                    }
                } else {
                    sharedPrefs.isAutomaticNewsUpdateEnabled.set(!sharedPrefs.isAutomaticNewsUpdateEnabled.get())
                }
            }
            true
        }
    }
}
