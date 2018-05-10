package at.ict4d.ict4dnews.screens

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.ActivityMainNavigationBinding
import at.ict4d.ict4dnews.screens.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main_navigation.*

class MainNavigationActivity : BaseActivity<ActivityMainNavigationBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_main_navigation

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_news -> {
                binding.message.setText(R.string.nav_news)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_ict4dat -> {
                message.setText(R.string.nav_ict4dat)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_more -> {
                message.setText(R.string.nav_more)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
}
