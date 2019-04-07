package at.ict4d.ict4dnews.screens.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
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
import at.ict4d.ict4dnews.lifecycle.SentryLifecycleObserver
import at.ict4d.ict4dnews.utils.recordActionBreadcrumb
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.DaggerFragment
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

abstract class BaseFragment<V : ViewModel, B : ViewDataBinding>(private val hasToolbar: Boolean = true) :
    DaggerFragment(),
    NavController.OnDestinationChangedListener {

    protected lateinit var binding: B

    protected lateinit var model: V

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
            activity?.let { lifecycle.addObserver(LeakCanaryLifecycleObserver(it, this)) }
        }

        lifecycle.addObserver(SentryLifecycleObserver(this))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        findNavController().addOnDestinationChangedListener(this)

        return binding.root
    }

    override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
        if (activity is AppCompatActivity && hasToolbar) {
            val appCompatActivity: AppCompatActivity
            try {
                appCompatActivity = activity as AppCompatActivity
            } catch (exception: Exception) {
                Timber.e(exception, "Activity is not of AppCompactActivity Type ${exception.message}")
                throw IllegalStateException("Activity is not of AppCompactActivity Type")
            }
            appCompatActivity.setSupportActionBar(binding.root.findViewById(R.id.toolbar))
            appCompatActivity.setupActionBarWithNavController(controller)
        } else {
            Timber.w("Activity is not of type AppCompact or Fragment has no Toolbar")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        findNavController().removeOnDestinationChangedListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        recordActionBreadcrumb("onOptionsItemSelected", this, mapOf("item" to "${item?.title}"))
        return super.onOptionsItemSelected(item)
    }
}