package at.ict4d.ict4dnews.di.modules

import androidx.room.Room
import androidx.room.migration.Migration
import at.ict4d.ict4dnews.persistence.database.AppDatabase
import at.ict4d.ict4dnews.persistence.database.migrations.Migration1to2
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val roomModule = module {

    single<Migration> { Migration1to2() }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration() // delete all data if migration fails and setup the database again
            .addMigrations(get<Migration>()).build()
    }

    single { get<AppDatabase>().newsDao() }

    single { get<AppDatabase>().authorDao() }

    single { get<AppDatabase>().mediaDao() }

    single { get<AppDatabase>().blogDao() }
}
