package at.ict4d.ict4dnews.dagger.modules.activities

import android.app.Activity
import android.content.Context
import android.support.v7.app.AppCompatActivity
import at.ict4d.ict4dnews.dagger.scopes.PerActivity
import dagger.Binds
import dagger.Module

@Suppress("unused")
@Module
abstract class BaseActivityModule {

    @Binds
    @PerActivity
    abstract fun activity(appCompatActivity: AppCompatActivity): Activity

    @Binds
    @PerActivity
    abstract fun activityContext(activity: Activity): Context
}