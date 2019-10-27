package at.ict4d.ict4dnews.screens.util

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ScrollToTopRecyclerViewScrollHandler(private val quickMoveView: View) : RecyclerView.OnScrollListener() {

    private var swipeUpTime = 0L
    private val visibleItemsRequireToShowQuickMoveView = 5
    private val switchEventTimeThresholdDuration = 200 // ms

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val position = layoutManager.findFirstCompletelyVisibleItemPosition()
        if (position >= visibleItemsRequireToShowQuickMoveView && dy < 0) {
            swipeUpTime = System.currentTimeMillis()

            if (quickMoveView is FloatingActionButton) {
                quickMoveView.show()
            } else {
                quickMoveView.visibility = View.VISIBLE
            }
        } else {
            if (System.currentTimeMillis() - swipeUpTime >= switchEventTimeThresholdDuration) {
                if (quickMoveView is FloatingActionButton) {
                    quickMoveView.hide()
                } else {
                    quickMoveView.visibility = View.GONE
                }
            } else {
                if (position < visibleItemsRequireToShowQuickMoveView) {
                    if (quickMoveView is FloatingActionButton) {
                        quickMoveView.hide()
                    } else {
                        quickMoveView.visibility = View.GONE
                    }
                }
            }
        }
    }
}
