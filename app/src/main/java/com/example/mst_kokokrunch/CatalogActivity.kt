package com.example.mst_kokokrunch

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mst_kokokrunch.databinding.ActivityCatalogBinding


class CatalogActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCatalogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityCatalogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.rvProducts.layoutManager = GridLayoutManager(this, 2)
        binding.rvProducts.adapter = ProductAdapter(ProductData.products) { product ->
            openDetailProduct(product.id)
        }

        // Event klik button Lihat Lebih
        binding.btnLihatLebih.setOnClickListener {
            val intent = Intent(this, WebViewActivity::class.java)
            startActivity(intent)
        }


        // Tombol Kembali
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
    private fun openDetailProduct(productId: String) {
        supportFragmentManager.beginTransaction()
            .replace(binding.main.id, DetailProductFragment.newInstance(productId))
            .addToBackStack(null)
            .commit()
    }
}