package com.turkoglu.moviecomposeapp.data.remote.dto.auth

import com.google.gson.annotations.SerializedName

data class RequestCreateSession(
    @SerializedName("request_token")
    val requestToken: String
)
