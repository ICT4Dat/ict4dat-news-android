package at.ict4d.ict4dnews.dagger.modules.activities

import android.support.v7.app.AppCompatActivity
import at.ict4d.ict4dnews.dagger.scopes.PerActivity
import at.ict4d.ict4dnews.dagger.scopes.PerFragment
import at.ict4d.ict4dnews.screens.MainNavigationActivity
import at.ict4d.ict4dnews.screens.ict4d.TabbedICT4DFragment
import at.ict4d.ict4dnews.screens.ict4d.ict4d.ICT4DFragment
import at.ict4d.ict4dnews.screens.ict4d.ict4dat.ICT4DatFragment
import at.ict4d.ict4dnews.screens.more.MoreFragment
import at.ict4d.ict4dnews.screens.news.blogandsource.BlogAndSourceFragment
import at.ict4d.ict4dnews.screens.news.list.ICT4DNewsFragment
import at.ict4d.ict4dnews.screens.splashscreen.SplashFragment
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module(includes = [BaseActivityModule::class])
abstract class MainNavigationActivityModule {

    @PerFragment
    @ContributesAndroidInjector
    abstract fun ict4dNewsFragmentInjector(): ICT4DNewsFragment

    @PerFragment
    @ContributesAndroidInjector
    abstract fun ict4dFragmentInjector(): ICT4DFragment

    @PerFragment
    @ContributesAndroidInjector
    abstract fun moreFragmentInjector(): MoreFragment

    @PerFragment
    @ContributesAndroidInjector
    abstract fun ict4dAtFragmentInjector(): ICT4DatFragment

    @PerFragment
    @ContributesAndroidInjector
    abstract fun tabbedICT4DFragmentInjector(): TabbedICT4DFragment

    @PerFragment
    @ContributesAndroidInjector
    abstract fun blogAndSourceFragmentInjector(): BlogAndSourceFragment

    @PerFragment
    @ContributesAndroidInjector
    abstract fun splashFragmentFragmentInjector(): SplashFragment

    @Binds
    @PerActivity
    abstract fun appCompatActivity(mainNavigationActivity: MainNavigationActivity): AppCompatActivity
}