package at.ict4d.ict4dnews.dagger.modules

import at.ict4d.ict4dnews.dagger.modules.activities.ICT4DNewsDetailActivityModule
import at.ict4d.ict4dnews.dagger.modules.activities.MainNavigationActivityModule
import at.ict4d.ict4dnews.dagger.scopes.PerActivity
import at.ict4d.ict4dnews.screens.MainNavigationActivity
import at.ict4d.ict4dnews.screens.news.detail.ICT4DNewsDetailActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class ApplicationModule {

    @PerActivity
    @ContributesAndroidInjector(modules = [ICT4DNewsDetailActivityModule::class])
    abstract fun iCT4DNewsDetailActivityInjector(): ICT4DNewsDetailActivity

    @PerActivity
    @ContributesAndroidInjector(modules = [MainNavigationActivityModule::class])
    abstract fun mainNavigationActivityInjector(): MainNavigationActivity
}