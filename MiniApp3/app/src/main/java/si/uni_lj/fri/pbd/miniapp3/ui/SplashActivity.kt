package si.uni_lj.fri.pbd.miniapp3.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import si.uni_lj.fri.pbd.miniapp3.R

@SuppressLint("CustomSplashScreen")
@Suppress("DEPRECATION")
class SplashActivity : AppCompatActivity() {
    // Show the splash screen
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Code from https://www.youtube.com/watch?v=Q0gRqbtFLcw
        // Hide the splash screen
        supportActionBar?.hide()
        // Redirect to the main activity after 2 seconds
        Handler().postDelayed({
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}