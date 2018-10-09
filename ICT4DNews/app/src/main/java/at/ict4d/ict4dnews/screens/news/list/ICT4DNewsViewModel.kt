package at.ict4d.ict4dnews.screens.news.list

import android.arch.lifecycle.MutableLiveData
import at.ict4d.ict4dnews.models.Blog
import at.ict4d.ict4dnews.models.News
import at.ict4d.ict4dnews.persistence.IPersistenceManager
import at.ict4d.ict4dnews.screens.base.BaseViewModel
import at.ict4d.ict4dnews.server.IServer
import at.ict4d.ict4dnews.utils.NewsRefreshDoneMessage
import at.ict4d.ict4dnews.utils.RxEventBus
import at.ict4d.ict4dnews.utils.ServerErrorMessage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Flowables
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import org.threeten.bp.LocalDate
import javax.inject.Inject

class ICT4DNewsViewModel @Inject constructor(
    persistenceManager: IPersistenceManager,
    private val server: IServer,
    rxEventBus: RxEventBus
) : BaseViewModel() {

    val newsList = MutableLiveData<List<Pair<News, Blog>>>()
    val searchedNewsList = MutableLiveData<List<Pair<News, Blog>>>()
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

        compositeDisposable.add(Flowables.combineLatest(
            persistenceManager.getAllActiveNewsAsFlowable(),
            persistenceManager.getAllActiveBlogsAsFlowable()
        )
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .subscribe {
                val resultList = mutableListOf<Pair<News, Blog>>()

                var blog: Blog?
                it.first.forEach { news ->
                    blog = it.second.find { b -> news.blogID == b.feed_url }
                    blog?.let { b ->
                        resultList.add(Pair(news, b))
                    }
                }
                newsList.postValue(resultList)
            })

        doAsync {
            if (persistenceManager.getLastAutomaticNewsUpdateLocalDate().get().dayOfMonth != LocalDate.now().dayOfMonth ||
                persistenceManager.getCountOfNews() == 0) {
                compositeDisposable.add(server.loadBlogs())
                requestToLoadFeedsFromServers()
            }
        }
    }

    fun requestToLoadFeedsFromServers() {
        if (isRefreshing.value == null || isRefreshing.value == false) {
            isRefreshing.postValue(true)

            compositeDisposable.add(server.loadAllNewsFromAllActiveBlogs())
        }
    }

    fun performSearch(searchQuery: String) {

        this.searchQuery = searchQuery

        val query = searchQuery.toLowerCase().trim()

        searchedNewsList.postValue(newsList.value?.filter { pair ->
            pair.first.title?.toLowerCase()?.contains(query) ?: false ||
                pair.second.name.toLowerCase().contains(query)
        })
    }
}