package com.example.mst_kokokrunch

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

class CartManager(context: Context) {
    private val pref = context.getSharedPreferences("user_cart", Context.MODE_PRIVATE)

    fun getCartItems(): MutableList<CartItem> {
        val jsonString = pref.getString("cart_list", "[]") ?: "[]"
        val list = mutableListOf<CartItem>()
        val jsonArray = JSONArray(jsonString)
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            list.add(
                CartItem(
                    id = obj.getString("id"),
                    name = obj.getString("name"),
                    price = obj.getDouble("price"),
                    quantity = obj.getInt("quantity"),
                    imageRes = obj.getInt("imageRes")
                )
            )
        }
        return list
    }

    fun saveCartItems(items: List<CartItem>) {
        val jsonArray = JSONArray()
        for (item in items) {
            val obj = JSONObject().apply {
                put("id", item.id)
                put("name", item.name)
                put("price", item.price)
                put("quantity", item.quantity)
                put("imageRes", item.imageRes)
            }
            jsonArray.put(obj)
        }
        pref.edit().putString("cart_list", jsonArray.toString()).apply()
    }

    fun clearCart() {
        pref.edit().remove("cart_list").apply()
    }
}