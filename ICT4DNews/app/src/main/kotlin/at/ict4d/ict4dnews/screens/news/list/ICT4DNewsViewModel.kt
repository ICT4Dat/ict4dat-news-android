package at.ict4d.ict4dnews.screens.news.list

import androidx.lifecycle.MutableLiveData
import at.ict4d.ict4dnews.models.Blog
import at.ict4d.ict4dnews.models.News
import at.ict4d.ict4dnews.persistence.IPersistenceManager
import at.ict4d.ict4dnews.screens.base.BaseViewModel
import at.ict4d.ict4dnews.server.IServer
import at.ict4d.ict4dnews.utils.BlogsRefreshDoneMessage
import at.ict4d.ict4dnews.utils.NewsRefreshDoneMessage
import at.ict4d.ict4dnews.utils.RxEventBus
import at.ict4d.ict4dnews.utils.ServerErrorMessage
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Flowables
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import timber.log.Timber
import javax.inject.Inject

class ICT4DNewsViewModel @Inject constructor(
    private val persistenceManager: IPersistenceManager,
    private val server: IServer,
    rxEventBus: RxEventBus
) : BaseViewModel() {

    val blogsCount = persistenceManager.getBlogsCountAsLiveData()
    val activeBlogsCount = persistenceManager.getActiveBlogsCountAsLiveData()
    var isSplashNotStartedOnce = true
    var searchQuery: String? = null
    var shouldMoveScrollToTop: Boolean = false

    val newsList: MutableLiveData<List<Pair<News, Blog>>> = MutableLiveData()
    val searchedNewsList: MutableLiveData<List<Pair<News, Blog>>> = MutableLiveData()

    private val existingNews: ArrayList<News> = arrayListOf()
    val mostRecentPublishedNewsDateTimeLiveData: MutableLiveData<LocalDateTime> = MutableLiveData()

    init {

        compositeDisposable.add(rxEventBus.filteredObservable(NewsRefreshDoneMessage::class.java)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                isRefreshing.value = false
                shouldMoveScrollToTop = true
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

        compositeDisposable.add(
            rxEventBus.filteredObservable(BlogsRefreshDoneMessage::class.java).observeOn(
                AndroidSchedulers.mainThread()
            ).subscribeOn(AndroidSchedulers.mainThread()).subscribe {
                isRefreshing.value = false
                requestToLoadFeedsFromServers()
            })

        requestToLoadFeedsFromServers()
    }

    fun requestToLoadFeedsFromServers(forceRefresh: Boolean = false) {
        if (isRefreshing.value == null || isRefreshing.value == false) {
            getMostRecentPublishedNews()
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
        if (!persistenceManager.isBlogsExist()) {
            compositeDisposable.add(server.loadBlogs())
        } else {
            if (isRefreshing.value == false) {
                compositeDisposable.add(server.loadAllNewsFromAllActiveBlogs())
            }
        }
    }

    private fun isLastNewsUpdateIsOld(): Boolean {
        return persistenceManager.getLastAutomaticNewsUpdateLocalDate().get().dayOfMonth != LocalDate.now().dayOfMonth ||
            persistenceManager.getCountOfNews() == 0
    }

    fun performSearch(searchQuery: String?) {
        this.searchQuery = if (searchQuery == null || searchQuery.trim().isEmpty()) {
            null
        } else {
            searchQuery.toLowerCase().trim()
        }

        val list = newsList.value?.filter { pair ->
            pair.first.title?.contains(this.searchQuery ?: "", true) ?: false ||
                pair.second.name.contains(this.searchQuery ?: "", true)
        }
        searchedNewsList.postValue(
            if (list?.isEmpty() == true) {
                null
            } else {
                list
            }
        )
    }

    private fun getMostRecentPublishedNews() {
        existingNews.clear()
        compositeDisposable.add(Single.fromCallable { persistenceManager.getAllActiveNewsAsList() }
            .subscribeOn(Schedulers.io())
            .subscribe({
                if (it != null && existingNews.isEmpty()) {
                    existingNews.addAll(it)
                    val temp = existingNews.maxBy { it.publishedDate ?: LocalDateTime.now() }
                    mostRecentPublishedNewsDateTimeLiveData.postValue(temp?.publishedDate)
                }
            }, { Timber.e("Something went wrong while getting the most younger news ---> ${it.message}") })
        )
    }
}