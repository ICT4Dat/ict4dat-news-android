package at.ict4d.ict4dnews

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.multidex.MultiDex
import at.ict4d.ict4dnews.dagger.components.ApplicationComponent
import at.ict4d.ict4dnews.dagger.components.DaggerApplicationComponent
import com.facebook.stetho.Stetho
import com.jakewharton.threetenabp.AndroidThreeTen
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import timber.log.Timber
import javax.inject.Inject

const val NOTIFICATION_CHANNEL_ID = "ict4d_news_app"

class ICT4DNewsApplication : DaggerApplication() {

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

        createNotificationChannel()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerApplicationComponent.builder().create(this)

    /**
     * @see https://developer.android.com/training/notify-user/build-notification
     */
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.notification_channel_name)
            val descriptionText = getString(R.string.notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}