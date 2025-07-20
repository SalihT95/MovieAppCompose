package com.turkoglu.moviecomposeapp.data.remote.dto.auth

import com.google.gson.annotations.SerializedName

data class CreateSession(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("session_id")
    val sessionId: String
)
