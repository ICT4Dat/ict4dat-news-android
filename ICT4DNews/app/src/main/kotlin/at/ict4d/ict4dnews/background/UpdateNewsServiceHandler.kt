package at.ict4d.ict4dnews.background

import android.os.Build
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class UpdateNewsServiceHandler @Inject constructor(
    private val workManager: WorkManager
) {

    fun requestToRegisterService() {
        val newsWork = PeriodicWorkRequestBuilder<UpdateNewsWorker>(1, TimeUnit.DAYS)
            .setConstraints(getConstraintsForNewsSyncService())
            .addTag(NEWS_WORKER_TAG)
            .build()
        workManager.enqueueUniquePeriodicWork(NEWS_WORKER_TAG, ExistingPeriodicWorkPolicy.KEEP, newsWork)
    }

    private fun getConstraintsForNewsSyncService(): Constraints {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .setRequiresDeviceIdle(true)
                .build()
        } else {
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build()
        }
    }
}