package com.carecrafter.models
import com.google.gson.annotations.SerializedName

data class WaterHistory(
    val created_at: String,
    val daily_goal: String,
    val current_water: String,
    val history: String,
)


