package com.carecrafter.models
import com.google.gson.annotations.SerializedName

data class StepsApi(
    @SerializedName("name")
    val name: String,
    @SerializedName("total_steps")
    val totalSteps: String,
    @SerializedName("steps")
    val steps: String,
    @SerializedName("latest_avatar")
    val avatar: Int,

)


