package com.example.expireguard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Home_activity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnAddProducts: Button
    private lateinit var productList: ArrayList<Product>
    private var db = Firebase.firestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        val btnLogout: Button = findViewById(R.id.btn_logout)
        recyclerView = findViewById(R.id.rcv_product_list)
        btnAddProducts = findViewById(R.id.btn_add_product)
        btnAddProducts.setOnClickListener {
            val intent = Intent(this, AddProductActivity::class.java)
            startActivity(intent)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        productList = arrayListOf()

        db = FirebaseFirestore.getInstance()

        db.collection("product").get()
            .addOnSuccessListener {
                if (!it.isEmpty) {
                    for (data in it.documents) {
                        val product: Product? = data.toObject(Product::class.java)
                        if (product != null) {
                            productList.add(product)
                        }
                    }
                    recyclerView.adapter = ProductAdapter(productList)
                }
            }
            .addOnFailureListener{
                Log.e("Firestore error", it.message.toString())
            }




        btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.Home_page)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars() )
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}