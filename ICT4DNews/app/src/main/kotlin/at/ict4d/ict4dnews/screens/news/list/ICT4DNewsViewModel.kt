package at.ict4d.ict4dnews.screens.news.list

import androidx.lifecycle.MutableLiveData
import at.ict4d.ict4dnews.extensions.filterObservableAndSetThread
import at.ict4d.ict4dnews.models.Blog
import at.ict4d.ict4dnews.models.News
import at.ict4d.ict4dnews.persistence.IPersistenceManager
import at.ict4d.ict4dnews.screens.base.BaseViewModel
import at.ict4d.ict4dnews.server.IServer
import at.ict4d.ict4dnews.utils.BlogsRefreshDoneMessage
import at.ict4d.ict4dnews.utils.NewsRefreshDoneMessage
import at.ict4d.ict4dnews.utils.RxEventBus
import at.ict4d.ict4dnews.utils.ServerErrorMessage
import io.reactivex.rxkotlin.Flowables
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import org.threeten.bp.LocalDate
import javax.inject.Inject

class ICT4DNewsViewModel @Inject constructor(
    private val persistenceManager: IPersistenceManager,
    private val server: IServer,
    rxEventBus: RxEventBus
) : BaseViewModel() {

    val blogsCount = persistenceManager.getBlogsCountAsLiveData()
    var isSplashNotStartedOnce = true
    val newsList = MutableLiveData<List<Pair<News, Blog>>>()
    val searchedNewsList = MutableLiveData<List<Pair<News, Blog>>>()
    var searchQuery: String? = null
    var shouldMoveScrollToTop: Boolean = false

    init {
        compositeDisposable.add(rxEventBus.filterObservableAndSetThread<NewsRefreshDoneMessage>(subscribeThread = Schedulers.io())
            .subscribe {
                isRefreshing.value = false
                shouldMoveScrollToTop = true
            })

        compositeDisposable.add(rxEventBus.filterObservableAndSetThread<ServerErrorMessage>(subscribeThread = Schedulers.io())
            .subscribe { isRefreshing.value = false })

        compositeDisposable.add(rxEventBus.filterObservableAndSetThread<BlogsRefreshDoneMessage>()
            .subscribe {
                isRefreshing.value = false
                requestToLoadFeedsFromServers()
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

        requestToLoadFeedsFromServers()
    }

    fun requestToLoadFeedsFromServers(forceRefresh: Boolean = false) {
        if (isRefreshing.value == null || isRefreshing.value == false) {
            isRefreshing.postValue(true)

            doAsync {
                if (forceRefresh) {
                    if (!persistenceManager.isBlogsExist()) {
                        compositeDisposable.add(server.loadBlogs())
                    } else {
                        compositeDisposable.add(server.loadAllNewsFromAllActiveBlogs())
                    }
                } else {
                    if (persistenceManager.isBlogsExist()) {
                        if (persistenceManager.getLastAutomaticNewsUpdateLocalDate().get().dayOfMonth != LocalDate.now().dayOfMonth ||
                            persistenceManager.getCountOfNews() == 0
                        ) {
                            compositeDisposable.add(server.loadAllNewsFromAllActiveBlogs())
                        } else {
                            isRefreshing.postValue(false)
                        }
                    } else {
                        compositeDisposable.add(server.loadBlogs())
                    }
                }
            }
        }
    }

    fun performSearch(searchQuery: String) {

        this.searchQuery = searchQuery

        val query = searchQuery.toLowerCase().trim()

        searchedNewsList.postValue(newsList.value?.filter { pair ->
            pair.first.title?.contains(query, true) ?: false ||
                pair.second.name.contains(query, true)
        })
    }
}