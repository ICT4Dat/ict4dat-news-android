package at.ict4d.ict4dnews.screens.base

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.lifecycle.RXErrorEventBusLifecycleObserver
import io.reactivex.disposables.CompositeDisposable

abstract class BaseActivity<B : ViewDataBinding> : AppCompatActivity() {

    protected lateinit var binding: B

    protected val compositeDisposable: CompositeDisposable = CompositeDisposable()

    /**
     * return the layout id associated with the Activity
     */
    @LayoutRes
    abstract fun getLayoutId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, getLayoutId())
        setSupportActionBar(binding.root.findViewById(R.id.toolbar))
        lifecycle.addObserver(RXErrorEventBusLifecycleObserver(this, compositeDisposable))
    }
}