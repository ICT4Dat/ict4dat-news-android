package at.ict4d.ict4dnews.dagger.modules.activities

import android.support.v7.app.AppCompatActivity
import at.ict4d.ict4dnews.dagger.scopes.PerActivity
import at.ict4d.ict4dnews.dagger.scopes.PerFragment
import at.ict4d.ict4dnews.screens.news.detail.ICT4DNewsDetailActivity
import at.ict4d.ict4dnews.screens.news.detail.ICT4DNewsDetailFragment
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module(includes = [BaseActivityModule::class])
abstract class ICT4DNewsDetailActivityModule {

    @Binds
    @PerActivity
    abstract fun appCompatActivity(iCT4DNewsDetailActivity: ICT4DNewsDetailActivity): AppCompatActivity

    @PerFragment
    @ContributesAndroidInjector
    abstract fun ict4dNewsDetailActivityInjector(): ICT4DNewsDetailFragment
}