package at.ict4d.ict4dnews.screens.splashscreen

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.FragmentSplashBinding
import at.ict4d.ict4dnews.screens.base.BaseFragment

class SplashFragment : BaseFragment<SplashViewModel, FragmentSplashBinding>() {
    private val delayHandler = Handler()
    private val splashDelay: Long = 1500

    private val runnable: Runnable = Runnable {
        activity?.let {
            if (!it.isFinishing) {
                findNavController().navigate(R.id.action_splashFragment_to_blogAndSourceFragment)
            }
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_splash

    override fun getViewModel(): Class<SplashViewModel> = SplashViewModel::class.java

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        delayHandler.postDelayed(runnable, splashDelay)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        delayHandler.removeCallbacks(runnable)
    }
}