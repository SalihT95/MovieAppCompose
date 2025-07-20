package com.turkoglu.moviecomposeapp.data.remote.dto.auth

import com.google.gson.annotations.SerializedName

data class CreateRequestToken(
    val success: Boolean,
    @SerializedName("expires_at")
    val expiresAt: String,
    @SerializedName("request_token")
    val requestToken: String
)