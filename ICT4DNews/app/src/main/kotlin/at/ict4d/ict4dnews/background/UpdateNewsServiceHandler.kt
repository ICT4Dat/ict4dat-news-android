package at.ict4d.ict4dnews.background

import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class UpdateNewsServiceHandler(private val workManager: WorkManager) {
    fun requestToRegisterService() {
        val newsWork =
            PeriodicWorkRequestBuilder<UpdateNewsWorker>(1, TimeUnit.DAYS)
                .setConstraints(getConstraintsForNewsSyncService())
                .setInitialDelay(1, TimeUnit.DAYS)
                // retry after 1 hour
                .setBackoffCriteria(
                    BackoffPolicy.EXPONENTIAL,
                    1,
                    TimeUnit.HOURS,
                )
                .addTag(NEWS_WORKER_TAG)
                .build()
        workManager.enqueueUniquePeriodicWork(NEWS_WORKER_TAG, ExistingPeriodicWorkPolicy.KEEP, newsWork)
    }

    fun cancelTask() = workManager.cancelAllWorkByTag(NEWS_WORKER_TAG)

    private fun getConstraintsForNewsSyncService() =
        Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()
}
