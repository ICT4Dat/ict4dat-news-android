package at.ict4d.ict4dnews.persistence.sharedpreferences

import com.f2prateek.rx.preferences2.Preference
import org.threeten.bp.LocalDateTime

interface ISharedPrefs {

    val defaultLastAutomaticNewsUpdateTime: LocalDateTime

    var lastAutomaticNewsUpdateLocalDate: Preference<LocalDateTime>
}