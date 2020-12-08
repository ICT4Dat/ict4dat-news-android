package at.ict4d.ict4dnews.background

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import at.ict4d.ict4dnews.server.repositories.NewsRepository
import at.ict4d.ict4dnews.utils.NotificationHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject

const val NEWS_WORKER_TAG = "NEWS_UPDATE_TASK"

class UpdateNewsWorker(val context: Context, workParams: WorkerParameters) : CoroutineWorker(context, workParams),
    KoinComponent {

    private val newsRepository by inject<NewsRepository>()
    private val notificationHandler by inject<NotificationHandler>()

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {

        val latestNewsDate = newsRepository.getLatestNewsPublishedDate().first()
        val downloadStatus = newsRepository.downloadAllNews()

        // Get all News which are older than 'latestNewsDate'
        val newNews = newsRepository.requestLatestNewsByDate(latestNewsDate).first()
        // If there are new news then create a notification and display the results
        notificationHandler.displayNewsNotifications(newNews, context)

        if (downloadStatus.totalCount > 0 && downloadStatus.successfulBlogs.count() > 0) {
            Result.success()
        } else {
            Result.retry()
        }
    }
}
