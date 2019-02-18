package at.ict4d.ict4dnews.screens

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.ui.setupWithNavController
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.ActivityMainNavigationBinding
import at.ict4d.ict4dnews.lifecycle.RXErrorEventBusLifecycleObserver
import at.ict4d.ict4dnews.utils.RxEventBus
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main_navigation.*
import javax.inject.Inject

class MainNavigationActivity : AppCompatActivity(), HasSupportFragmentInjector, NavController.OnDestinationChangedListener {

    private lateinit var binding: ActivityMainNavigationBinding

    private lateinit var model: MainNavigationViewModel

    @Inject
    protected lateinit var compositeDisposable: CompositeDisposable

    @Inject
    protected lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    protected lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    protected lateinit var rxEventBus: RxEventBus

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_navigation)
        model = ViewModelProviders.of(this, viewModelFactory).get(MainNavigationViewModel::class.java)
        lifecycle.addObserver(RXErrorEventBusLifecycleObserver(this, compositeDisposable, rxEventBus))
        val navController = findNavController(navHostController)
        binding.navigation.setupWithNavController(navController)
        navController.addOnDestinationChangedListener(this)
    }

    override fun supportFragmentInjector() = fragmentInjector

    override fun onSupportNavigateUp() = findNavController(navHostController).navigateUp()

    override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
        if (destination.id == R.id.splashFragment) {
            binding.navigation.visibility = View.GONE
        } else {
            binding.navigation.visibility = View.VISIBLE
        }
    }
}