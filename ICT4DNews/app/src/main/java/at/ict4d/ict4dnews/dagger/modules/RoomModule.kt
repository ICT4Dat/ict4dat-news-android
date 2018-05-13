package at.ict4d.ict4dnews.dagger.modules

import android.arch.persistence.room.Room
import at.ict4d.ict4dnews.ICT4DNewsApplication
import at.ict4d.ict4dnews.persistence.IPersistenceManager
import at.ict4d.ict4dnews.persistence.PersistenceManager
import at.ict4d.ict4dnews.persistence.database.AppDatabase
import at.ict4d.ict4dnews.persistence.database.dao.SelfHostedWPPostDao
import at.ict4d.ict4dnews.persistence.database.dao.WordpressAuthorDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {

    @Singleton
    @Provides
    fun providesRoomDatabase(application: ICT4DNewsApplication): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, AppDatabase.DATABASE_NAME).build()
    }

    @Singleton
    @Provides
    fun providesSelfHostedWPPostDao(database: AppDatabase): SelfHostedWPPostDao {
        return database.selfHostedWPPostDao()
    }

    @Singleton
    @Provides
    fun providesWordpressAuthorDao(database: AppDatabase): WordpressAuthorDao {
        return database.wordpressAuthorDao()
    }

    @Singleton
    @Provides
    fun providesPersistentManager(database: AppDatabase): IPersistenceManager {
        return PersistenceManager()
    }
}