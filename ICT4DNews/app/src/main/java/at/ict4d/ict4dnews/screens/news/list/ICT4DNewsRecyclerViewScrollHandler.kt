package at.ict4d.ict4dnews.screens.news.list

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View

class ICT4DNewsRecyclerViewScrollHandler(private val quickMoveView: View) : RecyclerView.OnScrollListener() {
    private var swipeUpTime = 0L
    private val visibleItemsRequireToShowQuickMoveView = 5
    private val switchEventTimeThresholdDuration = 200 // ms

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        recyclerView?.let {
            val layoutManager = it.layoutManager as LinearLayoutManager
            val position = layoutManager.findFirstCompletelyVisibleItemPosition()
            if (position >= visibleItemsRequireToShowQuickMoveView && dy < 0) {
                swipeUpTime = System.currentTimeMillis()
                quickMoveView.visibility = View.VISIBLE
            } else {
                if (System.currentTimeMillis() - swipeUpTime >= switchEventTimeThresholdDuration) {
                    quickMoveView.visibility = View.GONE
                } else {
                    if (position < visibleItemsRequireToShowQuickMoveView) {
                        quickMoveView.visibility = View.GONE
                    }
                }
            }
        }
    }
}