package at.ict4d.ict4dnews.screens.base

import android.arch.lifecycle.ViewModel
import android.databinding.ViewDataBinding
import android.support.v7.app.AppCompatActivity
import at.ict4d.ict4dnews.screens.MainNavigationActivity

abstract class BaseNavigationFragment<V : ViewModel, B : ViewDataBinding> : BaseFragment<V, B>() {

    override fun onResume() {
        super.onResume()
        if (activity is MainNavigationActivity) {
            val actionBar = (activity as AppCompatActivity).supportActionBar
            actionBar?.title = getString(getToolbarTitleResId())
        }
    }

    /**
     * return the layout id associated with the Activity
     */
    abstract fun getMenuItemId(): Int
}