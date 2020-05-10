package at.ict4d.ict4dnews.di.modules

import androidx.work.WorkManager
import at.ict4d.ict4dnews.ICT4DNewsApplication
import at.ict4d.ict4dnews.background.UpdateNewsServiceHandler
import at.ict4d.ict4dnews.persistence.sharedpreferences.ISharedPrefs
import at.ict4d.ict4dnews.persistence.sharedpreferences.SharedPrefs
import io.reactivex.disposables.CompositeDisposable
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val helperModule = module {

    factory { CompositeDisposable() }

    single { WorkManager.getInstance(get<ICT4DNewsApplication>()) }

    single<ISharedPrefs> { SharedPrefs(androidApplication() as ICT4DNewsApplication) }

    factory { UpdateNewsServiceHandler(get()) }
}
