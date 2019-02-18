package at.ict4d.ict4dnews.dagger.modules

import androidx.work.WorkManager
import at.ict4d.ict4dnews.persistence.sharedpreferences.ISharedPrefs
import at.ict4d.ict4dnews.persistence.sharedpreferences.SharedPrefs
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Singleton

@Module
abstract class HelperModule {

    @Binds
    abstract fun providesSharedPreferences(sharedPrefs: SharedPrefs): ISharedPrefs

    @Module
    companion object {

        @Provides
        @JvmStatic
        fun provideCompositeDisposable(): CompositeDisposable = CompositeDisposable()

        @Provides
        @JvmStatic
        @Singleton
        fun provideWorkManager(): WorkManager = WorkManager.getInstance()
    }
}