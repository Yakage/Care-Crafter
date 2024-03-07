package com.carecrafter.models
import com.google.gson.annotations.SerializedName

data class WaterHistory(
    @SerializedName("daily_goal")
    val daily_goal: String,
    @SerializedName("current_water")
    val currentWater: String,
    @SerializedName("history")
    val history: String,
    @SerializedName("water_sum")
    val totalWater: String,
)


