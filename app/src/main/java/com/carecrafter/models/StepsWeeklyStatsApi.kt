package com.carecrafter.models

import com.google.gson.annotations.SerializedName

data class StepsWeeklyStatsApi(
    @SerializedName("label")
    val label: String,
    @SerializedName("value")
    val value: String,
)
