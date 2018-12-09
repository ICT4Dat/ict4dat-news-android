package at.ict4d.ict4dnews.screens.news.list

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.models.Blog
import at.ict4d.ict4dnews.models.News
import at.ict4d.ict4dnews.persistence.IPersistenceManager
import at.ict4d.ict4dnews.screens.base.BaseViewModel
import at.ict4d.ict4dnews.server.IServer
import at.ict4d.ict4dnews.utils.BlogsRefreshDoneMessage
import at.ict4d.ict4dnews.utils.NewsRefreshDoneMessage
import at.ict4d.ict4dnews.utils.OnceEvent
import at.ict4d.ict4dnews.utils.RxEventBus
import at.ict4d.ict4dnews.utils.ServerErrorMessage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Flowables
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import org.threeten.bp.LocalDate
import timber.log.Timber
import javax.inject.Inject

class ICT4DNewsViewModel @Inject constructor(
    private val persistenceManager: IPersistenceManager,
    private val server: IServer,
    rxEventBus: RxEventBus
) : BaseViewModel() {

    private val _newsList = MutableLiveData<List<Pair<News, Blog>>>()
    private val _searchedNewsList = MutableLiveData<List<Pair<News, Blog>>>()
    private val _activeBlogsCount: MutableLiveData<OnceEvent<Int>> = MutableLiveData()
    var searchQuery: String? = null

    val newsList: LiveData<List<Pair<News, Blog>>>
        get() = _newsList
    val searchedNewsList: LiveData<List<Pair<News, Blog>>>
        get() = _searchedNewsList
    val activeBlogsCount: LiveData<OnceEvent<Int>>
        get() = _activeBlogsCount

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
                _newsList.postValue(resultList)
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

    private fun getActiveBlogsCount() {
        compositeDisposable.add(persistenceManager.getActiveBlogsCount().subscribeOn(Schedulers.io()).subscribe({
            _activeBlogsCount.postValue(OnceEvent(it))
        }, {
            Timber.e(it)
        }))
    }

    fun requestToLoadFeedsFromServers(forceRefresh: Boolean = false) {
        if (isRefreshing.value == null || isRefreshing.value == false) {
            isRefreshing.postValue(true)
            getActiveBlogsCount()

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

        _searchedNewsList.postValue(_newsList.value?.filter { pair ->
            pair.first.title?.contains(query, true) ?: false ||
                pair.second.name.contains(query, true)
        })
    }

    fun getNewsLoadingText(blogCount: Int, resources: Resources): String {
        return if (blogCount == 0) {
            resources.getString(R.string.no_blog_found)
        } else {
            String.format(resources.getString(R.string.connecting_text), blogCount)
        }
    }
}