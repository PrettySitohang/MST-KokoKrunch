package com.example.mst_kokokrunch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.mst_kokokrunch.databinding.FragmentDetailProductBinding

class DetailProductFragment : Fragment() {

    private var _binding: FragmentDetailProductBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val ARG_PRODUCT_ID = "product_id"

        fun newInstance(productId: String) = DetailProductFragment().apply {
            arguments = bundleOf(ARG_PRODUCT_ID to productId)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ambil produk berdasarkan id yang dikirim dari katalog
        val product = ProductData.findById(requireArguments().getString(ARG_PRODUCT_ID).orEmpty())
        if (product == null) {
            parentFragmentManager.popBackStack()
            return
        }
        binding.tvProductName.text = product.name

        // 1. Tombol Kembali (Kembali ke Katalog)
        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // 2. Tombol Tambah ke Keranjang
        val addToCartListener = View.OnClickListener {
            val cartManager = CartManager(requireContext())
            val currentList = cartManager.getCartItems()

            val existingItem = currentList.find { it.id == product.id }
            if (existingItem != null) {
                existingItem.quantity += 1
            } else {
                currentList.add(
                    CartItem(
                        id = product.id,
                        name = product.name,
                        price = product.price,
                        quantity = 1,
                        imageRes = product.imageRes
                    )
                )
            }

            cartManager.saveCartItems(currentList)
            Toast.makeText(requireContext(), "${product.name} masuk ke keranjang!", Toast.LENGTH_SHORT).show()
        }

        binding.btnAddToBasket.setOnClickListener(addToCartListener)
        binding.barAddToBasket.setOnClickListener(addToCartListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}