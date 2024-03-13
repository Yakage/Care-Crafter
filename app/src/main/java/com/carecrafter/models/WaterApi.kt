package com.carecrafter.models
import com.google.gson.annotations.SerializedName

data class WaterApi(
    @SerializedName("name")
    val name: String,
    @SerializedName("total_water")
    val totalWater: String,
    @SerializedName("water")
    val water: Int
)


