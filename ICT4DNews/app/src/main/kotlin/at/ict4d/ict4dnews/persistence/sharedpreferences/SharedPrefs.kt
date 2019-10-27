package at.ict4d.ict4dnews.persistence.sharedpreferences

import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import at.ict4d.ict4dnews.ICT4DNewsApplication
import at.ict4d.ict4dnews.R
import com.f2prateek.rx.preferences2.Preference
import com.f2prateek.rx.preferences2.RxSharedPreferences
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class SharedPrefs(application: ICT4DNewsApplication) : ISharedPrefs {

    private val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(application)
    private val rxSharedPreferences: RxSharedPreferences = RxSharedPreferences.create(sharedPreferences)

    private val keyLastAutoNewsUpdate = application.getString(R.string.pref_key_last_automatic_news_update)
    private val keyIsAutoNewsUpdateEnabled = application.getString(R.string.pref_key_is_auto_sync_enabled)
    private val keyIsBugTrackingEnabled = application.getString(R.string.pref_key_is_bug_tracking_enabled)

    override var lastAutomaticNewsUpdateLocalDate: Preference<LocalDate>
        get() = rxSharedPreferences.getObject(
            keyLastAutoNewsUpdate,
            LocalDate.now(),
            object : Preference.Converter<LocalDate> {
                override fun deserialize(serialized: String): LocalDate =
                    LocalDate.parse(serialized, DateTimeFormatter.ISO_DATE)

                override fun serialize(value: LocalDate): String = value.format(DateTimeFormatter.ISO_DATE)
            })
        set(value) = sharedPreferences.edit().putString(
            keyLastAutoNewsUpdate,
            value.get().format(DateTimeFormatter.ISO_DATE)
        ).apply()

    override var isAutomaticNewsUpdateEnabled: Preference<Boolean>
        get() = rxSharedPreferences.getBoolean(keyIsAutoNewsUpdateEnabled, true)
        set(value) = sharedPreferences.edit().putBoolean(keyIsAutoNewsUpdateEnabled, value.get()).apply()

    override var isBugTrackingEnabled: Preference<Boolean>
        get() = rxSharedPreferences.getBoolean(keyIsBugTrackingEnabled, true)
        set(value) = sharedPreferences.edit().putBoolean(keyIsBugTrackingEnabled, value.get()).apply()
}
