package com.carecrafter.models

import com.google.gson.annotations.SerializedName

data class WaterWeeklyStatsApi(
    @SerializedName("label")
    val label: String,
    @SerializedName("value")
    val value: String,
)
