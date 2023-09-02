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
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.lifecycle.SentryLifecycleObserver
import at.ict4d.ict4dnews.screens.MainNavigationActivity
import timber.log.Timber

abstract class BaseFragment<B : ViewDataBinding>(
    @LayoutRes private val layoutID: Int,
    private val hasToolbar: Boolean = true
) : Fragment(), NavController.OnDestinationChangedListener {

    protected lateinit var binding: B

    override fun onAttach(context: Context) {
        super.onAttach(context)
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
}
