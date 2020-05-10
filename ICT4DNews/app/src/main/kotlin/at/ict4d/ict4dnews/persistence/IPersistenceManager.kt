package at.ict4d.ict4dnews.persistence

import androidx.lifecycle.LiveData
import at.ict4d.ict4dnews.models.Author
import com.f2prateek.rx.preferences2.Preference
import org.threeten.bp.LocalDate

interface IPersistenceManager {

    // Shared Preferences

    fun getLastAutomaticNewsUpdateLocalDate(): Preference<LocalDate>

    fun isAutomaticNewsUpdateEnabled(): Preference<Boolean>

    fun isBugTrackingEnabled(): Preference<Boolean>

    // Authors

    fun getAuthorBy(authorId: String): LiveData<Author?>
}
