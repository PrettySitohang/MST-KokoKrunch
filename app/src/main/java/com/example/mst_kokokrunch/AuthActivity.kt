package com.example.mst_kokokrunch

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mst_kokokrunch.databinding.ActivityAuthBinding

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

        binding.btnMasuk.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            // 1. Validasi input kosong
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan Kata Sandi wajib diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 2. Validasi Kredensial Dummy
            if (email == "pretti@mei" && password == "p24") {
                // Simpan status is_logged_in = true
                val sharedPref = getSharedPreferences("user_session", Context.MODE_PRIVATE)
                val editor = sharedPref.edit()
                editor.putBoolean("is_logged_in", true)
                editor.apply()

                Toast.makeText(this, "Login Berhasil!", Toast.LENGTH_SHORT).show()

                // Pindah ke MainActivity
                val intent = Intent(this, ProfileActivity::class.java)
                intent.putExtra("EXTRA_EMAIL", email)
                finish() // Tutup AuthActivity agar tidak bisa diback
            } else {
                Toast.makeText(this, "Email atau Kata Sandi salah!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}