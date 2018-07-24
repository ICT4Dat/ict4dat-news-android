package at.ict4d.ict4dnews.dagger.modules

import android.arch.persistence.room.Room
import at.ict4d.ict4dnews.ICT4DNewsApplication
import at.ict4d.ict4dnews.persistence.IPersistenceManager
import at.ict4d.ict4dnews.persistence.PersistenceManager
import at.ict4d.ict4dnews.persistence.database.AppDatabase
import at.ict4d.ict4dnews.persistence.database.dao.AuthorDao
import at.ict4d.ict4dnews.persistence.database.dao.MediaDao
import at.ict4d.ict4dnews.persistence.database.dao.NewsDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {

    @Singleton
    @Provides
    fun providesRoomDatabase(application: ICT4DNewsApplication): AppDatabase = Room.databaseBuilder(application, AppDatabase::class.java, AppDatabase.DATABASE_NAME).build()


    @Singleton
    @Provides
    fun providesNewsDao(database: AppDatabase): NewsDao = database.newsDao()


    @Singleton
    @Provides
    fun providesAuthorDao(database: AppDatabase): AuthorDao = database.authorDao()


    @Singleton
    @Provides
    fun providesMediaDao(database: AppDatabase): MediaDao = database.mediaDao()


    @Singleton
    @Provides
    fun providesPersistentManager(
            authorDao: AuthorDao,
            newsDao: NewsDao,
            mediaDao: MediaDao
    ): IPersistenceManager = PersistenceManager(authorDao, newsDao, mediaDao)
}