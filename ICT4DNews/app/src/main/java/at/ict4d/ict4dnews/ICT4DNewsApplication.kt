package at.ict4d.ict4dnews

import android.app.Application
import android.content.Context
import at.ict4d.ict4dnews.dagger.components.ApplicationComponent
import at.ict4d.ict4dnews.dagger.components.DaggerApplicationComponent
import at.ict4d.ict4dnews.dagger.modules.ApplicationModule
import com.facebook.stetho.Stetho
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import timber.log.Timber

class ICT4DNewsApplication : Application() {

    companion object {
        @JvmStatic
        lateinit var component: ApplicationComponent

        @JvmStatic
        fun getRefWatcher(context: Context): RefWatcher {
            val applicationContext = context.applicationContext as ICT4DNewsApplication
            return applicationContext.refWatcher
        }
    }

    private lateinit var refWatcher: RefWatcher

    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        refWatcher = LeakCanary.install(this)

        initDagger()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Stetho.initializeWithDefaults(this)
        }
    }

    private fun initDagger() {
        component = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }

}