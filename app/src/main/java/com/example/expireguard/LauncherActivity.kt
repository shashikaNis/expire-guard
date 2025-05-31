package com.example.expireguard

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LauncherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_launcher)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // mekata kiyanne runner akak kiyala
        // meken wenne api dena time akak nawatila idala thama athule tiyana code aka run wenne
        Handler(Looper.getMainLooper()).postDelayed({

            // methana function akakin check karanawa login welada inne kiyala aken login wela innawanm
            // true return wenawa login wela naththan false return wenawa

            val isLoggedIn = checkUserLoginStatus() // mekata anawane tru hari false hari
            // a ana true hari false aka hari thama me check karala tinne
            if (isLoggedIn) {
                // true awoth me Activity akata yano
                startActivity(Intent(this, Home_activity::class.java))
            } else {
                // false awoata
                startActivity(Intent(this, MainActivity::class.java))
            }

            finish()

        }, 2000)// methana deela tiyenne a time aka mili second walin
        // thappara 2k nawathila idala elaga screen akata yanna kiyala tiyenne
    }
    private fun checkUserLoginStatus(): Boolean {
        // meka thama login welada inne kiyala check karana function aka
        // api Firebase Authentication use karala thama login status aka check karanne
        // FirebaseAuth.getInstance().currentUser null nemei nam, user kenek login wela innawa
        return FirebaseAuth.getInstance().currentUser != null
    }
}