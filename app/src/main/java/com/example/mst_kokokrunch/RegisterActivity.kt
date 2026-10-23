package com.example.mst_kokokrunch

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mst_kokokrunch.databinding.ActivityRegisterBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set email otomatis dari halaman sebelumnya
        val passedEmail = intent.getStringExtra("EXTRA_GMAIL") ?: ""
        binding.etRegEmail.setText(passedEmail)

        binding.btnSubmitRegister.setOnClickListener {
            val nama = binding.etNama.text.toString().trim()
            val email = binding.etRegEmail.text.toString().trim()
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etRegPassword.text.toString().trim()

            // Validasi Registrasi
            if (nama.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                showError("Semua field wajib diisi!")
                return@setOnClickListener
            }

            if (password.length < 6) {
                showError("Password minimal 6 karakter!")
                return@setOnClickListener
            }

            if (username.contains(" ")) {
                showError("Username tidak boleh mengandung spasi!")
                return@setOnClickListener
            }

            // Simpan seluruh data ke SharedPreferences
            val sharedPref = getSharedPreferences("user_data", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putString("nama", nama)
            editor.putString("email", email)
            editor.putString("username", username)
            editor.putString("password", password)
            editor.apply()

            // Tampilkan dialog informasi "Registrasi Berhasil"
            MaterialAlertDialogBuilder(this)
                .setTitle("Registrasi Berhasil")
                .setMessage("Akun Anda telah tersimpan. Silakan login menggunakan Username dan Password Anda.")
                .setPositiveButton("Ke Halaman Login") { _, _ ->
                    finish() // Kembali ke AuthActivity
                }
                .setCancelable(false)
                .show()
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