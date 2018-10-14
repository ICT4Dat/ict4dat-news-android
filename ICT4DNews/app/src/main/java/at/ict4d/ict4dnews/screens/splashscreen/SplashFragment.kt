package at.ict4d.ict4dnews.screens.splashscreen

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.FragmentSplashBinding
import at.ict4d.ict4dnews.screens.base.BaseFragment
import at.ict4d.ict4dnews.utils.RxEventBus
import at.ict4d.ict4dnews.utils.ServerErrorMessage
import javax.inject.Inject

class SplashFragment : BaseFragment<SplashViewModel, FragmentSplashBinding>() {

    @Inject
    protected lateinit var rxEventBus: RxEventBus

    override fun getLayoutId(): Int = R.layout.fragment_splash

    override fun getViewModel(): Class<SplashViewModel> = SplashViewModel::class.java

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        compositeDisposable.add(rxEventBus.filteredObservable(ServerErrorMessage::class.java).subscribe {
            if (findNavController().currentDestination?.id == R.id.splashFragment) {
                findNavController().navigate(R.id.action_splashFragment_to_news_fragment)
            }
        })

        model.allBlogs.observe(this, Observer {
            if (it != null && it.isNotEmpty()) {
                findNavController().navigate(R.id.action_splashFragment_to_news_fragment)
            }
        })
        return view
    }
}