package at.ict4d.ict4dnews.screens.news.blogandsource

import android.arch.lifecycle.LiveData
import at.ict4d.ict4dnews.models.Blog
import at.ict4d.ict4dnews.persistence.database.dao.BlogDao
import at.ict4d.ict4dnews.screens.base.BaseViewModel
import org.jetbrains.anko.doAsync
import javax.inject.Inject

class BlogAndSourceViewModel @Inject constructor(private val blogDao: BlogDao) : BaseViewModel() {
    var allBlogsList: LiveData<List<Blog>> = blogDao.getAll()


    fun updateBlogActiveStatus(blog: Blog) {
        doAsync { blogDao.updateBlogActiveFlag(blog) }
    }
}