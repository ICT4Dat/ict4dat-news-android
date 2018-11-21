package at.ict4d.ict4dnews.syncservice

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
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
    var newsWorkId: UUID? = null

    private fun getConstraintsForNewsSyncService(): Constraints {
        return Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()
    }

    private fun isServiceAlreadyRunning(): Boolean {
        newsWorkId?.let {
            val state = workManager.getWorkInfoById(it).get().state

            return state == WorkInfo.State.ENQUEUED || state == WorkInfo.State.BLOCKED || state == WorkInfo.State.RUNNING
        }
        return false
    }

    private fun isLastDownloadTimeIsDayAgo(): Boolean {
        return persistenceManager.getLastAutomaticNewsUpdateLocalDate().get().isLastUpdateIsDayAgo()
    }

    fun requestToRunService() {
        if (isServiceAlreadyRunning() || !isLastDownloadTimeIsDayAgo()) {
            Timber.d("Service is already running or enqueued or blocked or last update is less than a day")
            return
        }
        val newsWork =
            PeriodicWorkRequestBuilder<NewsWorker>(1, TimeUnit.DAYS)
                .setConstraints(getConstraintsForNewsSyncService())
                .build()
        newsWorkId = newsWork.id
        workManager.enqueue(newsWork)
    }
}