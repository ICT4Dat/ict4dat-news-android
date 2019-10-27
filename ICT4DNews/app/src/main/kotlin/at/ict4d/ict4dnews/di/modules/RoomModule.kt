package at.ict4d.ict4dnews.di.modules

import androidx.room.Room
import androidx.room.migration.Migration
import at.ict4d.ict4dnews.persistence.IPersistenceManager
import at.ict4d.ict4dnews.persistence.PersistenceManager
import at.ict4d.ict4dnews.persistence.database.AppDatabase
import at.ict4d.ict4dnews.persistence.database.migrations.Migration1to2
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val roomModule = module {

    single<Migration> { Migration1to2() }

    single(createdAtStart = true) {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration() // delete all data if migration fails and setup the database again
            .addMigrations(get<Migration>()).build()
    }

    single(createdAtStart = true) { get<AppDatabase>().newsDao() }

    single(createdAtStart = true) { get<AppDatabase>().authorDao() }

    single(createdAtStart = true) { get<AppDatabase>().mediaDao() }

    single(createdAtStart = true) { get<AppDatabase>().blogDao() }

    single<IPersistenceManager>(createdAtStart = true) {
        PersistenceManager(
            database = get(),
            sharedPrefs = get(),
            authorDao = get(),
            newsDao = get(),
            mediaDao = get(),
            blogsDao = get()
        )
    }
}
