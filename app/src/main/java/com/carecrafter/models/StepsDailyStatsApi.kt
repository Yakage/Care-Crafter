package com.carecrafter.models

import com.google.gson.annotations.SerializedName

data class StepsDailyStatsApi(
    @SerializedName("name")
    val name: String,
    @SerializedName("total_steps")
    val totalSteps: String,
    @SerializedName("day_of_week")
    val dayOfWeek: Int,
)
