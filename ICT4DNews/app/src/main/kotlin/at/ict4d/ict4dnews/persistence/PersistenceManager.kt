package at.ict4d.ict4dnews.persistence

import at.ict4d.ict4dnews.persistence.database.dao.AuthorDao
import at.ict4d.ict4dnews.persistence.sharedpreferences.ISharedPrefs

class PersistenceManager(
    private val sharedPrefs: ISharedPrefs,
    private val authorDao: AuthorDao
) : IPersistenceManager {

    // Shared Preferences

    override fun getLastAutomaticNewsUpdateLocalDate() = sharedPrefs.lastAutomaticNewsUpdateLocalDate

    override fun isAutomaticNewsUpdateEnabled() = sharedPrefs.isAutomaticNewsUpdateEnabled

    override fun isBugTrackingEnabled() = sharedPrefs.isBugTrackingEnabled

    // Authors

    override fun getAuthorBy(authorId: String) = authorDao.getAuthorDetailsBy(authorId)
}
