package com.carecrafter.models
import com.google.gson.annotations.SerializedName

data class StepHistory(
    @SerializedName("daily_goal")
    val daily_goal: String,
    @SerializedName("current_steps")
    val current_steps: String,
)


