package com.turkoglu.composedeneme.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CreateSession(
    @SerializedName("success")
    val success: Boolean, // true
    @SerializedName("session_id")
    val sessionId: String
)
