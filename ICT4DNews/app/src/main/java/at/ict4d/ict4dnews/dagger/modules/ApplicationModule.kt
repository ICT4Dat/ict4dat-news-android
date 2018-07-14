package at.ict4d.ict4dnews.dagger.modules

import android.app.Application
import at.ict4d.ict4dnews.ICT4DNewsApplication
import at.ict4d.ict4dnews.dagger.modules.activities.ICT4DNewsDetailActivityModule
import at.ict4d.ict4dnews.dagger.modules.activities.MainNavigationActivityModule
import at.ict4d.ict4dnews.dagger.scopes.PerActivity
import at.ict4d.ict4dnews.screens.MainNavigationActivity
import at.ict4d.ict4dnews.screens.news.detail.ICT4DNewsDetailActivity
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Suppress("unused")
@Module(includes = [AndroidSupportInjectionModule::class])
abstract class ApplicationModule {

    @Binds
    @Singleton
    abstract fun application(application: ICT4DNewsApplication): Application

    @PerActivity
    @ContributesAndroidInjector(modules = [ICT4DNewsDetailActivityModule::class])
    abstract fun iCT4DNewsDetailActivityInjector(): ICT4DNewsDetailActivity

    @PerActivity
    @ContributesAndroidInjector(modules = [MainNavigationActivityModule::class])
    abstract fun mainNavigationActivityInjector(): MainNavigationActivity
}