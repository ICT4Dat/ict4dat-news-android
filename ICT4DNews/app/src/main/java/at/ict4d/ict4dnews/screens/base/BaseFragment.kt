package at.ict4d.ict4dnews.screens.base

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import at.ict4d.ict4dnews.lifecycle.RXLifecycleObserver
import io.reactivex.disposables.CompositeDisposable

abstract class BaseFragment<V : ViewModel, B : ViewDataBinding>: Fragment() {

    protected lateinit var binding: B

    protected lateinit var model: V

    protected val compositeDisposable: CompositeDisposable = CompositeDisposable()

    /**
     * return the layout id associated with the Activity
     */
    @StringRes
    abstract fun getToolbarTitleResId(): Int

    /**
     * return the layout id associated with the Activity
     */
    @LayoutRes
    abstract fun getLayoutId(): Int

    /**
     * Set class of ViewModel
     */
    abstract fun getViewModel(): Class<V>

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is AppCompatActivity) {
            model = ViewModelProviders.of(context).get(getViewModel())
        }
        lifecycle.addObserver(RXLifecycleObserver(compositeDisposable))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)

        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.title = getString(getToolbarTitleResId())

        return binding.root
    }
}