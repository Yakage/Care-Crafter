package com.carecrafter.models

import com.google.gson.annotations.SerializedName

data class StepsMonthlyStatsApi(
    @SerializedName("name")
    val name: String,
    @SerializedName("total_steps")
    val totalSteps: String,
    @SerializedName("day_of_week")
    val monthNumber: Int,
)
