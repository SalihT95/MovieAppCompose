package com.turkoglu.moviecomposeapp.data.remote.dto.auth

import com.google.gson.annotations.SerializedName
import com.turkoglu.moviecomposeapp.domain.model.UserAccount

data class AccountDetails(
    val avatar: Avatar,
    val id: String,
    @SerializedName("include_adult")
    val includeAdult: Boolean,
    @SerializedName("iso_3166_1")
    val iso31661: String,
    @SerializedName("iso_639_1")
    val iso6391: String,
    val name: String,
    val username: String
)

fun AccountDetails.toUserAccount(): UserAccount {
    return UserAccount(
        id = id,
        name = name,
        username = username,
        avatarUrl = avatar.tmdb.avatarPath,
        includeAdult = includeAdult,
        iso31661 = iso31661,
        iso6391 = iso6391
    )
}