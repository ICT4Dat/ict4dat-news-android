package at.ict4d.ict4dnews.screens.news.list

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
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
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import org.threeten.bp.LocalDate

class ICT4DNewsViewModel(
    private val persistenceManager: IPersistenceManager,
    private val server: IServer,
    pagedListConfig: PagedList.Config,
    rxEventBus: RxEventBus
) : BaseViewModel() {

    val blogsCount = persistenceManager.getBlogsCountAsLiveData()
    val activeBlogsCount = persistenceManager.getActiveBlogsCountAsLiveData()
    var isSplashNotStartedOnce = true
    var shouldMoveScrollToTop: Boolean = false

    var searchQuery: String = ""
    private val newsSearchDataSourceFactory: NewsSearchDataSourceFactory = NewsSearchDataSourceFactory()
    val newsList: LiveData<PagedList<Pair<News, Blog?>>> =
        LivePagedListBuilder(newsSearchDataSourceFactory, pagedListConfig).build()

    val lastAutomaticNewsUpdateLocalDate = persistenceManager.getLastAutomaticNewsUpdateLocalDate()

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

        requestToLoadFeedsFromServers()
    }

    fun requestToLoadFeedsFromServers(forceRefresh: Boolean = false) {
        if (isRefreshing.value == null || isRefreshing.value == false) {
            isRefreshing.postValue(true)

            doAsync {
                if (forceRefresh) {
                    requestToLoadNews()
                } else {
                    if (isLastNewsUpdateIsOld()) {
                        requestToLoadNews()
                    } else {
                        isRefreshing.postValue(false)
                    }
                }
            }
        }
    }

    private fun requestToLoadNews() {
        if (isRefreshing.value == true) {
            return
        }
        if (!persistenceManager.isBlogsExist()) {
            compositeDisposable.add(server.loadBlogs())
        } else {
            compositeDisposable.add(server.loadAllNewsFromAllActiveBlogs())
        }
    }

    private fun isLastNewsUpdateIsOld(): Boolean {
        return lastAutomaticNewsUpdateLocalDate.get().dayOfMonth != LocalDate.now().dayOfMonth ||
            persistenceManager.getCountOfNews() == 0
    }

    fun performSearch(searchQuery: String) {
        this.searchQuery = searchQuery
        newsSearchDataSourceFactory.query = searchQuery
        newsList.value?.dataSource?.invalidate()
    }

    inner class NewsSearchDataSourceFactory : DataSource.Factory<Int, Pair<News, Blog?>>() {

        var query: String = ""

        override fun create(): DataSource<Int, Pair<News, Blog?>> {
            return persistenceManager.getAllActiveNews(query).map { news ->
                if (news.blogID == null) {
                    Pair(news, persistenceManager.getBlogByUrl(""))
                } else {
                    Pair(news, persistenceManager.getBlogByUrl(news.blogID))
                }
            }.create()
        }
    }
}
