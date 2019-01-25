package at.ict4d.ict4dnews.background

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import at.ict4d.ict4dnews.ICT4DNewsApplication
import at.ict4d.ict4dnews.NOTIFICATION_CHANNEL_ID
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.persistence.IPersistenceManager
import at.ict4d.ict4dnews.screens.MainNavigationActivity
import at.ict4d.ict4dnews.server.IServer
import timber.log.Timber
import javax.inject.Inject

const val NEWS_WORKER_TAG = "NEWS_UPDATE_TASK"
const val NEWS_WORKER_NOTIFICATION_ID = 99

class UpdateNewsWorker(val context: Context, workParams: WorkerParameters) : Worker(context, workParams) {

    @Inject
    protected lateinit var server: IServer

    @Inject
    protected lateinit var persistenceManager: IPersistenceManager

    init {
        ICT4DNewsApplication.instance.component.inject(this)
    }

    override fun doWork(): Result {

        val latestNewsDate = persistenceManager.getLatestNewsPublishedDate()
        val downloadStatus = server.loadAllNewsFromAllActiveBlogsSynchronous()

        // TODO: get all News which are older than 'latestNewsDate'
        val newNews = persistenceManager.requestLatestNewsByDate(latestNewsDate)
        // TODO: if >0 then create a notification and display the results
        Timber.e("Size sda is ----> $newNews")
        if (newNews > 0) {
            val intent = Intent(context, MainNavigationActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
            val notificationBuilder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(context.getString(R.string.news_update_complete))
                .setContentText(
                    String.format(
                        context.getString(R.string.news_update_notification_content_text),
                        newNews
                    )
                )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
            with(NotificationManagerCompat.from(context)) {
                // notificationId is a unique int for each notification that you must define
                notify(NEWS_WORKER_NOTIFICATION_ID, notificationBuilder.build())
            }
        }
        return if (downloadStatus) Result.success() else Result.failure()
    }
}