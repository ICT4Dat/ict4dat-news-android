package at.ict4d.ict4dnews.dagger.modules

import android.arch.persistence.room.Room
import at.ict4d.ict4dnews.ICT4DNewsApplication
import at.ict4d.ict4dnews.persistence.IPersistenceManager
import at.ict4d.ict4dnews.persistence.PersistenceManager
import at.ict4d.ict4dnews.persistence.database.AppDatabase
import at.ict4d.ict4dnews.persistence.database.dao.NewsDao
import at.ict4d.ict4dnews.persistence.database.dao.AuthorDao
import at.ict4d.ict4dnews.persistence.database.dao.MediaDao
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
    fun providesSelfHostedWPPostDao(database: AppDatabase): NewsDao {
        return database.newsDao()
    }

    @Singleton
    @Provides
    fun providesWordpressAuthorDao(database: AppDatabase): AuthorDao {
        return database.authorDao()
    }

    @Singleton
    @Provides
    fun providesWordpressMediaDao(database: AppDatabase): MediaDao {
        return database.mediaDao()
    }

    @Singleton
    @Provides
    fun providesPersistentManager(): IPersistenceManager {
        return PersistenceManager()
    }
}