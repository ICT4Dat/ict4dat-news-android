package at.ict4d.ict4dnews.screens.news.list

import at.ict4d.ict4dnews.models.News
import at.ict4d.ict4dnews.persistence.IPersistenceManager
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class NewNewsHandler @Inject constructor(private val persistenceManager: IPersistenceManager) {
    private val existingNews: ArrayList<News> = arrayListOf()

    fun getExistingNewsFromDatabase() {
        val disposable = CompositeDisposable()
        existingNews.clear()
        disposable.add(persistenceManager.getAllActiveNewsAsFlowable().subscribeOn(Schedulers.io()).subscribe {
            if (it != null && existingNews.isEmpty()) {
                existingNews.addAll(it)
            }
        })
    }

    fun getNewNews(newNews: List<News>): List<News> {
        Timber.d("Number of existingNewsList is ----> ${existingNews.size}")
        val temp = newNews.filter { it !in existingNews }
        Timber.d("Number of new news is ----> ${temp.size}")

        return temp
    }
}