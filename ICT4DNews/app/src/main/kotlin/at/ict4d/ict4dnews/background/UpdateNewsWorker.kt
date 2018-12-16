package at.ict4d.ict4dnews.background

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import at.ict4d.ict4dnews.NOTIFICATION_CHANNEL_ID
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.persistence.IPersistenceManager
import at.ict4d.ict4dnews.screens.MainNavigationActivity
import at.ict4d.ict4dnews.server.IServer
import javax.inject.Inject

const val NEWS_WORKER_TAG = "NEWS_UPDATE_TASK"

class UpdateNewsWorker(val context: Context, workParams: WorkerParameters) : Worker(context, workParams) {

    @Inject
    protected lateinit var server: IServer

    @Inject
    protected lateinit var persistenceManager: IPersistenceManager

    override fun doWork(): Result {

        val latestNewsDate = persistenceManager.getLatestNewsPublishedDate()
        server.loadAllNewsFromAllActiveBlogsSynchronous()

        // TODO: get all News which are older than 'latestNewsDate'
        // TODO: if >0 then create a notification and display the results
        val intent = Intent(context, MainNavigationActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        val notificationBuilder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("News Update Complete")
            .setContentText("Jipi... we updated ?? news for you, read them now")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(0, notificationBuilder.build())
        }

        return Result.success()
    }
}