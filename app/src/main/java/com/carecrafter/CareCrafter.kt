package com.carecrafter

import android.app.Application
import android.os.Bundle
import com.carecrafter.retrofit_database.ApiClient.setSharedPreferences


class CareCrafter : Application() {
    override fun onCreate() {
        super.onCreate()

        val sharedPreferences = getSharedPreferences("myPreference", MODE_PRIVATE)

        // set SharedPreferences for PHINMAClient
        setSharedPreferences(sharedPreferences)
    }
}