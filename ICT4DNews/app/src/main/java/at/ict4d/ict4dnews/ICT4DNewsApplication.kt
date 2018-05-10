package at.ict4d.ict4dnews

import android.app.Application
import at.ict4d.ict4dnews.dagger.components.ApplicationComponent
import at.ict4d.ict4dnews.dagger.components.DaggerApplicationComponent
import at.ict4d.ict4dnews.dagger.modules.ApplicationModule
import timber.log.Timber

class ICT4DNewsApplication : Application() {

    companion object {
        @JvmStatic
        lateinit var component: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()
        initDagger()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initDagger() {
        component = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
    }

}