package at.ict4d.ict4dnews.screens

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.ui.setupWithNavController
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.ActivityMainNavigationBinding
import at.ict4d.ict4dnews.screens.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main_navigation.*

class MainNavigationActivity : BaseActivity<MainNavigationViewModel, ActivityMainNavigationBinding>() {

    override fun getViewModel(): Class<MainNavigationViewModel> = MainNavigationViewModel::class.java

    override fun getLayoutId(): Int = R.layout.activity_main_navigation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navController = findNavController(navHostController)
        navigation.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp() = findNavController(navHostController).navigateUp()
}
