package at.ict4d.ict4dnews.dagger.modules

import at.ict4d.ict4dnews.ICT4DNewsApplication
import at.ict4d.ict4dnews.persistence.sharedpreferences.ISharedPrefs
import at.ict4d.ict4dnews.persistence.sharedpreferences.SharedPrefs
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.reactivex.disposables.CompositeDisposable
import org.threeten.bp.LocalDate
import javax.inject.Singleton

@Module
class HelperModule {

    @Provides
    fun provideCompositeDisposable(): CompositeDisposable = CompositeDisposable()

    @Singleton
    @Provides
    fun provideDefaultLastAutomaticNewsUpdateDate() = LocalDate.now().minusYears(20)

    @Provides
    @Reusable
    fun providesSharedPreferences(application: ICT4DNewsApplication, lastUpdateDate: LocalDate): ISharedPrefs =
        SharedPrefs(application, lastUpdateDate)
}