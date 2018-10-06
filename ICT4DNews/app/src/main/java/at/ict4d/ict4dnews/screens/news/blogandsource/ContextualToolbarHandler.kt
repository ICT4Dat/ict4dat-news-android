package at.ict4d.ict4dnews.screens.news.blogandsource

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.view.View
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.models.Blog
import at.ict4d.ict4dnews.utils.BlogsAndSourceSubtitleUpdateMessage
import at.ict4d.ict4dnews.utils.RxEventBus
import javax.inject.Inject

class ContextualToolbarHandler @Inject constructor(private val rxEventBus: RxEventBus) {
    private var isContextualModeEnable = false
    private var selectedBlogsList: MutableList<Blog> = mutableListOf()
    private var shouldShowCheckSelected: MutableLiveData<Boolean> = MutableLiveData()

    fun isContextualModeEnable(): Boolean = isContextualModeEnable

    private fun addBlogToContextualList(blog: Blog) {

        if (selectedBlogsList.contains(blog)) {
            selectedBlogsList.remove(blog)
        } else {
            selectedBlogsList.add(blog)
        }

        if (selectedBlogsList.size == 0) {
            disableContextualMode()
        } else {
            shouldShowCheckSelected.value = !blog.active
        }

        rxEventBus.post(BlogsAndSourceSubtitleUpdateMessage(selectedBlogsList.size))
    }

    private fun enableContextualMode() {
        isContextualModeEnable = true
    }

    fun disableContextualMode() {
        isContextualModeEnable = false
        shouldShowCheckSelected.value = null
        selectedBlogsList.clear()
        rxEventBus.post(BlogsAndSourceSubtitleUpdateMessage(selectedBlogsList.size))
    }

    private fun isContextualListEmpty(): Boolean = selectedBlogsList.isEmpty()

    fun getContextualList(): MutableList<Blog> = selectedBlogsList

    fun shouldShowCheckSelected(): LiveData<Boolean> = shouldShowCheckSelected

    fun isBlogAlreadySelected(blog: Blog): Boolean = selectedBlogsList.contains(blog)

    private fun addBackgroundToSelectedBlogs(blog: Blog, parentView: View) {
        if (!isBlogAlreadySelected(blog)) {
            if (parentView is CardView) {
                parentView.setCardBackgroundColor(ContextCompat.getColor(parentView.context, R.color.light_grey))
            } else {
                parentView.setBackgroundColor(ContextCompat.getColor(parentView.context, R.color.light_grey))
            }
        } else {
            if (parentView is CardView) {
                parentView.setCardBackgroundColor(ContextCompat.getColor(parentView.context, R.color.white))
            } else {
                parentView.setBackgroundColor(ContextCompat.getColor(parentView.context, R.color.light_grey))
            }
        }
    }

    fun handleContextualRequest(blog: Blog, parentView: View, isLongClickRequest: Boolean = false) {
        if (!isContextualModeEnable()) {
            enableContextualMode()
        }

        if (isLongClickRequest && isContextualListEmpty()) { // first long click to enable contextual
            addBackgroundToSelectedBlogs(blog, parentView)
            addBlogToContextualList(blog)
        } else if (!isLongClickRequest) { // condition for normal click after contextual mode enable
            addBackgroundToSelectedBlogs(blog, parentView)
            addBlogToContextualList(blog)
        }
    }
}