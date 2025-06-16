package com.example.expireguard

import com.google.firebase.Timestamp
import java.util.Date

data class Product(
    var id: String = "",
    val name: String = "",
    val expiryDate: Timestamp = Timestamp.now()
) {
    // Optional: Add a helper function to convert Timestamp to Date if needed frequently
    fun getExpirationDate(): Date? {
        return expiryDate?.toDate()
    }
}