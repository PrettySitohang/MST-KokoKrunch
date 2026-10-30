package com.example.mst_kokokrunch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mst_kokokrunch.databinding.FragmentBasketBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.NumberFormat
import java.util.Locale

class BasketFragment : Fragment() {

    private var _binding: FragmentBasketBinding? = null
    private val binding get() = _binding!!

    private lateinit var cartManager: CartManager
    private var cartList = mutableListOf<CartItem>()
    private lateinit var adapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBasketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cartManager = CartManager(requireContext())

        // Inisialisasi RecyclerView
        adapter = CartAdapter(cartList) {
            cartManager.saveCartItems(cartList)
            updateTotalPrice()
            checkEmptyState()
        }

        binding.rvCartItems.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCartItems.adapter = adapter

        // Tombol Checkout
        binding.btnCheckout.setOnClickListener {
            if (cartList.isEmpty()) {
                Toast.makeText(requireContext(), "Keranjang kamu masih kosong!", Toast.LENGTH_SHORT).show()
            } else {
                showCheckoutDialog()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadCartData()
    }

    private fun loadCartData() {
        cartList.clear()
        cartList.addAll(cartManager.getCartItems())
        adapter.updateData(cartList)

        updateTotalPrice()
        checkEmptyState()
    }

    private fun checkEmptyState() {
        if (cartList.isEmpty()) {
            binding.tvEmptyCart.visibility = View.VISIBLE
            binding.rvCartItems.visibility = View.GONE
        } else {
            binding.tvEmptyCart.visibility = View.GONE
            binding.rvCartItems.visibility = View.VISIBLE
        }
    }

    private fun updateTotalPrice() {
        val total = cartList.sumOf { it.price * it.quantity }
        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)

        binding.tvTotalPrice.text = formatRupiah.format(total)
        binding.btnCheckout.text = "Checkout (${cartList.sumOf { it.quantity }})"
    }

    private fun showCheckoutDialog() {
        val total = cartList.sumOf { it.price * it.quantity }
        val localeID = Locale("in", "ID")
        val totalFormatted = NumberFormat.getCurrencyInstance(localeID).format(total)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Konfirmasi Checkout")
            .setMessage("Total pembayaran kamu adalah $totalFormatted. Lanjutkan transaksi?")
            .setPositiveButton("Bayar Sekarang") { dialog, _ ->
                cartManager.clearCart()
                loadCartData()

                Toast.makeText(requireContext(), "Transaksi Berhasil! Pesanan sedang diproses.", Toast.LENGTH_LONG).show()
                dialog.dismiss()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}