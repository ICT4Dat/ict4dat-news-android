package at.ict4d.ict4dnews.persistence.sharedpreferences

import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import at.ict4d.ict4dnews.ICT4DNewsApplication
import at.ict4d.ict4dnews.R
import com.tfcporciuncula.flow.FlowSharedPreferences
import com.tfcporciuncula.flow.Serializer
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

class SharedPrefs(application: ICT4DNewsApplication) {

    private val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(application)
    private val flowSharedPreferences = FlowSharedPreferences(sharedPreferences)

    private val keyLastAutoNewsUpdate = application.getString(R.string.pref_key_last_automatic_news_update)
    private val keyIsAutoNewsUpdateEnabled = application.getString(R.string.pref_key_is_auto_sync_enabled)
    private val keyIsBugTrackingEnabled = application.getString(R.string.pref_key_is_bug_tracking_enabled)

    private val lastAutomaticNewsUpdateLocalDateSerializer = object : Serializer<LocalDate> {
        override fun deserialize(serialized: String) = LocalDate.parse(serialized, DateTimeFormatter.ISO_DATE)

        override fun serialize(value: LocalDate) = value.format(DateTimeFormatter.ISO_DATE)
    }

    val lastAutomaticNewsUpdateLocalDate = flowSharedPreferences.getObject(keyLastAutoNewsUpdate, lastAutomaticNewsUpdateLocalDateSerializer, LocalDate.now())

    val isAutomaticNewsUpdateEnabled = flowSharedPreferences.getBoolean(keyIsAutoNewsUpdateEnabled, true)

    val isBugTrackingEnabled = flowSharedPreferences.getBoolean(keyIsBugTrackingEnabled, true)
}
