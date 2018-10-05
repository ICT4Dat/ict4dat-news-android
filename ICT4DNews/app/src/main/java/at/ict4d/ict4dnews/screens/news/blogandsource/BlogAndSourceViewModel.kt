package at.ict4d.ict4dnews.screens.news.blogandsource

import android.arch.lifecycle.LiveData
import at.ict4d.ict4dnews.models.Blog
import at.ict4d.ict4dnews.persistence.database.dao.BlogDao
import at.ict4d.ict4dnews.screens.base.BaseViewModel
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import timber.log.Timber
import javax.inject.Inject

class BlogAndSourceViewModel @Inject constructor(
    private val blogDao: BlogDao,
    private val contextualToolbarHandler: ContextualToolbarHandler
) : BaseViewModel() {
    var allBlogsList: LiveData<List<Blog>> = blogDao.getAll()

    fun updateBlogActiveStatus(blog: Blog) {
        doAsync { blogDao.updateBlogActiveFlag(blog) }
    }

    fun updateBlogsActiveStatus(blogList: List<Blog>) {
        val shouldCheck = contextualToolbarHandler.shouldShowCheckSelected().value
        shouldCheck?.let { blogList.map { blog -> blog.active = it } }

        compositeDisposable.add(Completable.fromAction {
            blogDao.insertAll(blogList)
            allBlogsList = blogDao.getAll()
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe { contextualToolbarHandler.disableContextualMode() })
    }

    /*CONTEXTUAL RELATED*/
    fun handleContextualRequest(blog: Blog, isLongClickRequest: Boolean = false) {
        if (!contextualToolbarHandler.isContextualModeEnable()) {
            Timber.d("XXX:Going to enable contextual request")
            contextualToolbarHandler.enableContextualMode()
        }

        if (isLongClickRequest && contextualToolbarHandler.isContextualListEmpty()) { // first long click to enable contextual
            contextualToolbarHandler.addBlogToContextualList(blog)
        } else if (!isLongClickRequest) { // condition for normal click after contextual mode enable
            contextualToolbarHandler.addBlogToContextualList(blog)
        }
    }

    fun isContextualRequestEnable(): Boolean = contextualToolbarHandler.isContextualModeEnable()

    fun getContextualBlogsLiveData() = contextualToolbarHandler.getContextualList()

    fun shouldShowCheckSelected(): LiveData<Boolean> = contextualToolbarHandler.shouldShowCheckSelected()
}