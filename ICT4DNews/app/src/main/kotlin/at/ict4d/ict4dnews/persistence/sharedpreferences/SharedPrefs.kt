package at.ict4d.ict4dnews.persistence.sharedpreferences

import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import at.ict4d.ict4dnews.ICT4DNewsApplication
import at.ict4d.ict4dnews.R
import com.f2prateek.rx.preferences2.Preference
import com.f2prateek.rx.preferences2.RxSharedPreferences
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class SharedPrefs(
    val application: ICT4DNewsApplication,
    private val defaultLastAutomaticNewsUpdateTime: LocalDate
) : ISharedPrefs {

    private val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(application)
    private val rxSharedPreferences: RxSharedPreferences = RxSharedPreferences.create(sharedPreferences)

    private val KEY_LAST_AUTO_NEWS_UPDATE = application.getString(R.string.pref_key_last_automatic_news_update)

    override var lastAutomaticNewsUpdateLocalDate: Preference<LocalDate>
        get() = rxSharedPreferences.getObject(
            KEY_LAST_AUTO_NEWS_UPDATE, defaultLastAutomaticNewsUpdateTime,
            object : Preference.Converter<LocalDate> {
                override fun deserialize(serialized: String): LocalDate =
                    LocalDate.parse(serialized, DateTimeFormatter.ISO_DATE)

                override fun serialize(value: LocalDate): String = value.format(DateTimeFormatter.ISO_DATE)
            })
        set(value) = sharedPreferences.edit().putString(
            KEY_LAST_AUTO_NEWS_UPDATE,
            value.get().format(DateTimeFormatter.ISO_DATE)
        ).apply()
}