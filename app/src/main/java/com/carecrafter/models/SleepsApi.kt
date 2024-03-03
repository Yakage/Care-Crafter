package com.carecrafter.models

import com.google.gson.annotations.SerializedName

data class SleepsApi(
    @SerializedName("name")
    val name: String,
    @SerializedName("total_sleeps")
    val totalSleeps: String
)
