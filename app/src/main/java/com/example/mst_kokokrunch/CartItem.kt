package com.example.mst_kokokrunch

data class CartItem(
    val id: String,
    val name: String,
    val price: Double,
    var quantity: Int,
    val imageRes: Int
)