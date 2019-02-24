package at.ict4d.ict4dnews.utils

import at.ict4d.ict4dnews.persistence.sharedpreferences.ISharedPrefs
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle
import javax.inject.Inject

class NewsUpdateTimeManager @Inject constructor(private val sharedPreferences: ISharedPrefs) {

    fun provideTextHelperBasedOnLastNewsUpdateDate(): LastUpdateResponseForUi {
        val lastAutomaticNewsUpdateLocalDate = sharedPreferences.lastAutomaticNewsUpdateLocalDate.get()

        return if (lastAutomaticNewsUpdateLocalDate == sharedPreferences.defaultLastAutomaticNewsUpdateTime) {
            LastUpdateResponseForUi.NewsNeverUpdated
        } else {
            LastUpdateResponseForUi.NewsUpdatedAt(lastAutomaticNewsUpdateLocalDate.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)))
        }
    }
}

sealed class LastUpdateResponseForUi {
    object NewsNeverUpdated : LastUpdateResponseForUi()
    data class NewsUpdatedAt(val date: String) : LastUpdateResponseForUi()
}