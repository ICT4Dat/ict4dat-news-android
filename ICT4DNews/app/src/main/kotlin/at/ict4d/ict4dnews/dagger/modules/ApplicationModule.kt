package at.ict4d.ict4dnews.dagger.modules

import at.ict4d.ict4dnews.dagger.modules.activities.MainNavigationActivityModule
import at.ict4d.ict4dnews.dagger.scopes.PerActivity
import at.ict4d.ict4dnews.screens.MainNavigationActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class ApplicationModule {
    @PerActivity
    @ContributesAndroidInjector(modules = [MainNavigationActivityModule::class])
    abstract fun mainNavigationActivityInjector(): MainNavigationActivity
}