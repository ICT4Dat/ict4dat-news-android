package at.ict4d.ict4dnews.persistence.sharedpreferences

import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import at.ict4d.ict4dnews.ICT4DNewsApplication
import at.ict4d.ict4dnews.R
import com.f2prateek.rx.preferences2.Preference
import com.f2prateek.rx.preferences2.RxSharedPreferences
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class SharedPrefs(val application: ICT4DNewsApplication) : ISharedPrefs {

    private val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(application)
    private val rxSharedPreferences: RxSharedPreferences = RxSharedPreferences.create(sharedPreferences)

    private val KEY_LAST_AUTO_NEWS_UPDATE = application.getString(R.string.pref_key_last_automatic_news_update)
    private val KEY_NEWS_WORK_ID = application.getString(R.string.pref_key_news_work_id)

    override var lastAutomaticNewsUpdateLocalDate: Preference<LocalDate>
        get() = rxSharedPreferences.getObject(
            KEY_LAST_AUTO_NEWS_UPDATE,
            LocalDate.now(),
            object : Preference.Converter<LocalDate> {
                override fun deserialize(serialized: String): LocalDate =
                    LocalDate.parse(serialized, DateTimeFormatter.ISO_DATE)

                override fun serialize(value: LocalDate): String = value.format(DateTimeFormatter.ISO_DATE)
            })
        set(value) = sharedPreferences.edit().putString(
            KEY_LAST_AUTO_NEWS_UPDATE,
            value.get().format(DateTimeFormatter.ISO_DATE)
        ).apply()

    override var newsServiceId: Preference<String>
        get() = rxSharedPreferences.getObject(
            KEY_NEWS_WORK_ID,
            "",
            object : Preference.Converter<String> {
                override fun deserialize(serialized: String): String = serialized

                override fun serialize(value: String): String = value
            })
        set(value) = sharedPreferences.edit().putString(KEY_NEWS_WORK_ID, value.get()).apply()
}