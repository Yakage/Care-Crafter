package com.carecrafter.models
import com.google.gson.annotations.SerializedName

data class StepHistory(
    val daily_goal: String,
    val current_steps: String,
    val created_at: String,
)


