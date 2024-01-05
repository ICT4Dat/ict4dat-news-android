package at.ict4d.ict4dnews.screens.welcome.setup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import at.ict4d.ict4dnews.server.repositories.BlogsRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class WelcomeSetupViewModel(
    private val blogsRepository: BlogsRepository,
) : ViewModel() {
    private val trigger = MutableLiveData<Boolean>().apply { value = false }
    val blogs = trigger.switchMap { blogsRepository.getAllBlogs().asLiveData() }

    init {
        viewModelScope.launch {
            if (!blogsRepository.doBlogsExists().first()) {
                trigger.postValue(true)
            }
        }
    }
}
