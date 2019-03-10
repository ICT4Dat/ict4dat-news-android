package at.ict4d.ict4dnews.dagger.modules

import androidx.room.Room
import at.ict4d.ict4dnews.ICT4DNewsApplication
import at.ict4d.ict4dnews.persistence.IPersistenceManager
import at.ict4d.ict4dnews.persistence.PersistenceManager
import at.ict4d.ict4dnews.persistence.database.AppDatabase
import at.ict4d.ict4dnews.persistence.database.dao.AuthorDao
import at.ict4d.ict4dnews.persistence.database.dao.BlogDao
import at.ict4d.ict4dnews.persistence.database.dao.MediaDao
import at.ict4d.ict4dnews.persistence.database.dao.NewsDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Module
abstract class RoomModule {

    @Binds
    @Reusable
    abstract fun bindIPersistenceManager(persistenceManager: PersistenceManager): IPersistenceManager

    @Module
    companion object {

        @Provides
        @JvmStatic
        @Reusable
        fun providesRoomDatabase(application: ICT4DNewsApplication): AppDatabase =
            Room.databaseBuilder(application, AppDatabase::class.java, AppDatabase.DATABASE_NAME).build()

        @Provides
        @JvmStatic
        @Reusable
        fun providesNewsDao(database: AppDatabase): NewsDao = database.newsDao()

        @Provides
        @JvmStatic
        @Reusable
        fun providesAuthorDao(database: AppDatabase): AuthorDao = database.authorDao()

        @Provides
        @JvmStatic
        @Reusable
        fun providesMediaDao(database: AppDatabase): MediaDao = database.mediaDao()

        @Provides
        @JvmStatic
        @Reusable
        fun providesBlogDao(database: AppDatabase): BlogDao = database.blogDao()
    }
}