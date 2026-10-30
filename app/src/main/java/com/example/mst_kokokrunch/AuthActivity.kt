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

        // 1. CEK AUTO-LOGIN: Jika user sudah login, langsung lempar ke BaseActivity
        val sessionPref = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        if (sessionPref.getBoolean("is_logged_in", false)) {
            val userDataPref = getSharedPreferences("user_data", Context.MODE_PRIVATE)
            val savedEmail = userDataPref.getString("email", "") ?: ""

            val intent = Intent(this, BaseActivity::class.java).apply {
                putExtra("EXTRA_EMAIL", savedEmail)
            }
            startActivity(intent)
            finish()
            return
        }

        enableEdgeToEdge()
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 2. Tombol Masuk
        binding.btnMasuk.setOnClickListener {
            val usernameInput = binding.etEmail.text.toString().trim()
            val passwordInput = binding.etPassword.text.toString().trim()

            if (usernameInput.isEmpty() || passwordInput.isEmpty()) {
                showErrorDialog("Semua kolom (Username/Email dan Password) wajib diisi!")
                return@setOnClickListener
            }

            // Ambil data hasil registrasi dari SharedPreferences
            val sharedPref = getSharedPreferences("user_data", Context.MODE_PRIVATE)
            val savedUsername = sharedPref.getString("username", "") ?: ""
            val savedEmail = sharedPref.getString("email", "") ?: ""
            val savedPassword = sharedPref.getString("password", "") ?: ""

            // Kondisi Login: (username == password) ATAU (cocok dengan username/email & password terdaftar)
            val isRuleUsernameEqualsPassword = (usernameInput == passwordInput)
            val isMatchingSavedData = (savedPassword.isNotEmpty() && passwordInput == savedPassword) &&
                    (usernameInput == savedUsername || usernameInput == savedEmail)

            if (isRuleUsernameEqualsPassword || isMatchingSavedData) {
                // Simpan status session login
                sessionPref.edit().apply {
                    putBoolean("is_logged_in", true)
                    putString("logged_in_user", usernameInput)
                    apply()
                }

                val intent = Intent(this, BaseActivity::class.java).apply {
                    putExtra("EXTRA_EMAIL", usernameInput)
                }
                startActivity(intent)
                finish()
            } else {
                showErrorDialog("Login Gagal! Username/Email dan Password tidak cocok.")
            }
        }

        // 3. Tombol Register With Gmail
        binding.btnRegisterGmail.setOnClickListener {
            val intent = Intent(this, InputGmailActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // Otomatis isi kolom email/username jika user baru saja selesai registrasi di InputGmailActivity
        val sharedPref = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val registeredEmail = sharedPref.getString("email", "") ?: ""
        val registeredUsername = sharedPref.getString("username", "") ?: ""

        if (registeredEmail.isNotEmpty() && binding.etEmail.text.toString().isEmpty()) {
            binding.etEmail.setText(registeredEmail.ifEmpty { registeredUsername })
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