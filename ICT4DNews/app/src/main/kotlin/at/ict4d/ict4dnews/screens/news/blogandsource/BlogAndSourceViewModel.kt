package at.ict4d.ict4dnews.screens.news.blogandsource

import androidx.lifecycle.LiveData
import at.ict4d.ict4dnews.extensions.filterObservableAndSetThread
import at.ict4d.ict4dnews.models.Blog
import at.ict4d.ict4dnews.persistence.IPersistenceManager
import at.ict4d.ict4dnews.screens.base.BaseViewModel
import at.ict4d.ict4dnews.server.IServer
import at.ict4d.ict4dnews.utils.BlogsRefreshDoneMessage
import at.ict4d.ict4dnews.utils.RxEventBus
import at.ict4d.ict4dnews.utils.ServerErrorMessage
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync

class BlogAndSourceViewModel(
    private val persistenceManager: IPersistenceManager,
    private val server: IServer,
    rxEventBus: RxEventBus
) : BaseViewModel() {

    var allBlogsList: LiveData<List<Blog>> = persistenceManager.getAllBlogs()

    init {
        compositeDisposable.add(rxEventBus.filterObservableAndSetThread<BlogsRefreshDoneMessage>(subscribeThread = Schedulers.io())
            .subscribe { isRefreshing.value = false })

        compositeDisposable.add(rxEventBus.filterObservableAndSetThread<ServerErrorMessage>(subscribeThread = Schedulers.io())
            .subscribe { isRefreshing.value = false })
    }

    fun updateBlogActiveStatus(blog: Blog) {
        doAsync { persistenceManager.updateBlog(blog) }
    }

    fun refreshBlogs() {
        if (isRefreshing.value == null || isRefreshing.value == false) {
            isRefreshing.postValue(true)
            compositeDisposable.add(server.loadBlogs())
        }
    }
}