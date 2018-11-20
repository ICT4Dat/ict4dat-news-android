package at.ict4d.ict4dnews.screens

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.ActivityMainNavigationBinding
import at.ict4d.ict4dnews.screens.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main_navigation.*

class MainNavigationActivity : BaseActivity<MainNavigationViewModel, ActivityMainNavigationBinding>(),

    NavController.OnNavigatedListener {

    override fun getViewModel(): Class<MainNavigationViewModel> = MainNavigationViewModel::class.java

    override fun getLayoutId(): Int = R.layout.activity_main_navigation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navController = findNavController(navHostController)
        navigation.setupWithNavController(navController)
        navController.addOnNavigatedListener(this)
    }

    override fun onSupportNavigateUp() = findNavController(navHostController).navigateUp()

    override fun onNavigated(controller: NavController, destination: NavDestination) {
        // resets subtitle of Toolbar
        if (destination.id != R.id.blogAndSourceFragment) {
            supportActionBar?.subtitle = ""
        }

        if (destination.id != R.id.splashFragment && destination.id != R.id.actionNews) {
            setupActionBarWithNavController(controller)
            controller.graph.startDestination = R.id.actionNews
        }

        if (destination.id == R.id.splashFragment) {
            supportActionBar?.hide()
            binding.navigation.visibility = View.GONE
        } else if (destination.id == R.id.ICT4DNewsDetailFragment) {
            supportActionBar?.hide()
        } else {
            supportActionBar?.show()
            binding.navigation.visibility = View.VISIBLE
        }
    }
}