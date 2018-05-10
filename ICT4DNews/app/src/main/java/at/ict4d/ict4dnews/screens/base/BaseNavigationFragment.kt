package at.ict4d.ict4dnews.screens.base

import android.arch.lifecycle.ViewModel
import android.databinding.ViewDataBinding
import android.support.annotation.IdRes
import android.support.v7.app.AppCompatActivity
import at.ict4d.ict4dnews.screens.MainNavigationActivity
import kotlinx.android.synthetic.main.activity_main_navigation.*

abstract class BaseNavigationFragment<V : ViewModel, B : ViewDataBinding> : BaseFragment<V, B>() {

    override fun onResume() {
        super.onResume()
        if (activity is MainNavigationActivity) {
            val actionBar = (activity as AppCompatActivity).supportActionBar
            actionBar?.title = getString(getToolbarTitleResId())
            (activity as MainNavigationActivity).navigation.selectedItemId = getMenuItemId()
        }
    }

    /**
     * return the layout id associated with the Activity
     */
    @IdRes
    abstract fun getMenuItemId(): Int
}