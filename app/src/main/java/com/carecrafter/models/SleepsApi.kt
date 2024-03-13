package com.carecrafter.models

import com.google.gson.annotations.SerializedName

data class SleepsApi(
    @SerializedName("name")
    val name: String,
    @SerializedName("score")
    val score: String,
    @SerializedName("total_sleeps")
    val totalSleeps: Int
)
