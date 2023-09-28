package at.ict4d.ict4dnews.di.modules

import androidx.work.WorkManager
import at.ict4d.ict4dnews.ICT4DNewsApplication
import at.ict4d.ict4dnews.background.UpdateNewsServiceHandler
import at.ict4d.ict4dnews.persistence.sharedpreferences.SharedPrefs
import at.ict4d.ict4dnews.utils.NotificationHandler
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val helperModule = module {

    single { androidApplication() as ICT4DNewsApplication }

    single { WorkManager.getInstance(get<ICT4DNewsApplication>()) }

    single { SharedPrefs(get()) }

    factory { UpdateNewsServiceHandler(get()) }

    single { NotificationHandler(get()) }
}
