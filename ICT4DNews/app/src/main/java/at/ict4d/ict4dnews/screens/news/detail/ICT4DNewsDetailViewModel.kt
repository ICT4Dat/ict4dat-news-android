package at.ict4d.ict4dnews.screens.news.detail

import android.arch.lifecycle.LiveData
import at.ict4d.ict4dnews.models.Author
import at.ict4d.ict4dnews.persistence.IPersistenceManager
import at.ict4d.ict4dnews.screens.base.BaseViewModel
import javax.inject.Inject

class ICT4DNewsDetailViewModel @Inject constructor(private val persistenceManager: IPersistenceManager) :
        BaseViewModel() {

    fun loadAuthorDetails(authorId: String): LiveData<Author> = persistenceManager.getAuthorBy(authorId)
}