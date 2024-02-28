package com.carecrafter.body

import android.app.Activity
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.carecrafter.R
import com.carecrafter.databinding.ActivityBodyBinding
import com.carecrafter.models.SharedPrefsViewModel
import com.carecrafter.models.UserViewModel

class BodyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBodyBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var viewModel: SharedPrefsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBodyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)

        val sharedPreferences = getSharedPreferences("myPreference", MODE_PRIVATE)
        viewModel = ViewModelProvider(this@BodyActivity).get(SharedPrefsViewModel::class.java)
        val authToken = sharedPreferences.getString("authToken", "")
        viewModel.authToken = "Bearer $authToken"
    }

    fun getAuthToken(): String? {
        return viewModel.authToken
    }
}
