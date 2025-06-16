package com.example.expireguard

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.expireguard.databinding.ActivityEditProductBinding
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProductBinding
    private var productId: String? = null
    private var expiryDate: Long? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        productId = intent.getStringExtra("PRODUCT_ID")

        if (productId == null) {
            finish() // If no product ID is provided, exit the activity
            return
        }

        binding.etProductName.setText(intent.getStringExtra("PRODUCT_NAME"))
        expiryDate = intent.getLongExtra("PRODUCT_EXPIRY", System.currentTimeMillis())
        updateExpiryEditText()

        binding.etProductExpiry.setOnClickListener {
            showDatePicker()
        }

        binding.btnSaveProduct.setOnClickListener {
            val newName = binding.etProductName.text.toString()
            val newExpiry = Timestamp(expiryDate!! / 1000, 0)

            if (newName.isNotBlank() && productId != null) {
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                if (userId == null) {
                    Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show()
                    finish()
                    return@setOnClickListener
                }
                val db = FirebaseFirestore.getInstance()
                val productRef = db.collection("users").document(userId).collection("products").document(productId!!)
                productRef.update("name", newName, "expiryDate", newExpiry)
                    .addOnSuccessListener {
                        setResult(RESULT_OK, Intent())
                        finish() }
                    .addOnFailureListener {
                       Toast.makeText(this, "Error updating product", Toast.LENGTH_SHORT).show()
                        finish()
                    }
            } else {
                finish() // If name is blank or ID is null (shouldn't happen here due to earlier check)
            }
        }
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        expiryDate?.let { calendar.timeInMillis = it }
        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                expiryDate = calendar.timeInMillis
                updateExpiryEditText()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun updateExpiryEditText() {
        val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        binding.etProductExpiry.setText(sdf.format(expiryDate ?: System.currentTimeMillis()))
    }
}