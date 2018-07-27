package at.ict4d.ict4dnews.screens.news.list

import android.arch.lifecycle.LiveData
import at.ict4d.ict4dnews.models.News
import at.ict4d.ict4dnews.persistence.IPersistenceManager
import at.ict4d.ict4dnews.screens.base.BaseViewModel
import at.ict4d.ict4dnews.server.IServer
import org.jetbrains.anko.doAsync
import javax.inject.Inject

class ICT4DNewsViewModel @Inject constructor(
        persistenceManager: IPersistenceManager,
        server: IServer) : BaseViewModel() {

    val newsList: LiveData<List<News>> = persistenceManager.getAllOrderedByPublishedDate()

    init {
        doAsync {
            compositeDisposable.add(server.loadICT4DatJsonFeed(persistenceManager.getLatestNewsPublishedDate()))
        }
    }
}