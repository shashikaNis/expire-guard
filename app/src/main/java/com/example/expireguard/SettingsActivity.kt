package com.example.expireguard

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import com.example.expireguard.databinding.SettingsActivityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: SettingsActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = SettingsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // set saved settings

        val sharedPref = getSharedPreferences("settings", MODE_PRIVATE)
        binding.chkSettingVibrate.isChecked = sharedPref.getBoolean("vibrate", false)
        binding.chkSettingRingtone.isChecked = sharedPref.getBoolean("ringtone", false)


        binding.btnSave.setOnClickListener {
            saveSettings()

        }
    }

    private fun saveSettings() {
        val vibarate = binding.chkSettingVibrate.isChecked
        val ringtone = binding.chkSettingRingtone.isChecked
        val morning = binding.chkMorning.isChecked
        val afternoon = binding.chkAfternoon.isChecked
        val evening = binding.chkEvening.isChecked
        val night = binding.chkNight.isChecked

        val sharedPref = getSharedPreferences("settings", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("vibrate", vibarate)
        editor.putBoolean("ringtone", ringtone)
        editor.apply()
        // save notification time to firestore
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        FirebaseFirestore.getInstance().collection("users").document(userId).collection("settings")
            .document("notificationTime")
            .set(mapOf(
                "morning" to morning,
                "afternoon" to afternoon,
                "evening" to evening,
                "night" to night
            ))
            .addOnSuccessListener {
                Toast.makeText(this, "Settings saved successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error saving settings: ${e.message}", Toast.LENGTH_SHORT).show()
            }


    }


}