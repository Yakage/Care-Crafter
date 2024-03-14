package com.carecrafter.models

import com.google.gson.annotations.SerializedName

data class StepsMonthlyStatsApi(
    @SerializedName("week")
    val week: String,
    @SerializedName("steps")
    val steps: String,
)
