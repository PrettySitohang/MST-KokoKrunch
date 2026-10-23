package com.example.mst_kokokrunch

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mst_kokokrunch.databinding.ActivityInputGmailBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class InputGmailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInputGmailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputGmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tombol Lanjutkan Registrasi
        binding.btnLanjut.setOnClickListener {
            val email = binding.etGmailInput.text.toString().trim()

            if (email.isEmpty()) {
                showError("Email tidak boleh kosong!")
                return@setOnClickListener
            }

            if (!email.endsWith("@gmail.com")) {
                showError("Email harus menggunakan domain @gmail.com")
                return@setOnClickListener
            }

            // Pindah ke RegisterActivity
            val intent = Intent(this, RegisterActivity::class.java)
            intent.putExtra("EXTRA_GMAIL", email)
            startActivity(intent)
            finish()
        }

        // Tombol Kembali ke Login
        binding.tvBackToLogin.setOnClickListener {
            finish()
        }
    }

    private fun showError(message: String) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Validasi Gagal")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
}