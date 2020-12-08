package at.ict4d.ict4dnews.screens

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.ActivityMainNavigationBinding
import at.ict4d.ict4dnews.lifecycle.SentryLifecycleObserver
import at.ict4d.ict4dnews.utils.recordNavigationBreadcrumb
import kotlinx.android.synthetic.main.activity_main_navigation.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainNavigationActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {

    private lateinit var binding: ActivityMainNavigationBinding

    private val model: MainNavigationViewModel by viewModel()

    lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_navigation)
        lifecycle.addObserver(SentryLifecycleObserver(this))

        appBarConfiguration = AppBarConfiguration.Builder(binding.navigation.menu).build()

        val navController = findNavController(navHostController)
        binding.navigation.setupWithNavController(navController)
        navController.addOnDestinationChangedListener(this)

        model.watchAutomaticNewsUpdates()
    }

    override fun onSupportNavigateUp() = findNavController(navHostController).navigateUp(appBarConfiguration)

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
