package com.carecrafter.models

import com.google.gson.annotations.SerializedName
data class DefaultResponse(
    @SerializedName("error") val error: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("userId") val userId: String // Add the userId field
)

