package at.ict4d.ict4dnews.screens

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import at.ict4d.ict4dnews.ICT4DNewsApplication
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.ActivityMainNavigationBinding
import at.ict4d.ict4dnews.screens.base.BaseActivity
import at.ict4d.ict4dnews.screens.ict4dat.ICT4DatFragment
import at.ict4d.ict4dnews.screens.more.MoreFragment
import at.ict4d.ict4dnews.screens.news.list.ICT4DNewsFragment
import at.ict4d.ict4dnews.server.IServer
import java.lang.IllegalArgumentException
import javax.inject.Inject

class MainNavigationActivity : BaseActivity<ActivityMainNavigationBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_main_navigation

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_news -> {
                showFragment(ICT4DNewsFragment::class.java.simpleName, true)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_ict4dat -> {
                showFragment(ICT4DatFragment::class.java.simpleName, true)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_more -> {
                showFragment(MoreFragment::class.java.simpleName, true)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        ICT4DNewsApplication.component.inject(this)

        if (savedInstanceState == null) {
            showFragment(ICT4DNewsFragment::class.java.simpleName)
        }
    }

    /**
     * convenience method to replace the content fragment
     * @param fragmentTag an identifying string of the new fragment
     * @param addToBackStack if true then this fragment transaction will be added to the backstack. Default = false
     */
    private fun showFragment(fragmentTag: String, addToBackStack: Boolean = false) {

        val currentTag = supportFragmentManager.findFragmentById(R.id.placeholder)?.tag

        if (currentTag != fragmentTag) {
            var fragment: Fragment? = supportFragmentManager.findFragmentByTag(fragmentTag)

            if (fragment == null) {
                when (fragmentTag) {
                    ICT4DNewsFragment::class.java.simpleName -> fragment = ICT4DNewsFragment.newInstance()
                    ICT4DatFragment::class.java.simpleName -> fragment = ICT4DatFragment()
                    MoreFragment::class.java.simpleName -> fragment = MoreFragment()
                    else -> throw IllegalArgumentException("Fragment Tag unknown.")
                }
            }

            val transaction = supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.placeholder, fragment, fragmentTag)

            if (addToBackStack) {
                transaction.addToBackStack(fragmentTag)
            }

            transaction.commit()
        }
    }
}
