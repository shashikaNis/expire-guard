package com.example.expireguard

import android.util.Log
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import java.util.Date
import com.example.expireguard.databinding.ActivityAddProductBinding

class AddProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnAdd.setOnClickListener {
            Log.d("AddProductActivity", "Add button clicked")
            addProductToFirestore()
        }

        binding.btnAddProductBack.setOnClickListener {
            Log.d("AddProductActivity", "Back button clicked")
            finish() // Go back to the previous activity
        }
    }

    private fun addProductToFirestore() {
        val productName = binding.etProductName.text.toString().trim()
        val expiryDateString = binding.etExpireDate.dayOfMonth // Assuming YYYY-MM-DD format for now
        val qty = binding.etQty.text.toString().trim()

        if (productName.isEmpty() ) {
            Toast.makeText(this, "Product name and expiry date cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        // For simplicity, directly using String for expiryDate. Consider DatePicker and proper date handling.
        val product = hashMapOf(
            "name" to productName,
            "expiryDate" to expiryDateString, // Store as String, or convert to Timestamp
            "qty" to qty,
            "addedDate" to Date() // Timestamp of when the product was added
        )

        FirebaseFirestore.getInstance().collection("users").document(userId).collection("products")
            .add(product)
            .addOnSuccessListener {
                Toast.makeText(this, "Product added successfully", Toast.LENGTH_SHORT).show()
                finish() // Close activity after adding
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error adding product: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}