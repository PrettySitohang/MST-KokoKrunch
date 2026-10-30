package com.example.mst_kokokrunch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mst_kokokrunch.databinding.ItemCartBinding
import java.text.NumberFormat
import java.util.Locale

class CartAdapter(
    private var cartItems: MutableList<CartItem>,
    private val onItemQuantityChanged: () -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = cartItems[position]
        val binding = holder.binding

        binding.tvTitleItem.text = item.name

        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
        binding.tvPriceItem.text = formatRupiah.format(item.price)
        binding.tvQtyItem.text = item.quantity.toString()

        if (item.imageRes != 0) {
            binding.imgProduct.setImageResource(item.imageRes)
        }

        // Tombol Tambah (+)
        binding.btnPlus.setOnClickListener {
            item.quantity += 1
            binding.tvQtyItem.text = item.quantity.toString()
            onItemQuantityChanged()
        }

        // Tombol Kurang (-)
        binding.btnMinus.setOnClickListener {
            if (item.quantity > 1) {
                item.quantity -= 1
                binding.tvQtyItem.text = item.quantity.toString()
            } else {
                // Hapus item dari list jika qty = 0
                val pos = holder.adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    cartItems.removeAt(pos)
                    notifyItemRemoved(pos)
                }
            }
            onItemQuantityChanged()
        }
    }

    override fun getItemCount(): Int = cartItems.size

    fun updateData(newList: List<CartItem>) {
        cartItems.clear()
        cartItems.addAll(newList)
        notifyDataSetChanged()
    }
}