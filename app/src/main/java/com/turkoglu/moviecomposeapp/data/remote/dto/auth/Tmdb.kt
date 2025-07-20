package com.turkoglu.moviecomposeapp.data.remote.dto.auth

import com.google.gson.annotations.SerializedName

data class Tmdb(
    @SerializedName("avatar_path")
    val avatarPath: String
)