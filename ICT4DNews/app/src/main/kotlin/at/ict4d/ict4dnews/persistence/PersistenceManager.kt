package at.ict4d.ict4dnews.persistence

import at.ict4d.ict4dnews.persistence.sharedpreferences.ISharedPrefs

class PersistenceManager(
    private val sharedPrefs: ISharedPrefs
) : IPersistenceManager {

    // Shared Preferences

    override fun getLastAutomaticNewsUpdateLocalDate() = sharedPrefs.lastAutomaticNewsUpdateLocalDate

    override fun isAutomaticNewsUpdateEnabled() = sharedPrefs.isAutomaticNewsUpdateEnabled

    override fun isBugTrackingEnabled() = sharedPrefs.isBugTrackingEnabled
}
