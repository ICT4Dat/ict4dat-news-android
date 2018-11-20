package at.ict4d.ict4dnews.screens.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.lifecycle.RXErrorEventBusLifecycleObserver
import at.ict4d.ict4dnews.utils.RxEventBus
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

abstract class BaseActivity<V : ViewModel, B : ViewDataBinding> : AppCompatActivity(), HasSupportFragmentInjector {

    protected lateinit var binding: B

    protected lateinit var model: V

    @Inject
    protected lateinit var compositeDisposable: CompositeDisposable

    @Inject
    protected lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    protected lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    protected lateinit var rxEventBus: RxEventBus

    /**
     * return the layout id associated with the Activity
     */
    @LayoutRes
    abstract fun getLayoutId(): Int

    /**
     * Set class of ViewModel
     */
    abstract fun getViewModel(): Class<V>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, getLayoutId())
        model = ViewModelProviders.of(this, viewModelFactory).get(getViewModel())

        setSupportActionBar(binding.root.findViewById(R.id.toolbar))
        lifecycle.addObserver(RXErrorEventBusLifecycleObserver(this, compositeDisposable, rxEventBus))
    }

    override fun supportFragmentInjector() = fragmentInjector
}