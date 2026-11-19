package com.example.mst_kokokrunch

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mst_kokokrunch.databinding.FragmentHomeBinding
import java.io.File
import java.io.FileOutputStream

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.menuKatalog.setOnClickListener {
            startActivity(Intent(requireContext(), CatalogActivity::class.java))
        }
        binding.menuVarietas.setOnClickListener {
            startActivity(Intent(requireContext(), VarietasActivity::class.java))
        }
        binding.menuSertifikat.setOnClickListener {
            generateAndSaveCertificatePdf()
        }
        binding.menuWebsite.setOnClickListener {
            startActivity(Intent(requireContext(), WebViewActivity::class.java))
        }
        binding.btnBack.setOnClickListener {
            requireActivity().finish()
        }
    }

    private fun generateAndSaveCertificatePdf() {
        val pdfDocument = PdfDocument()

        // Ukuran Halaman A4 dalam point (595 x 842 pt)
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas
        val paint = Paint()

        // 1. LATAR BELAKANG & BINGKAI EMAS ELEGANT
        // Latar Luar (Hijau Tua Toolbar)
        paint.color = Color.parseColor("#224A22")
        canvas.drawRect(20f, 20f, 575f, 822f, paint)

        // Isian Dalam Putih
        paint.color = Color.WHITE
        canvas.drawRect(30f, 30f, 565f, 812f, paint)

        // Bingkai Emas Dalam
        paint.color = Color.parseColor("#D4AF37")
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 3f
        canvas.drawRect(38f, 38f, 557f, 804f, paint)

        paint.style = Paint.Style.FILL

        // 2. HEADER SERTIFIKAT
        paint.color = Color.parseColor("#224A22")
        paint.textSize = 22f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText("SERTIFIKAT AKREDITASI", 297f, 110f, paint)

        paint.textSize = 13f
        paint.color = Color.parseColor("#D4AF37")
        canvas.drawText("PRODUSEN & DISTRIBUTOR BIBIT SAWIT UNGGUL", 297f, 135f, paint)

        // Garis Pembatas Header
        paint.color = Color.parseColor("#224A22")
        paint.strokeWidth = 2f
        canvas.drawLine(100f, 155f, 495f, 155f, paint)

        // 3. IDENTITAS CV & PENERIMA
        paint.color = Color.parseColor("#333333")
        paint.textSize = 11f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        canvas.drawText("Diberikan Resmi Kepada badan usaha:", 297f, 200f, paint)

        paint.color = Color.parseColor("#1E4226")
        paint.textSize = 26f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("CV. BERKAH PALMA", 297f, 240f, paint)

        paint.color = Color.parseColor("#666666")
        paint.textSize = 10f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        canvas.drawText("Nomor Registrasi IUP-P: 503/IUP-P/BP/2025", 297f, 262f, paint)

        // 4. DESKRIPSI KELAYAKAN
        paint.color = Color.parseColor("#333333")
        paint.textSize = 11f
        canvas.drawText("Dinyatakan telah memenuhi kualifikasi kelayakan mutu, kemurnian genetis,", 297f, 310f, paint)
        canvas.drawText("serta legalitas sertifikasi benih resmi kelapa sawit unggul nasional.", 297f, 328f, paint)

        // 5. KOTAK INFORMASI LISENSI VARIETAS
        paint.color = Color.parseColor("#F7F8FA")
        canvas.drawRect(70f, 370f, 525f, 500f, paint)

        paint.color = Color.parseColor("#D4AF37")
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1f
        canvas.drawRect(70f, 370f, 525f, 500f, paint)
        paint.style = Paint.Style.FILL

        paint.textAlign = Paint.Align.LEFT
        paint.color = Color.parseColor("#224A22")
        paint.textSize = 12f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("Detail Lisensi & Varietas Terdaftar:", 90f, 400f, paint)

        paint.color = Color.parseColor("#333333")
        paint.textSize = 11f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        canvas.drawText("• Varietas Resmi   : PPKS 540, DxP Topaz 3, Dura", 100f, 425f, paint)
        canvas.drawText("• Predikat Akreditasi: Kelas Utama (Sangat Baik / A)", 100f, 448f, paint)
        canvas.drawText("• Masa Berlaku Lisensi : 12 Januari 2025 – 12 Januari 2030", 100f, 471f, paint)

        // 6. BLOK TANDA TANGAN KEPALA DINAS
        paint.textAlign = Paint.Align.CENTER
        paint.color = Color.parseColor("#333333")
        canvas.drawText("Diterbitkan di Pekanbaru,", 420f, 580f, paint)
        canvas.drawText("Kepala Dinas Perkebunan & Mutu Benih", 420f, 598f, paint)

        // Garis Tanda Tangan
        paint.color = Color.parseColor("#224A22")
        paint.strokeWidth = 1.5f
        canvas.drawLine(340f, 670f, 500f, 670f, paint)

        paint.color = Color.parseColor("#1E4226")
        paint.textSize = 12f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        canvas.drawText("Ir. H. Sudirman, M.Si.", 420f, 690f, paint)

        paint.color = Color.parseColor("#666666")
        paint.textSize = 10f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        canvas.drawText("NIP. 19750812 200112 1 002", 420f, 705f, paint)

        // Selesai Membuat Halaman
        pdfDocument.finishPage(page)

        // 7. PROSES SIMPAN KE FOLDER DOWNLOADS HP
        val fileName = "Sertifikat_Akreditasi_CV_Berkah_Palma.pdf"
        val downloadsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val pdfFile = File(downloadsFolder, fileName)

        try {
            pdfDocument.writeTo(FileOutputStream(pdfFile))
            Toast.makeText(
                requireContext(),
                "Sertifikat PDF berhasil dibuat!\nTersimpan di folder Download.",
                Toast.LENGTH_LONG
            ).show()
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Gagal menyimpan PDF: ${e.message}", Toast.LENGTH_SHORT).show()
        } finally {
            pdfDocument.close()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}