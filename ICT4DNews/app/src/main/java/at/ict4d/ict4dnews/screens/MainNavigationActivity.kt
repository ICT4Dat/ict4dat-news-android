package at.ict4d.ict4dnews.screens

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import at.ict4d.ict4dnews.R
import kotlinx.android.synthetic.main.activity_main_navigation.*
import timber.log.Timber

class MainNavigationActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_news -> {
                message.setText(R.string.nav_news)
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
        setContentView(R.layout.activity_main_navigation)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }
}
