package at.ict4d.ict4dnews

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.multidex.MultiDex
import at.ict4d.ict4dnews.di.modules.apiServiceModule
import at.ict4d.ict4dnews.di.modules.helperModule
import at.ict4d.ict4dnews.di.modules.repositoryModule
import at.ict4d.ict4dnews.di.modules.roomModule
import at.ict4d.ict4dnews.di.modules.viewModelModule
import at.ict4d.ict4dnews.persistence.sharedpreferences.SharedPrefs
import com.facebook.stetho.Stetho
import com.jakewharton.threetenabp.AndroidThreeTen
import io.sentry.Sentry
import io.sentry.android.AndroidSentryClientFactory
import leakcanary.AppWatcher
import leakcanary.ObjectWatcher
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

const val NOTIFICATION_CHANNEL_ID = "ict4d_news_app"

open class ICT4DNewsApplication : Application() {

    private lateinit var objWatcher: ObjectWatcher
    private val sharedPrefs by inject<SharedPrefs>()

    companion object {

        @JvmStatic
        fun getRefWatcher(context: Context): ObjectWatcher {
            val applicationContext = context.applicationContext as ICT4DNewsApplication
            return applicationContext.objWatcher
        }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ICT4DNewsApplication)
            modules(listOf(apiServiceModule, helperModule, roomModule, viewModelModule, repositoryModule))
        }

        // java.time backport
        AndroidThreeTen.init(this)

        objWatcher = AppWatcher.objectWatcher

        setUpTimber()

        setUpSentryBugTracking()

        if (BuildConfig.DEBUG) {
            installStetho()
        }

        createNotificationChannel()
    }

    private fun setUpSentryBugTracking() {
        if (BuildConfig.DEBUG || !sharedPrefs.isBugTrackingEnabled.get()) {
            Timber.i("Sentry is NOT running due to debug build or disabled bug tracking in the settings")
        } else {
            try {
                Sentry.init(BuildConfig.SENTRY_DNS, AndroidSentryClientFactory(applicationContext))
            } catch (e: Exception) {
                Timber.e(
                    e,
                    "Sentry is NOT running due to config error, see sentry-config.gradle for more information"
                )
            }
        }
    }

    private fun setUpTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String? {
                    return String.format(
                        "C:%s: Line %s",
                        super.createStackElementTag(element),
                        element.lineNumber
                    )
                }
            })
        } else {
            Timber.plant(ReleaseTree())
        }
    }

    protected open fun installStetho() {
        Stetho.initializeWithDefaults(this)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    /*
     * https://developer.android.com/training/notify-user/build-notification
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

class ReleaseTree : Timber.Tree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {

        if (priority == Log.ERROR) {
            Sentry.capture("$message\n${t?.message}")
        }
    }
}
