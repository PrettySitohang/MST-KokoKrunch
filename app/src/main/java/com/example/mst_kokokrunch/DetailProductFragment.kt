package com.example.mst_kokokrunch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mst_kokokrunch.databinding.FragmentDetailProductBinding

class DetailProductFragment : Fragment() {

    private var _binding: FragmentDetailProductBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Tombol Kembali (Kembali ke Katalog)
        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // 2. Tombol Tambah ke Keranjang
        val addToCartListener = View.OnClickListener {
            val cartManager = CartManager(requireContext())
            val currentList = cartManager.getCartItems()

            val productId = "ppks_540"
            val productName = binding.tvProductName.text.toString()
            val productPrice = 35000.0

            val existingItem = currentList.find { it.id == productId }
            if (existingItem != null) {
                existingItem.quantity += 1
            } else {
                currentList.add(
                    CartItem(
                        id = productId,
                        name = productName,
                        price = productPrice,
                        quantity = 1,
                        imageRes = R.drawable.bibit
                    )
                )
            }

            cartManager.saveCartItems(currentList)
            Toast.makeText(requireContext(), "$productName masuk ke keranjang!", Toast.LENGTH_SHORT).show()
        }

        binding.btnAddToBasket.setOnClickListener(addToCartListener)
        binding.barAddToBasket.setOnClickListener(addToCartListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}