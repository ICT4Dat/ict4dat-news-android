package at.ict4d.ict4dnews.screens.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import at.ict4d.ict4dnews.BuildConfig
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.lifecycle.LeakCanaryLifecycleObserver
import at.ict4d.ict4dnews.lifecycle.RXLifecycleObserver
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

abstract class BaseFragment<V : ViewModel, B : ViewDataBinding> : Fragment(), HasSupportFragmentInjector,
    NavController.OnNavigatedListener {

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

    /**
     * Some fragments don't have toolbar such as {@link at.ict4d.ict4dnews.screens.ict4d.ict4dat.ICT4DatFragment}
     */
    abstract fun isFragmentContainToolbar(): Boolean

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
        model = ViewModelProviders.of(this, viewModelFactory).get(getViewModel())
        lifecycle.addObserver(RXLifecycleObserver(compositeDisposable))

        if (BuildConfig.DEBUG) {
            activity?.let { lifecycle.addObserver(LeakCanaryLifecycleObserver(it, this)) }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        findNavController().addOnNavigatedListener(this)

        return binding.root
    }

    /**
     * @see HasSupportFragmentInjector
     */
    override fun supportFragmentInjector() = childFragmentInjector

    override fun onNavigated(controller: NavController, destination: NavDestination) {
        if (activity is AppCompatActivity && isFragmentContainToolbar()) {
            val appCompatActivity: AppCompatActivity
            try {
                appCompatActivity = activity as AppCompatActivity
            } catch (exception: Exception) {
                Timber.e("Activity is not of AppCompactActivity Type ${exception.message}")
                throw IllegalStateException("Activity is not of AppCompactActivity Type")
            }
            appCompatActivity.setSupportActionBar(binding.root.findViewById(R.id.toolbar))

            // resets subtitle of Toolbar
            if (destination.id != R.id.blogAndSourceFragment) {
                appCompatActivity.supportActionBar?.subtitle = ""
            }

            if (destination.id != R.id.splashFragment && destination.id != R.id.actionNews) {
                appCompatActivity.setupActionBarWithNavController(controller)
                controller.graph.startDestination = R.id.actionNews
            }

            if (destination.id == R.id.splashFragment) {
                appCompatActivity.supportActionBar?.hide()
            } else {
                appCompatActivity.supportActionBar?.show()
            }
        } else {
            Timber.e("Activity is not of type AppCompact")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        findNavController().removeOnNavigatedListener(this)
    }
}