package at.ict4d.ict4dnews.screens

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.ActivityMainNavigationBinding
import at.ict4d.ict4dnews.lifecycle.SentryLifecycleObserver
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainNavigationActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {

    private lateinit var binding: ActivityMainNavigationBinding

    private val model: MainNavigationViewModel by viewModel()

    val appBarConfiguration: AppBarConfiguration = AppBarConfiguration(
        topLevelDestinationIds = setOf(
            R.id.newsListFragment,
            R.id.ict4dFragment,
            R.id.moreFragment
        ),
        fallbackOnNavigateUpListener = ::onSupportNavigateUp
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_navigation)
        lifecycle.addObserver(SentryLifecycleObserver(this))

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostController) as NavHostFragment
        val navController = navHostFragment.navController
        binding.navigation.setupWithNavController(navController)
        navController.addOnDestinationChangedListener(this)

        model.watchAutomaticNewsUpdates()
    }

    override fun onSupportNavigateUp() = findNavController(R.id.navHostController).navigateUp()

    override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
        if (destination.id == R.id.splashFragment) {
            binding.navigation.visibility = View.GONE
        } else {
            binding.navigation.visibility = View.VISIBLE
        }
    }
}
