package at.ict4d.ict4dnews.screens.news.blogandsource

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.res.Resources
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.models.Blog
import at.ict4d.ict4dnews.persistence.database.dao.BlogDao
import at.ict4d.ict4dnews.screens.base.BaseViewModel
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import javax.inject.Inject

class BlogAndSourceViewModel @Inject constructor(
    private val blogDao: BlogDao,
    private val contextualToolbarHandler: ContextualToolbarHandler
) : BaseViewModel() {
    var allBlogsList: LiveData<List<Blog>> = blogDao.getAll()
    var contextualMenuSubtitle: MutableLiveData<String> = MutableLiveData()

    fun updateBlogActiveStatus(blog: Blog) {
        doAsync { blogDao.updateBlogActiveFlag(blog) }
    }

    fun updateBlogsActiveStatus(blogList: List<Blog>) {
        val shouldCheck = contextualToolbarHandler.shouldShowCheckSelected().value
        shouldCheck?.let { blogList.map { blog -> blog.active = it } }

        compositeDisposable.add(Completable.fromAction {
            blogDao.insertAll(blogList)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe { contextualToolbarHandler.disableContextualMode() })
    }

    fun createSubtitleForContextualMenu(selectedBlogs: Int, resources: Resources) {
        contextualMenuSubtitle.value = if (selectedBlogs > 0) {
            String.format(
                resources.getString(R.string.contextual_selection),
                selectedBlogs,
                if (selectedBlogs == 1) "" else "s"
            )
        } else {
            null
        }
    }

    /*CONTEXTUAL RELATED*/

    fun isContextualRequestEnable(): Boolean = contextualToolbarHandler.isContextualModeEnable()

    fun getContextualBlogsLiveData(): List<Blog> = contextualToolbarHandler.getContextualList()

    fun shouldShowCheckSelected(): LiveData<Boolean> = contextualToolbarHandler.shouldShowCheckSelected()

    fun getContextualToolbarHandler() = contextualToolbarHandler
}