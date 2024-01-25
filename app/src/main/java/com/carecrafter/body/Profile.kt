package com.carecrafter.body

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.carecrafter.R
import com.carecrafter.databinding.BodyProfileBinding

class Profile : AppCompatActivity() {
    private lateinit var binding: BodyProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BodyProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_home -> {
                    startActivity(Intent(this, Home::class.java))
                    true
                }
                R.id.action_dashboard -> {
                    startActivity(Intent(this, Dashboard::class.java))
                    true
                }
                R.id.action_features -> {
                    startActivity(Intent(this, Features::class.java))
                    true
                }
                R.id.action_profile -> {
                    startActivity(Intent(this, Profile::class.java))
                    true
                }
                else -> false
            }
        }

        // Set the default selected item
        binding.bottomNavigation.selectedItemId = R.id.action_home
    }
}