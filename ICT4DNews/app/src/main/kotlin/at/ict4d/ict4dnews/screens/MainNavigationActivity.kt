package at.ict4d.ict4dnews.screens

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.ui.setupWithNavController
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.ActivityMainNavigationBinding
import at.ict4d.ict4dnews.lifecycle.RXErrorEventBusLifecycleObserver
import at.ict4d.ict4dnews.lifecycle.SentryLifecycleObserver
import at.ict4d.ict4dnews.utils.RxEventBus
import at.ict4d.ict4dnews.utils.recordNavigationBreadcrumb
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main_navigation.*
import javax.inject.Inject

class MainNavigationActivity : DaggerAppCompatActivity(), NavController.OnDestinationChangedListener {

    private lateinit var binding: ActivityMainNavigationBinding

    private lateinit var model: MainNavigationViewModel

    @Inject
    lateinit var compositeDisposable: CompositeDisposable

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var rxEventBus: RxEventBus

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_navigation)
        model = ViewModelProviders.of(this, viewModelFactory).get(MainNavigationViewModel::class.java)

        lifecycle.addObserver(RXErrorEventBusLifecycleObserver(this, compositeDisposable, rxEventBus))
        lifecycle.addObserver(SentryLifecycleObserver(this))

        val navController = findNavController(navHostController)
        binding.navigation.setupWithNavController(navController)
        navController.addOnDestinationChangedListener(this)
    }

    override fun onSupportNavigateUp() = findNavController(navHostController).navigateUp()

    override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
        if (destination.id == R.id.splashFragment) {
            binding.navigation.visibility = View.GONE
        } else {
            binding.navigation.visibility = View.VISIBLE
        }
    }

    override fun onBackPressed() {
        recordNavigationBreadcrumb("onBackPressed", this)
        super.onBackPressed()
    }
}