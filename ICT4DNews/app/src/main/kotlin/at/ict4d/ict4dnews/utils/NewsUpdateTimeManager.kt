package at.ict4d.ict4dnews.utils

import at.ict4d.ict4dnews.persistence.sharedpreferences.ISharedPrefs
import org.threeten.bp.LocalDate
import javax.inject.Inject

class NewsUpdateTimeManager @Inject constructor(
    private val sharedPreferencesContract: ISharedPrefs,
    private val defaultLastAutomaticNewsUpdateTime: LocalDate
) {

    fun provideTextHelperBasedOnLastNewsUpdateDate(): LastUpdateResponseForUi {
        val lastAutomaticNewsUpdateLocalDate = sharedPreferencesContract.lastAutomaticNewsUpdateLocalDate.get()

        return if (lastAutomaticNewsUpdateLocalDate == defaultLastAutomaticNewsUpdateTime) {
            LastUpdateResponseForUi.NewsNeverUpdated
        } else {
            LastUpdateResponseForUi.NewsUpdatedAt(lastAutomaticNewsUpdateLocalDate.toString())
        }
    }
}

sealed class LastUpdateResponseForUi {
    object NewsNeverUpdated : LastUpdateResponseForUi()
    data class NewsUpdatedAt(val date: String) : LastUpdateResponseForUi()
}