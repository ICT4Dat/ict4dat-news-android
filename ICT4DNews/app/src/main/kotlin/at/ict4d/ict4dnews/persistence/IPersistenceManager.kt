package at.ict4d.ict4dnews.persistence

import com.f2prateek.rx.preferences2.Preference
import org.threeten.bp.LocalDate

interface IPersistenceManager {

    // Shared Preferences

    fun getLastAutomaticNewsUpdateLocalDate(): Preference<LocalDate>

    fun isAutomaticNewsUpdateEnabled(): Preference<Boolean>

    fun isBugTrackingEnabled(): Preference<Boolean>
}
