package at.ict4d.ict4dnews.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import at.ict4d.ict4dnews.persistence.database.AppDatabase
import at.ict4d.ict4dnews.screens.MainNavigationActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule
import org.koin.core.context.stopKoin
import org.robolectric.Robolectric

abstract class BaseDaoTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule() // Triggers LiveData

    protected lateinit var appDatabase: AppDatabase

    @Before
    open fun createDatabase() {
        val activity = Robolectric.buildActivity(MainNavigationActivity::class.java).get()
        appDatabase = Room.inMemoryDatabaseBuilder(activity, AppDatabase::class.java).allowMainThreadQueries().build()
    }

    @After
    fun closeDatabase() {
        appDatabase.clearAllTables()
        appDatabase.close()
        stopKoin()
    }
}
