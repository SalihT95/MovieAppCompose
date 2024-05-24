package com.turkoglu.composedeneme.data.remote.dto

data class CreateRequestToken(
    val expires_at: String,
    val request_token: String,
    val success: Boolean
)