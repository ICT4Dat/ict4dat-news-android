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
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import at.ict4d.ict4dnews.BuildConfig
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.lifecycle.LeakCanaryLifecycleObserver
import at.ict4d.ict4dnews.lifecycle.SentryLifecycleObserver
import at.ict4d.ict4dnews.screens.MainNavigationActivity
import at.ict4d.ict4dnews.utils.recordActionBreadcrumb
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import kotlin.reflect.KClass

abstract class BaseFragment<V : ViewModel, B : ViewDataBinding>(
    @LayoutRes private val layoutID: Int,
    viewModelClass: KClass<V>,
    private val hasToolbar: Boolean = true
) : Fragment(), NavController.OnDestinationChangedListener {

    protected lateinit var binding: B

    protected val model: V by viewModel(viewModelClass)

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (BuildConfig.DEBUG) {
            activity?.let { lifecycle.addObserver(LeakCanaryLifecycleObserver(it, this)) }
        }

        lifecycle.addObserver(SentryLifecycleObserver(this))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutID, container, false)
        findNavController().addOnDestinationChangedListener(this)

        return binding.root
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        if (activity is AppCompatActivity && hasToolbar) {
            val appCompatActivity: AppCompatActivity
            try {
                appCompatActivity = activity as AppCompatActivity
            } catch (exception: Exception) {
                Timber.e(
                    exception,
                    "Activity is not of AppCompactActivity Type ${exception.message}"
                )
                throw IllegalStateException("Activity is not of AppCompactActivity Type")
            }
            appCompatActivity.setSupportActionBar(binding.root.findViewById(R.id.toolbar))
            appCompatActivity.setupActionBarWithNavController(controller, (requireActivity() as MainNavigationActivity).appBarConfiguration)
        } else {
            Timber.w("Activity is not of type AppCompact or Fragment has no Toolbar")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        findNavController().removeOnDestinationChangedListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        recordActionBreadcrumb("onOptionsItemSelected", this, mapOf("item" to "${item.title}"))
        return super.onOptionsItemSelected(item)
    }
}
