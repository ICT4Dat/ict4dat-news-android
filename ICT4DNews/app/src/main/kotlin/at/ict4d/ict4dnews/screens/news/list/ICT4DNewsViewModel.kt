package at.ict4d.ict4dnews.screens.news.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import at.ict4d.ict4dnews.persistence.sharedpreferences.SharedPrefs
import at.ict4d.ict4dnews.server.repositories.BlogsRepository
import at.ict4d.ict4dnews.server.repositories.NewsRepository
import at.ict4d.ict4dnews.server.utils.NewsUpdateResource
import at.ict4d.ict4dnews.server.utils.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate

class ICT4DNewsViewModel(
    private val blogsRepository: BlogsRepository,
    private val newsRepository: NewsRepository,
    sharedPrefs: SharedPrefs
) : ViewModel() {

    private val mutableNewsUpdateStatus = MutableLiveData<NewsUpdateResource>()
    val newsUpdateStatus: LiveData<NewsUpdateResource> = mutableNewsUpdateStatus

    val blogsCount = blogsRepository.getBlogsCount()
    val activeBlogsCount = blogsRepository.getActiveBlogsCount().asLiveData()

    var isWelcomeSetupNotStartedOnce = true
    var shouldMoveScrollToTop: Boolean = false

    private val mutableSearchQuery = MutableLiveData<String>().apply { value = "" }
    val searchQuery: LiveData<String> = mutableSearchQuery

    val newsList = mutableSearchQuery.switchMap {
        newsRepository.getAllActiveNews(it)
            .map { newsList -> newsList.map { news -> Pair(news, blogsRepository.getBlogByUrl(news.blogID ?: "").first()) } }
            .asLiveData(Dispatchers.IO)
    }

    val lastAutomaticNewsUpdateLocalDate = sharedPrefs.lastAutomaticNewsUpdateLocalDate

    init {
        requestToLoadFeedsFromServers()
    }

    fun requestToLoadFeedsFromServers(forceRefresh: Boolean = false) {
        if (!isNewsUpdateLoading()) {
            viewModelScope.launch {
                if (forceRefresh) {
                    requestToLoadNews()
                } else {
                    if (isLastNewsUpdateIsOld()) {
                        requestToLoadNews()
                    }
                }
            }
        }
    }

    fun isNewsUpdateLoading(): Boolean {
        val value = newsUpdateStatus.value
        return if (value == null) {
            false
        } else {
            value.status == Status.LOADING
        }
    }

    private suspend fun requestToLoadNews() {
        if (!blogsRepository.doBlogsExists().first()) {
            blogsRepository.getAllBlogs().collect {
                if (it.status == Status.SUCCESS) {
                    requestToLoadNews()
                }
            }
        } else {
            newsRepository.updateAllActiveNewsWithFlow().collect { newsUpdateResource ->
                mutableNewsUpdateStatus.postValue(newsUpdateResource)
            }
        }
    }

    private suspend fun isLastNewsUpdateIsOld(): Boolean {
        return lastAutomaticNewsUpdateLocalDate.get().dayOfMonth != LocalDate.now().dayOfMonth ||
            newsRepository.getCountOfNews().first() == 0
    }

    fun performSearch(searchQuery: String) {
        this.mutableSearchQuery.postValue(searchQuery)
    }
}
