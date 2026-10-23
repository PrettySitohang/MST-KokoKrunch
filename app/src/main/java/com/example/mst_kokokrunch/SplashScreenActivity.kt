package com.example.mst_kokokrunch

import android.content.Context
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
            delay(2000) // Tunda 2 detik
            startActivity(Intent(this@SplashScreenActivity, AuthActivity::class.java))
            finish()
        }

            // 1. Cek SharedPreferences
//            val sharedPref = getSharedPreferences("user_session", Context.MODE_PRIVATE)
//            val isLoggedIn = sharedPref.getBoolean("is_logged_in", false)
//
//            // 2. Tentukan tujuan berdasarkan status login
//            val targetActivity = if (isLoggedIn) {
//                BaseActivity::class.java
//            } else {
//                AuthActivity::class.java
//            }

            // 3. Pindah Halaman & Tutup SplashScreen
//            startActivity(Intent(this@SplashScreenActivity, targetActivity))
//            finish()
//        }
    }
}