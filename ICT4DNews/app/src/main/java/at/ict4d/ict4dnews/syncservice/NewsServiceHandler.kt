package at.ict4d.ict4dnews.syncservice

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import at.ict4d.ict4dnews.extensions.isLastUpdateIsDayAgo
import at.ict4d.ict4dnews.persistence.PersistenceManager
import timber.log.Timber
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NewsServiceHandler @Inject constructor(
    private val persistenceManager: PersistenceManager,
    private val workManager: WorkManager
) {

    // TODO("Remove all of the log statements")

    private fun getConstraintsForNewsSyncService(): Constraints {
        return Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()
    }

    private fun isServiceAlreadyEnqueued(): Boolean {
        val result = persistenceManager.getNewsServiceId().get()
        return if (result.isNotEmpty()) {
            val isServiceDone = workManager.getWorkInfoById(UUID.fromString(result)).isDone
            Timber.d("Is service queued ----> ${!isServiceDone}")
            !isServiceDone
        } else {
            false
        }
    }

    private fun isLastDownloadTimeIsDayAgo(): Boolean {
        return persistenceManager.getLastAutomaticNewsUpdateLocalDate().get().isLastUpdateIsDayAgo()
    }

    fun requestToRunService() {
        if (isServiceAlreadyEnqueued() || isLastDownloadTimeIsDayAgo()) {
            Timber.d("Service is already running or enqueued or blocked or last update is less than a day")
            return
        }
        val newsWork = PeriodicWorkRequestBuilder<NewsWorker>(1, TimeUnit.DAYS)
            .setConstraints(getConstraintsForNewsSyncService())
            .build()
        persistenceManager.getNewsServiceId().set(newsWork.id.toString())
        workManager.enqueue(newsWork)
    }
}