package at.ict4d.ict4dnews.screens.news.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import at.ict4d.ict4dnews.models.Author
import at.ict4d.ict4dnews.models.Blog
import at.ict4d.ict4dnews.models.News
import at.ict4d.ict4dnews.persistence.IPersistenceManager
import at.ict4d.ict4dnews.screens.base.BaseViewModel
import at.ict4d.ict4dnews.server.repositories.BlogsRepository

class ICT4DNewsDetailViewModel(
    private val persistenceManager: IPersistenceManager,
    private val blogsRepository: BlogsRepository
) : BaseViewModel() {

    var selectedNews: News? = null

    fun authorDetails(authorId: String): LiveData<Author?> = persistenceManager.getAuthorBy(authorId)
    fun getBlogBy(url: String): LiveData<Blog?> = blogsRepository.getBlogByUrl(url).asLiveData()
}
