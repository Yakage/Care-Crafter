package com.carecrafter.models

import com.google.gson.annotations.SerializedName

data class WaterWeeklyStatsApi(
    @SerializedName("week_of_year")
    val weekOfYear: Int,
    @SerializedName("week_start_date")
    val weekStartDate: String, // Add week start date field
    @SerializedName("total_water")
    val totalWater: String
)
