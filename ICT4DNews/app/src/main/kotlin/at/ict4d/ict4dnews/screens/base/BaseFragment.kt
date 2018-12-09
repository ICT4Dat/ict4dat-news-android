package at.ict4d.ict4dnews.screens.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import at.ict4d.ict4dnews.BuildConfig
import at.ict4d.ict4dnews.lifecycle.LeakCanaryLifecycleObserver
import at.ict4d.ict4dnews.lifecycle.RXLifecycleObserver
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
        model = ViewModelProviders.of(this, viewModelFactory).get(getViewModel())
        lifecycle.addObserver(RXLifecycleObserver(compositeDisposable))

        if (BuildConfig.DEBUG) {
            activity?.let {
                lifecycle.addObserver(LeakCanaryLifecycleObserver(it, this))
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)

        return binding.root
    }

    /**
     * @see HasSupportFragmentInjector
     */
    override fun supportFragmentInjector() = childFragmentInjector
}