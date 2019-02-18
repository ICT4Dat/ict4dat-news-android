package at.ict4d.ict4dnews.utils

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import at.ict4d.ict4dnews.BuildConfig
import at.ict4d.ict4dnews.NOTIFICATION_CHANNEL_ID
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.extensions.stripHtml
import at.ict4d.ict4dnews.models.News
import at.ict4d.ict4dnews.persistence.IPersistenceManager
import at.ict4d.ict4dnews.screens.MainNavigationActivity
import javax.inject.Inject

const val NEWS_WORKER_SUMMARY_NOTIFICATION_ID = 1
const val NEWS_WORKER_NOTIFICATION_GROUP = "${BuildConfig.APPLICATION_ID}.NEWS_UPDATE"

class NotificationHandler @Inject constructor(private val persistenceManager: IPersistenceManager) {

    fun displayNewsNotifications(newsList: List<News>, context: Context) {

        if (newsList.isNotEmpty()) {

            val allActiveBlogs = persistenceManager.getAllActiveBlogs()
            val notifications = mutableListOf<Notification>()
            val summaryNotificationStyle = NotificationCompat.InboxStyle()

            newsList.forEach { news ->

                val pendingIntent = NavDeepLinkBuilder(context)
                    .setGraph(R.navigation.nav_graph)
                    .setDestination(R.id.ICT4DNewsDetailFragment)
                    .setArguments(bundleOf(Pair("newsItem", news)))
                    .createPendingIntent()

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
                    notificationIdCounter += 1
                }

                notify(NEWS_WORKER_SUMMARY_NOTIFICATION_ID, summaryNotification)
            }
        } else if (BuildConfig.DEBUG) { // Display a notification if the updates was successful, but no new news were found

            val debugNotification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("ICT4D News Update")
                .setContentText("Update was complete, but no new news found...")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setGroup(NEWS_WORKER_NOTIFICATION_GROUP)
                .build()

            with(NotificationManagerCompat.from(context)) { notify(999, debugNotification) }
        }
    }
}