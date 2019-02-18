package at.ict4d.ict4dnews.background

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import at.ict4d.ict4dnews.ICT4DNewsApplication
import at.ict4d.ict4dnews.persistence.IPersistenceManager
import at.ict4d.ict4dnews.server.IServer
import at.ict4d.ict4dnews.utils.NotificationHandler
import javax.inject.Inject

const val NEWS_WORKER_TAG = "NEWS_UPDATE_TASK"

class UpdateNewsWorker(val context: Context, workParams: WorkerParameters) : Worker(context, workParams) {

    @Inject
    protected lateinit var server: IServer

    @Inject
    protected lateinit var persistenceManager: IPersistenceManager

    @Inject
    protected lateinit var notificationHandler: NotificationHandler

    init {
        ICT4DNewsApplication.instance.component.inject(this)
    }

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