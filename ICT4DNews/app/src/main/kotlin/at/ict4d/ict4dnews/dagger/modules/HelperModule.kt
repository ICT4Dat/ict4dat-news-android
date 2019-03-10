package at.ict4d.ict4dnews.dagger.modules

import androidx.paging.PagedList
import androidx.work.WorkManager
import at.ict4d.ict4dnews.persistence.sharedpreferences.ISharedPrefs
import at.ict4d.ict4dnews.persistence.sharedpreferences.SharedPrefs
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.reactivex.disposables.CompositeDisposable

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
        fun provideWorkManager(): WorkManager = WorkManager.getInstance()

        @Provides
        @JvmStatic
        @Reusable
        fun providePagedListConfig(): PagedList.Config =
            PagedList.Config.Builder().setEnablePlaceholders(true)
                .setPageSize(20).setPrefetchDistance(20)
                .setInitialLoadSizeHint(20).build()
    }
}