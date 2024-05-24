package com.turkoglu.composedeneme.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RequestCreateSessionWL(
    @SerializedName("username")
    val username: String, // johnny_appleseed
    @SerializedName("password")
    val password: String, // test123
    @SerializedName("request_token")
    val requestToken: String
)
