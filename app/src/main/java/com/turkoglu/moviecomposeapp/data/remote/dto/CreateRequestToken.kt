package com.turkoglu.moviecomposeapp.data.remote.dto

data class CreateRequestToken(
    val success: Boolean,
    val expires_at: String,
    val request_token: String
)