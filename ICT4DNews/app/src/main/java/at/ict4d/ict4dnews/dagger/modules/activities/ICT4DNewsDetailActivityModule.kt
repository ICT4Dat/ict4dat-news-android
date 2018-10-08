package at.ict4d.ict4dnews.dagger.modules.activities

import at.ict4d.ict4dnews.dagger.scopes.PerFragment
import at.ict4d.ict4dnews.screens.news.detail.ICT4DNewsDetailFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class ICT4DNewsDetailActivityModule {

    @PerFragment
    @ContributesAndroidInjector
    abstract fun ict4dNewsDetailActivityInjector(): ICT4DNewsDetailFragment
}