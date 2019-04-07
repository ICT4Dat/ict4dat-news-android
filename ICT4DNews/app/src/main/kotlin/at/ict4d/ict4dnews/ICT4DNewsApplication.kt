package at.ict4d.ict4dnews

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.multidex.MultiDex
import at.ict4d.ict4dnews.dagger.components.ApplicationComponent
import at.ict4d.ict4dnews.dagger.components.DaggerApplicationComponent
import at.ict4d.ict4dnews.persistence.IPersistenceManager
import com.facebook.stetho.Stetho
import com.jakewharton.threetenabp.AndroidThreeTen
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import io.sentry.Sentry
import io.sentry.android.AndroidSentryClientFactory
import timber.log.Timber
import javax.inject.Inject

const val NOTIFICATION_CHANNEL_ID = "ict4d_news_app"

open class ICT4DNewsApplication : DaggerApplication() {

    @Inject
    lateinit var component: ApplicationComponent

    @Inject
    lateinit var persistenceManager: IPersistenceManager

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
        instance = this
        super.onCreate()

        // java.time backport
        AndroidThreeTen.init(this)

        installLeakCanary()

        setUpTimber()

        setUpSentryBugTracking()

        if (BuildConfig.DEBUG) {
            installStetho()
        }

        createNotificationChannel()
    }

    private fun setUpSentryBugTracking() {
        if (BuildConfig.DEBUG || !persistenceManager.isBugTrackingEnabled().get()) {
            Timber.i("Sentry is NOT running due to debug build or disabled bug tracking in the settings")
        } else {
            try {
                Sentry.init(BuildConfig.SENTRY_DNS, AndroidSentryClientFactory(applicationContext))
            } catch (e: Exception) {
                Timber.e(e, "Sentry is NOT running due to config error, see sentry-config.gradle for more information")
            }
        }
    }

    private fun setUpTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String? {
                    return String.format("C:%s: Line %s", super.createStackElementTag(element), element.lineNumber)
                }
            })
        } else {
            Timber.plant(ReleaseTree())
        }
    }

    protected open fun installLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        refWatcher = LeakCanary.install(this)
    }

    protected open fun installStetho() {
        Stetho.initializeWithDefaults(this)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerApplicationComponent.factory().create(this)

    /**
     * @see https://developer.android.com/training/notify-user/build-notification
     */
    @Suppress("KDocUnresolvedReference")
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

class ReleaseTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {

        if (priority == Log.ERROR) {
            Sentry.capture("$message\n${t?.message}")
        }
    }
}