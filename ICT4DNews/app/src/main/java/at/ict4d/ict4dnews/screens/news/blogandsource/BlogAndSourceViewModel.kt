package at.ict4d.ict4dnews.screens.news.blogandsource

import android.arch.lifecycle.LiveData
import at.ict4d.ict4dnews.models.Blog
import at.ict4d.ict4dnews.persistence.IPersistenceManager
import at.ict4d.ict4dnews.screens.base.BaseViewModel
import org.jetbrains.anko.doAsync
import javax.inject.Inject

class BlogAndSourceViewModel @Inject constructor(
    private val persistenceManager: IPersistenceManager
) : BaseViewModel() {

    var allBlogsList: LiveData<List<Blog>> = persistenceManager.getAllBlogs()

    fun updateBlogActiveStatus(blog: Blog) {
        doAsync { persistenceManager.updateBlog(blog) }
    }
}