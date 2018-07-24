package at.ict4d.ict4dnews.screens.base

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
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
import at.ict4d.ict4dnews.BuildConfig
import at.ict4d.ict4dnews.lifecycle.LeakCanaryLifecycleObserver
import at.ict4d.ict4dnews.lifecycle.RXLifecycleObserver
import at.ict4d.ict4dnews.utils.RxEventBus
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

abstract class BaseFragment<V : ViewModel, B : ViewDataBinding> : Fragment(), HasSupportFragmentInjector {

    protected lateinit var binding: B

    protected lateinit var model: V

    @Inject
    protected lateinit var childFragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    protected lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    protected lateinit var compositeDisposable: CompositeDisposable

    @Inject
    protected lateinit var rxEventBus: RxEventBus

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
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
        if (context is AppCompatActivity) {
            model = ViewModelProviders.of(context, viewModelFactory).get(getViewModel())
        }
        lifecycle.addObserver(RXLifecycleObserver(compositeDisposable))

        if (BuildConfig.DEBUG) {
            activity?.let {
                lifecycle.addObserver(LeakCanaryLifecycleObserver(it, this))
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)

        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.title = getString(getToolbarTitleResId())

        return binding.root
    }

    /**
     * @see HasSupportFragmentInjector
     */
    override fun supportFragmentInjector() = childFragmentInjector
}