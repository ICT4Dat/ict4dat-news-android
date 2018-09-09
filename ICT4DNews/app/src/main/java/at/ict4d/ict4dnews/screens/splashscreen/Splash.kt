package at.ict4d.ict4dnews.screens.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.screens.MainNavigationActivity

const val SPLASH_DELAY: Long = 1500

class Splash : AppCompatActivity() {
    private val delayHandler = Handler()

    private val runnable: Runnable = Runnable {
        if (!isFinishing) {
            val intent = Intent(applicationContext, MainNavigationActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        delayHandler.postDelayed(runnable, SPLASH_DELAY)
    }

    public override fun onDestroy() {
        delayHandler.removeCallbacks(runnable)
        super.onDestroy()
    }
}