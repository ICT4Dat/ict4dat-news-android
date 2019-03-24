package at.ict4d.ict4dnews.persistence.sharedpreferences

import com.f2prateek.rx.preferences2.Preference
import org.threeten.bp.LocalDate

interface ISharedPrefs {

    var lastAutomaticNewsUpdateLocalDate: Preference<LocalDate>

    var isAutomaticNewsUpdateEnabled: Preference<Boolean>
}