package at.ict4d.ict4dnews.screens.splashscreen

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.screens.MainNavigationActivity

const val SPLASH_DELAY: Long = 3000

class Splash : AppCompatActivity() {
    private var delayHandler = Handler()
    internal val runnable: Runnable = Runnable {
        if (!isFinishing) {
            val intent = Intent(applicationContext, MainNavigationActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        delayHandler = Handler()
        delayHandler.postDelayed(runnable, SPLASH_DELAY)
    }
    public override fun onDestroy() {
        if (true) {
            delayHandler.removeCallbacks(runnable)
        }
        super.onDestroy()
    }
}