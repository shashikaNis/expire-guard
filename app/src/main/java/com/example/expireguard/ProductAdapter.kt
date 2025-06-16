package com.example.expireguard

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
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

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productNameTextView: TextView = itemView.findViewById(R.id.tv_product_name)
        private val productExpirationTextView: TextView = itemView.findViewById(R.id.tv_product_expiration)

        // Date formatter
        private val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

        init {
            itemView.setOnLongClickListener { view -> // 'view' is the clicked item view
                val position = adapterPosition
                if (position == RecyclerView.NO_POSITION) return@setOnLongClickListener true // Check if position is valid
                val product = productList[position] // Get the product associated with this ViewHolder
                val popup = PopupMenu(view.context, view)
                popup.menuInflater.inflate(R.menu.product_item_menu, popup.menu)
                popup.setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.action_edit -> {
                            val intent = Intent(view.context, EditProductActivity::class.java)
                            intent.putExtra("PRODUCT_ID", product.id)
                            intent.putExtra("PRODUCT_NAME", product.name)
                            intent.putExtra("PRODUCT_EXPIRY", product.expiryDate?.toDate()?.time ?: 0L)
                            view.context.startActivity(intent)
                            true
                        }
                        R.id.action_delete -> {
                            // Handle delete
                            true
                        }
                        else -> false
                    }
                }
                popup.show()
                true
            }
        }
        fun bind(product: Product) {
            productNameTextView.text = product.name

            // Format the Timestamp to a readable date string
            // Handle null expireAt if you made it nullable in your Product data class
            if (product.expiryDate != null) { // Or just product.expireAt if it's non-nullable
                val expirationDate = product.expiryDate.toDate() // Convert Timestamp to java.util.Date
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