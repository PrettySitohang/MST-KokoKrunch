package com.example.mst_kokokrunch

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mst_kokokrunch.databinding.ActivityAuthBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 1. Tombol Masuk
        binding.btnMasuk.setOnClickListener {
            val usernameInput = binding.etEmail.text.toString().trim()
            val passwordInput = binding.etPassword.text.toString().trim()

            if (usernameInput.isEmpty() || passwordInput.isEmpty()) {
                showErrorDialog("Semua kolom (Username/Email dan Password) wajib diisi!")
                return@setOnClickListener
            }

            // Ambil data terdaftar dari SharedPreferences
            val sharedPref = getSharedPreferences("user_data", Context.MODE_PRIVATE)
            val savedUsername = sharedPref.getString("username", "") ?: ""
            val savedPassword = sharedPref.getString("password", "") ?: ""

            // Kondisi Login: (username == password) ATAU (sesuai data SharedPreferences)
            val isRuleUsernameEqualsPassword = (usernameInput == passwordInput)
            val isMatchingSavedData = (savedUsername.isNotEmpty() && usernameInput == savedUsername && passwordInput == savedPassword)

            if (isRuleUsernameEqualsPassword || isMatchingSavedData) {
                // Simpan session
                val sessionPref = getSharedPreferences("user_session", Context.MODE_PRIVATE)
                sessionPref.edit().putBoolean("is_logged_in", true).apply()

                val intent = Intent(this, BaseActivity::class.java)
                intent.putExtra("EXTRA_EMAIL", usernameInput)
                startActivity(intent)
                finish()
            } else {
                showErrorDialog("Login Gagal! Username dan Password tidak cocok.")
            }
        }

        // 2. Tombol Register With Gmail
        binding.btnRegisterGmail.setOnClickListener {
            val intent = Intent(this, InputGmailActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showErrorDialog(message: String) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Error Login")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
}