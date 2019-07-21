package at.ict4d.ict4dnews.background

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import at.ict4d.ict4dnews.persistence.IPersistenceManager
import at.ict4d.ict4dnews.server.IServer
import at.ict4d.ict4dnews.utils.NotificationHandler
import org.koin.core.KoinComponent
import org.koin.core.inject

const val NEWS_WORKER_TAG = "NEWS_UPDATE_TASK"

class UpdateNewsWorker(val context: Context, workParams: WorkerParameters) : Worker(context, workParams),
    KoinComponent {

    private val server: IServer by inject()
    private val persistenceManager: IPersistenceManager by inject()
    private val notificationHandler: NotificationHandler by inject()

    override fun doWork(): Result {

        val latestNewsDate = persistenceManager.getLatestNewsPublishedDate()
        val downloadStatus = server.loadAllNewsFromAllActiveBlogsSynchronous()

        // Get all News which are older than 'latestNewsDate'
        val newNews = persistenceManager.requestLatestNewsByDate(latestNewsDate)
        // If there are new news then create a notification and display the results
        notificationHandler.displayNewsNotifications(newNews, context)

        return if (downloadStatus) Result.success() else Result.failure()
    }
}