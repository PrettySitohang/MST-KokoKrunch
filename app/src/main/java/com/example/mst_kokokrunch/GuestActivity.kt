package com.example.mst_kokokrunch

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mst_kokokrunch.databinding.ActivityGuestBinding

class GuestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGuestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityGuestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Tombol Kembali
        binding.btnBack.setOnClickListener {
            finish()
        }

        // Event Klik Unduh Sertifikat
        binding.btnLihatSertifikat.setOnClickListener {
            downloadCertificate()
        }
    }

    private fun downloadCertificate() {
        // Ganti URL berikut dengan link file PDF/gambar sertifikat yang sebenarnya
        val fileUrl = "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf"
        val fileName = "Sertifikat_Berkah_Palma.pdf"

        try {
            val request = DownloadManager.Request(Uri.parse(fileUrl))
                .setTitle("Mengunduh Sertifikat")
                .setDescription("Sedang mengunduh Sertifikat Berkah Palma...")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

            val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadManager.enqueue(request)

            Toast.makeText(this, "Proses unduh dimulai. Silakan cek notifikasi ponsel!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Gagal mengunduh: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}