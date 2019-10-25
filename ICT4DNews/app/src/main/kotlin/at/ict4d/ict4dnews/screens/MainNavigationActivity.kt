package at.ict4d.ict4dnews.screens

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
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
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main_navigation.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainNavigationActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {

    private lateinit var binding: ActivityMainNavigationBinding

    private val model: MainNavigationViewModel by viewModel()
    private val compositeDisposable: CompositeDisposable by inject()
    private val rxEventBus: RxEventBus by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_navigation)
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