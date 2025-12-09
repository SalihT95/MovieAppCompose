package com.turkoglu.moviecomposeapp.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_account")
data class UserAccount(
    @PrimaryKey
    val id: String,

    val username: String?,
    val name: String?,
    val avatarUrl: String?,
    val includeAdult: Boolean = false,
    val iso31661: String = "TR",
    val iso6391: String = "tr",

    val isGuest: Boolean = false
)