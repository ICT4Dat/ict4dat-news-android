package at.ict4d.ict4dnews.screens.news.list

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import at.ict4d.ict4dnews.models.News
import at.ict4d.ict4dnews.persistence.IPersistenceManager
import at.ict4d.ict4dnews.screens.base.BaseViewModel
import at.ict4d.ict4dnews.server.IServer
import javax.inject.Inject

class ICT4DNewsViewModel @Inject constructor(
        persistenceManager: IPersistenceManager,
        private val server: IServer) : BaseViewModel() {

    var isRefreshing: MutableLiveData<Boolean> = MutableLiveData()
    val newsList: LiveData<List<News>> = persistenceManager.getAllOrderedByPublishedDate()

    init {
        requestToLoadJsonFeed()
    }

    fun requestToLoadJsonFeed() {
        if (isRefreshing.value == null || isRefreshing.value == false) {
            isRefreshing.value = true
            compositeDisposable.add(server.loadICT4DatJsonFeed())
        }
    }
}