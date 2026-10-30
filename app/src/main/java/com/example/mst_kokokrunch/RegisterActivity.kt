package com.example.mst_kokokrunch

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mst_kokokrunch.databinding.ActivityRegisterBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Ambil email yang dikirim dari InputGmailActivity
        val gmailFromInput = intent.getStringExtra("EXTRA_GMAIL") ?: ""

        // Tampilkan/kunci email jika ada kolom email di layout registrasi
        binding.etRegEmail.setText(gmailFromInput)
        binding.etRegEmail.isEnabled = false // Email diset read-only karena sudah divalidasi

        // 2. Tombol Selesaikan Registrasi
        binding.btnSubmitRegister.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etRegPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                showError("Username dan Password wajib diisi!")
                return@setOnClickListener
            }

            // 3. Simpan data registrasi ke SharedPreferences "user_data"
            val sharedPref = getSharedPreferences("user_data", Context.MODE_PRIVATE)
            sharedPref.edit().apply {
                putString("email", gmailFromInput)
                putString("username", username)
                putString("password", password)
                apply()
            }

            Toast.makeText(this, "Registrasi berhasil! Silakan login.", Toast.LENGTH_SHORT).show()

            // 4. Kembali ke AuthActivity (Halaman Login)
            val intent = Intent(this, AuthActivity::class.java)
            // Clear stack agar user tidak bisa tekan back kembali ke form registrasi
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun showError(message: String) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Registrasi Gagal")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
}