package com.example.expireguard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale

class ProductAdapter(private val productList: ArrayList<Product>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val currentItem = productList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount() = productList.size

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productNameTextView: TextView = itemView.findViewById(R.id.tv_product_name)
        private val productExpirationTextView: TextView = itemView.findViewById(R.id.tv_product_expiration)

        // Date formatter
        private val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        // Or "yyyy-MM-dd" or any other format you prefer

        fun bind(product: Product) {
            productNameTextView.text = product.name

            // Format the Timestamp to a readable date string
            // Handle null expireAt if you made it nullable in your Product data class
            if (product.expireAt != null) { // Or just product.expireAt if it's non-nullable
                val expirationDate = product.expireAt.toDate() // Convert Timestamp to java.util.Date
                productExpirationTextView.text = "Expires: ${dateFormat.format(expirationDate)}"
            } else {
                productExpirationTextView.text = "Expires: N/A"
            }
        }
    }

    // Optional: Method to update the list if you add swipe-to-refresh or live updates
    fun updateData(newProductList: List<Product>) {
        productList.clear()
        productList.addAll(newProductList)
        notifyDataSetChanged() // Be mindful of performance with large lists; consider DiffUtil
    }
}