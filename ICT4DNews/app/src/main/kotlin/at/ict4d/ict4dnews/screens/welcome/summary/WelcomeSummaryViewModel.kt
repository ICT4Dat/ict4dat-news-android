package at.ict4d.ict4dnews.screens.welcome.summary

import androidx.lifecycle.ViewModel
import at.ict4d.ict4dnews.persistence.sharedpreferences.SharedPrefs
import at.ict4d.ict4dnews.server.repositories.BlogsRepository

class WelcomeSummaryViewModel(
    blogsRepository: BlogsRepository,
    sharedPrefs: SharedPrefs,
) : ViewModel() {
    val blogsCount = blogsRepository.getBlogsCount()

    val isAutomaticNewsUpdateEnabled = sharedPrefs.isAutomaticNewsUpdateEnabled
}
