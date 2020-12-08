package at.ict4d.ict4dnews.screens.news.blogandsource

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import at.ict4d.ict4dnews.extensions.trigger
import at.ict4d.ict4dnews.models.Blog
import at.ict4d.ict4dnews.server.repositories.BlogsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BlogAndSourceViewModel(
    private val blogsRepository: BlogsRepository
) : ViewModel() {

    private val trigger = MutableLiveData<Boolean>().apply { value = false }
    val allBlogsList = trigger.switchMap { blogsRepository.getAllBlogs().asLiveData() }

    fun updateBlogActiveStatus(blog: Blog) {
        viewModelScope.launch(Dispatchers.IO) {
            blogsRepository.updateBlog(blog)
        }
    }

    fun refreshBlogs() = trigger.trigger()
}
