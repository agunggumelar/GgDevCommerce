package id.ggdev.ggdevcommerce.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import id.ggdev.ggdevcommerce.R
import id.ggdev.ggdevcommerce.data.AppSessionManager
import id.ggdev.ggdevcommerce.ui.loginSignup.LoginSignUpActivity

class LaunchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_launch)
        setLaunchScreenTimeOut()
    }

    private fun setLaunchScreenTimeOut() {
        Looper.myLooper()?.let {
            Handler(it).postDelayed({
                startPreferredActivity()
            }, TIME_OUT)
        }
    }

    private fun startPreferredActivity() {
        val sessionManager = AppSessionManager(this)
        if (sessionManager.isLoggedIn()) {
            launchHome(this)
            finish()
        } else {
            val intent = Intent(this, LoginSignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    companion object {
        private const val TIME_OUT: Long = 1500
    }
}