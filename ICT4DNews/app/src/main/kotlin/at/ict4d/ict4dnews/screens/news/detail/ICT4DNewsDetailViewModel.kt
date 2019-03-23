package at.ict4d.ict4dnews.screens.news.detail

import androidx.lifecycle.LiveData
import at.ict4d.ict4dnews.models.Author
import at.ict4d.ict4dnews.models.Blog
import at.ict4d.ict4dnews.models.News
import at.ict4d.ict4dnews.persistence.IPersistenceManager
import at.ict4d.ict4dnews.screens.base.BaseViewModel
import javax.inject.Inject

class ICT4DNewsDetailViewModel @Inject constructor(private val persistenceManager: IPersistenceManager) :
    BaseViewModel() {

    var selectedNews: News? = null

    fun authorDetails(authorId: String): LiveData<Author> = persistenceManager.getAuthorBy(authorId)
    fun getBlogBy(url: String): LiveData<Blog> = persistenceManager.getBlogByUrlAsLiveData(url)
}
