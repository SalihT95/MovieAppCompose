package com.turkoglu.composedeneme.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RequestCreateSession(
    @SerializedName("request_token")
    val requestToken: String
)
