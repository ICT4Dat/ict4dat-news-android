package at.ict4d.ict4dnews.di.modules

import androidx.paging.PagedList
import androidx.work.WorkManager
import at.ict4d.ict4dnews.ICT4DNewsApplication
import at.ict4d.ict4dnews.background.UpdateNewsServiceHandler
import at.ict4d.ict4dnews.persistence.sharedpreferences.ISharedPrefs
import at.ict4d.ict4dnews.persistence.sharedpreferences.SharedPrefs
import at.ict4d.ict4dnews.utils.RxEventBus
import io.reactivex.disposables.CompositeDisposable
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val helperModule = module {

    factory { CompositeDisposable() }

    single { WorkManager.getInstance(get<ICT4DNewsApplication>()) }

    factory {
        PagedList.Config.Builder().setEnablePlaceholders(true)
            .setPageSize(20).setPrefetchDistance(20)
            .setInitialLoadSizeHint(20).build()
    }

    single { RxEventBus() }

    single<ISharedPrefs> { SharedPrefs(androidApplication() as ICT4DNewsApplication) }

    factory { UpdateNewsServiceHandler(workManager = get()) }
}
