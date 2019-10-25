package at.ict4d.ict4dnews.screens.splashscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.FragmentSplashBinding
import at.ict4d.ict4dnews.extensions.filterObservableAndSetThread
import at.ict4d.ict4dnews.extensions.visible
import at.ict4d.ict4dnews.screens.base.BaseFragment
import at.ict4d.ict4dnews.utils.RxEventBus
import at.ict4d.ict4dnews.utils.ServerErrorMessage
import at.ict4d.ict4dnews.utils.recordNavigationBreadcrumb
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject

class SplashFragment :
    BaseFragment<SplashViewModel, FragmentSplashBinding>(
        R.layout.fragment_splash,
        SplashViewModel::class,
        hasToolbar = false
    ) {

    private val rxEventBus: RxEventBus by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        compositeDisposable.add(rxEventBus.filterObservableAndSetThread<ServerErrorMessage>(
            subscribeThread = Schedulers.io()
        )
            .subscribe {
                if (view?.findNavController()?.currentDestination?.id == R.id.splashFragment) {
                    recordNavigationBreadcrumb("pop", this)
                    view.findNavController().popBackStack()
                }
            })

        model.allBlogs.observe(this, Observer {
            if (it != null && it.isNotEmpty()) {
                view?.findNavController()?.popBackStack()
            } else {
                binding.splashProgressBar.visible(true)
                binding.ict4datLogo.visible(true)
            }
        })
        return view
    }
}