package at.ict4d.ict4dnews.screens.news.list

import androidx.lifecycle.MutableLiveData
import at.ict4d.ict4dnews.models.News
import at.ict4d.ict4dnews.persistence.IPersistenceManager
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class NewNewsHandler @Inject constructor(private val persistenceManager: IPersistenceManager) {
    private val existingNews: MutableLiveData<List<News>> = MutableLiveData()

    // TODO("Remove log statements")
    fun getExistingNewsFromDatabase() {
        val disposable = CompositeDisposable()
        existingNews.value = null
        disposable.add(persistenceManager.getAllActiveNewsAsFlowable().subscribeOn(Schedulers.io()).subscribe {
            if (it != null && existingNews.value == null) {
                Timber.d("size is above ----> ${it.size}")
                existingNews.postValue(it)
            }
        })
    }

    fun getNewNews(newNews: List<News>): List<News> {
        val existingNewsList = existingNews.value
        Timber.d("Number of existingNewsList is ----> ${existingNewsList?.size}")
        val temp = existingNewsList?.let { existingNews -> newNews.filter { it !in existingNews } }
        Timber.d("Number of new news is ----> ${temp?.size}")

        return temp ?: emptyList()
    }
}