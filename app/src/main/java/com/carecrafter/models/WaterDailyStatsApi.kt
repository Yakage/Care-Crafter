package com.carecrafter.models

import com.google.gson.annotations.SerializedName

data class WaterDailyStatsApi(
    @SerializedName("name")
    val name: String,
    @SerializedName("total_water")
    val totalWater: String,
    @SerializedName("day_of_week")
    val dayOfWeek: Int,
)
