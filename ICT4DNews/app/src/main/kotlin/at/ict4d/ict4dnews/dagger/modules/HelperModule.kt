package at.ict4d.ict4dnews.dagger.modules

import androidx.work.WorkManager
import at.ict4d.ict4dnews.ICT4DNewsApplication
import at.ict4d.ict4dnews.persistence.sharedpreferences.ISharedPrefs
import at.ict4d.ict4dnews.persistence.sharedpreferences.SharedPrefs
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Singleton

@Module
class HelperModule {

    @Provides
    fun provideCompositeDisposable(): CompositeDisposable = CompositeDisposable()

    @Provides
    @Reusable
    fun providesSharedPreferences(application: ICT4DNewsApplication): ISharedPrefs = SharedPrefs(application)

    @Provides
    @Singleton
    fun provideWorkManager(): WorkManager = WorkManager.getInstance()
}