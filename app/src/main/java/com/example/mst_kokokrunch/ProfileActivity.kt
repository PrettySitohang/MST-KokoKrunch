package com.example.mst_kokokrunch

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mst_kokokrunch.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 1. Tampilkan data email tersimpan
        val sharedPref = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val savedEmail = sharedPref.getString("logged_in_user", sharedPref.getString("user_email", "pretti@mei"))
        binding.tvEmail.text = "Email: $savedEmail"

        // 2. Fungsi Logout saat tombol @+id/etlogout diklik
        binding.etlogout.setOnClickListener {
            // Hapus data sesi login
            val sessionPref = getSharedPreferences("user_session", Context.MODE_PRIVATE)
            sessionPref.edit().clear().apply()

            // Hapus juga data pendaftaran (user_data)
            val userDataPref = getSharedPreferences("user_data", Context.MODE_PRIVATE)
            userDataPref.edit().clear().apply()

            Toast.makeText(this, "Berhasil Logout!", Toast.LENGTH_SHORT).show()

            // Arahkan kembali ke halaman Login (AuthActivity) & bersihkan riwayat halaman (backstack)
            val intent = Intent(this, AuthActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            finish()
        }
    }
}