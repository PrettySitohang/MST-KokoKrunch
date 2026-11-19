package com.example.mst_kokokrunch

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lifecycleScope.launch {
            delay(2000)

            val onboardingDone = getSharedPreferences("app_prefs", MODE_PRIVATE)
                .getBoolean("onboarding_done", false)
            val isLoggedIn = getSharedPreferences("user_session", MODE_PRIVATE)
                .getBoolean("is_logged_in", false)

            val target = when {
                !onboardingDone -> OnboardingActivity::class.java
                isLoggedIn -> BaseActivity::class.java
                else -> AuthActivity::class.java
            }

            startActivity(Intent(this@SplashScreenActivity, target))
            finish()
        }
    }
}