package com.carecrafter.models

import com.google.gson.annotations.SerializedName

data class WaterMonthlyStatsApi(
    @SerializedName("week")
    val week: String,
    @SerializedName("water")
    val water: String,
)
