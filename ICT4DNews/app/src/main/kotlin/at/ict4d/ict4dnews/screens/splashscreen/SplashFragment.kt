package at.ict4d.ict4dnews.screens.splashscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.FragmentSplashBinding
import at.ict4d.ict4dnews.extensions.visible
import at.ict4d.ict4dnews.screens.base.BaseFragment
import at.ict4d.ict4dnews.utils.RxEventBus
import at.ict4d.ict4dnews.utils.ServerErrorMessage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SplashFragment : BaseFragment<SplashViewModel, FragmentSplashBinding>() {

    @Inject
    protected lateinit var rxEventBus: RxEventBus

    override fun getLayoutId(): Int = R.layout.fragment_splash

    override fun getViewModel(): Class<SplashViewModel> = SplashViewModel::class.java

    override fun isFragmentContainToolbar(): Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        compositeDisposable.add(rxEventBus.filteredObservable(ServerErrorMessage::class.java)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe {
                if (view?.findNavController()?.currentDestination?.id == R.id.splashFragment) {
                    view?.let {
                        it.findNavController().navigate(R.id.action_splashFragment_to_news_fragment)
                    }
                }
            })

        model.allBlogs.observe(this, Observer {
            if (it != null && it.isNotEmpty()) {
                view?.findNavController()?.navigate(R.id.action_splashFragment_to_news_fragment)
            } else {
                binding.splashProgressBar.visible(true)
                binding.ict4datLogo.visible(true)
            }
        })
        return view
    }
}