package com.turkoglu.moviecomposeapp.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_account")
data class UserAccount(
    @PrimaryKey val id: Int,
    val username: String,
    val name: String?,
    val avatarUrl: String?,
    val includeAdult: Boolean,
    val iso31661: String,
    val iso6391: String,
    val isGuest: Boolean = false
)
