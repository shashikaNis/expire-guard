package com.example.expireguard

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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

            val isLoggedIn = checkUserLoginStatus()

            if (isLoggedIn) {
                startActivity(Intent(this, AddProductActivity::class.java))
            } else {
                startActivity(Intent(this, MainActivity::class.java))
            }

            finish()

        }, 2000)// methana deela tiyenne a time aka mili second walin
        // thappara 2k nawathila idala elaga screen akata yanna kiyala tiyenne
    }
    private fun checkUserLoginStatus(): Boolean {
        // meka thama login welada inne kiyala check karana function aka
        // api thama login aka hadala nathi nisa man danata meke false kiyana aka retrun karala tiyanawa
        //passe meka api database akath akka check karanna hadanna one
        return false
    }
}