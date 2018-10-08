package at.ict4d.ict4dnews.screens.news.list

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import at.ict4d.ict4dnews.models.News
import at.ict4d.ict4dnews.persistence.IPersistenceManager
import at.ict4d.ict4dnews.screens.base.BaseViewModel
import at.ict4d.ict4dnews.server.IServer
import at.ict4d.ict4dnews.utils.NewsRefreshDoneMessage
import at.ict4d.ict4dnews.utils.RxEventBus
import at.ict4d.ict4dnews.utils.ServerErrorMessage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import javax.inject.Inject

class ICT4DNewsViewModel @Inject constructor(
    private val persistenceManager: IPersistenceManager,
    private val server: IServer,
    rxEventBus: RxEventBus
) : BaseViewModel() {

    val newsList: LiveData<List<News>> = persistenceManager.getAllActiveNews()
    val searchedNewsList: MutableLiveData<List<News>> = MutableLiveData()
    var searchQuery: String? = null

    init {
        compositeDisposable.add(rxEventBus.filteredObservable(NewsRefreshDoneMessage::class.java)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                isRefreshing.value = false
            })

        compositeDisposable.add(rxEventBus.filteredObservable(ServerErrorMessage::class.java)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                isRefreshing.value = false
            })

        compositeDisposable.add(server.loadBlogs())
    }

    fun requestToLoadFeedsFromServers() {
        if (isRefreshing.value == null || isRefreshing.value == false) {
            isRefreshing.value = true

            doAsync {
                compositeDisposable.add(server.loadAllNewsFromAllActiveBlogs())
            }
        }
    }

    fun performSearch(searchQuery: String) {
        // TODO(change the filter logic in future when we have new UI)
        this.searchQuery = searchQuery
        searchedNewsList.postValue(newsList.value?.filter { it.title?.contains(searchQuery.trim(), true) ?: false })
    }
}