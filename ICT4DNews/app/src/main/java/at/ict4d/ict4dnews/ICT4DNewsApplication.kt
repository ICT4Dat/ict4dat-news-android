package at.ict4d.ict4dnews

import android.app.Activity
import android.content.Context
import androidx.multidex.MultiDexApplication
import at.ict4d.ict4dnews.dagger.components.ApplicationComponent
import at.ict4d.ict4dnews.dagger.components.DaggerApplicationComponent
import com.facebook.stetho.Stetho
import com.jakewharton.threetenabp.AndroidThreeTen
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import timber.log.Timber
import javax.inject.Inject

class ICT4DNewsApplication : MultiDexApplication(), HasActivityInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var component: ApplicationComponent

    private lateinit var refWatcher: RefWatcher

    companion object {

        @JvmStatic
        lateinit var instance: ICT4DNewsApplication

        @JvmStatic
        fun getRefWatcher(context: Context): RefWatcher {
            val applicationContext = context.applicationContext as ICT4DNewsApplication
            return applicationContext.refWatcher
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        // java.time backport
        AndroidThreeTen.init(this)

        DaggerApplicationComponent.builder().create(this).inject(this)

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        refWatcher = LeakCanary.install(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Stetho.initializeWithDefaults(this)
        }
    }

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector
}