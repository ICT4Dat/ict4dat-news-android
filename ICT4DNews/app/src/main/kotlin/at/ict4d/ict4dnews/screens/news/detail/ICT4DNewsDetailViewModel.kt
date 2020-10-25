package at.ict4d.ict4dnews.screens.news.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import at.ict4d.ict4dnews.models.News
import at.ict4d.ict4dnews.screens.base.BaseViewModel
import at.ict4d.ict4dnews.server.repositories.AuthorRepository
import at.ict4d.ict4dnews.server.repositories.BlogsRepository

class ICT4DNewsDetailViewModel(
    private val authorRepository: AuthorRepository,
    private val blogsRepository: BlogsRepository
) : BaseViewModel() {

    private val news = MutableLiveData<News>()

    val author = news.switchMap {
        authorRepository.getAuthorBy(it.authorID ?: "").asLiveData()
    }
    val blog = news.switchMap { blogsRepository.getBlogByUrl(it.blogID ?: "").asLiveData() }

    fun setUp(news: News) {
        this.news.value = news
    }

    fun getNews() = news.value
}
