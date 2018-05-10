package at.ict4d.ict4dnews.screens

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.ActivityMainNavigationBinding
import at.ict4d.ict4dnews.screens.base.BaseActivity
import at.ict4d.ict4dnews.screens.news.ICT4DNewsFragment
import kotlinx.android.synthetic.main.activity_main_navigation.*
import java.lang.IllegalArgumentException

class MainNavigationActivity : BaseActivity<ActivityMainNavigationBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_main_navigation

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_news -> {

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_ict4dat -> {

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_more -> {

                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

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

        var fragment: Fragment? = supportFragmentManager.findFragmentByTag(fragmentTag)

        if (fragment == null) {
            when (fragmentTag) {
                ICT4DNewsFragment::class.java.simpleName -> fragment = ICT4DNewsFragment.newInstance()
                else -> throw IllegalArgumentException("Fragment Tag unknown.")
            }
        }

        val transaction = supportFragmentManager
                .beginTransaction()
                .replace(R.id.placeholder, fragment, fragmentTag)

        if (addToBackStack) {
            transaction.addToBackStack(null)
        }

        transaction.commit()
    }
}
