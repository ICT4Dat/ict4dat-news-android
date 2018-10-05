package at.ict4d.ict4dnews.screens.news.blogandsource

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import at.ict4d.ict4dnews.models.Blog
import javax.inject.Inject

class ContextualToolbarHandler @Inject constructor() {
    private var isContextualModeEnable = false
    private var selectedBlogsList: MutableLiveData<MutableList<Blog>> = MutableLiveData()
    private var shouldShowCheckSelected: MutableLiveData<Boolean> = MutableLiveData()

    fun isContextualModeEnable(): Boolean = isContextualModeEnable

    init {
        selectedBlogsList.value = mutableListOf()
    }

    fun addBlogToContextualList(blog: Blog) {
        val contextualList = selectedBlogsList.value
        contextualList?.let {
            if (it.contains(blog)) {
                it.remove(blog)
            } else {
                it.add(blog)
            }

            if (it.size == 0) {
                disableContextualMode()
            } else {
                shouldShowCheckSelected.value = !blog.active
                selectedBlogsList.value = contextualList
            }
        }
    }

    private fun clearContextualBlogsList() {
        val contextualList = selectedBlogsList.value
        contextualList?.clear()
        selectedBlogsList.value = contextualList
    }

    fun enableContextualMode() {
        isContextualModeEnable = true
    }

    fun disableContextualMode() {
        isContextualModeEnable = false
        shouldShowCheckSelected.value = null
        clearContextualBlogsList()
    }

    fun isContextualListEmpty(): Boolean = selectedBlogsList.value?.isEmpty() == true

    fun getContextualList(): LiveData<MutableList<Blog>> = selectedBlogsList

    fun shouldShowCheckSelected(): LiveData<Boolean> = shouldShowCheckSelected
}