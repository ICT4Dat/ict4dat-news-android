package at.ict4d.ict4dnews.background

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import at.ict4d.ict4dnews.BuildConfig
import at.ict4d.ict4dnews.ICT4DNewsApplication
import at.ict4d.ict4dnews.NOTIFICATION_CHANNEL_ID
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.extensions.stripHtml
import at.ict4d.ict4dnews.models.News
import at.ict4d.ict4dnews.persistence.IPersistenceManager
import at.ict4d.ict4dnews.screens.MainNavigationActivity
import at.ict4d.ict4dnews.server.IServer
import javax.inject.Inject

const val NEWS_WORKER_TAG = "NEWS_UPDATE_TASK"
const val NEWS_WORKER_SUMMARY_NOTIFICATION_ID = 1
const val NEWS_WORKER_NOTIFICATION_GROUP = "${BuildConfig.APPLICATION_ID}.NEWS_UPDATE"

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

        // Get all News which are older than 'latestNewsDate'
        val newNews = persistenceManager.requestLatestNewsByDate(latestNewsDate)
        // If there are new news then create a notification and display the results
        displayNotifications(newNews)

        return if (downloadStatus) Result.success() else Result.failure()
    }

    private fun displayNotifications(newsList: List<News>) {

        if (newsList.isNotEmpty()) {

            val allActiveBlogs = persistenceManager.getAllActiveBlogs()
            val notifications = mutableListOf<Notification>()
            val summaryNotificationStyle = NotificationCompat.InboxStyle()

            newsList.forEach { news ->

                // TODO set right intent to open the detail view
                val intent = Intent(context, MainNavigationActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

                notifications.add(
                    NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle(
                            allActiveBlogs.find { blog -> blog.feed_url == news.blogID }?.name
                                ?: context.getString(R.string.app_name)
                        )
                        .setContentText(news.title ?: "")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setGroup(NEWS_WORKER_NOTIFICATION_GROUP)
                        .setStyle(
                            NotificationCompat.BigTextStyle()
                                .bigText(news.description?.stripHtml() ?: "")
                        )
                        .build()
                )

                if (!news.title.isNullOrEmpty()) {
                    summaryNotificationStyle.addLine(news.title)
                }
            }

            // Create summary notification for Android < 7.0
            val summaryIntent = Intent(context, MainNavigationActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val summaryPendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, summaryIntent, 0)
            summaryNotificationStyle
                .setBigContentTitle(context.getString(R.string.news_update_complete))
                .setSummaryText(context.getString(R.string.news_update_notification_content_text, newsList.size))
            val summaryNotification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(context.getString(R.string.news_update_complete))
                .setContentText(context.getString(R.string.news_update_notification_content_text, newsList.size))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(summaryPendingIntent)
                .setGroup(NEWS_WORKER_NOTIFICATION_GROUP)
                .setGroupSummary(true)
                .setStyle(summaryNotificationStyle)
                .build()

            with(NotificationManagerCompat.from(context)) {
                var notificationIdCounter = 2
                notifications.forEach { notification ->
                    notify(notificationIdCounter, notification)
                    notificationIdCounter += notificationIdCounter
                }

                notify(NEWS_WORKER_SUMMARY_NOTIFICATION_ID, summaryNotification)
            }
        }
    }
}