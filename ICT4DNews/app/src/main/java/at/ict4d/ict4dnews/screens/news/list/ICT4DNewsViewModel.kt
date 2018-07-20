package at.ict4d.ict4dnews.screens.news.list

import android.arch.lifecycle.LiveData
import at.ict4d.ict4dnews.ICT4DNewsApplication
import at.ict4d.ict4dnews.models.NewsModel
import at.ict4d.ict4dnews.persistence.IPersistenceManager
import at.ict4d.ict4dnews.screens.base.BaseViewModel
import at.ict4d.ict4dnews.server.IServer
import javax.inject.Inject

class ICT4DNewsViewModel : BaseViewModel() {

    @Inject
    protected lateinit var persistenceManager: IPersistenceManager

    @Inject
    protected lateinit var server: IServer

    val newsList: LiveData<List<NewsModel>>

    init {
        ICT4DNewsApplication.component.inject(this)
        compositeDisposable.add(server.loadICT4DatJsonFeed())
        newsList = persistenceManager.getAllNews()
    }
}