package co.diwakar.trivia.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import co.diwakar.trivia.R
import co.diwakar.trivia.registration.RegistrationActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        lifecycleScope.launch {
            delay(DELAY)
            moveToNextScreen()
        }
    }

    private fun moveToNextScreen() {
        Intent(this@SplashActivity, RegistrationActivity::class.java).apply {
            startActivity(this)
        }
        finish()
    }

    companion object {
        const val DELAY = 2_000L
    }
}