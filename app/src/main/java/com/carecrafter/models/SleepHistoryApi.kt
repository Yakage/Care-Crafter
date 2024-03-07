package com.carecrafter.models
import com.google.gson.annotations.SerializedName

data class SleepHistoryApi(
    @SerializedName("score")
    val score: String,
    @SerializedName("sleeps")
    val sleeps: String,
    @SerializedName("date")
    val date: String
)


