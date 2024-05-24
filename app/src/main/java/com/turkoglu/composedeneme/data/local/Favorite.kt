package com.turkoglu.composedeneme.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoritestable")
data class Favorite(
    val favorite: Boolean,
    @PrimaryKey val mediaId: Int,
    val image: String,
    val title: String,
    val releaseDate: String,
    val rating: Float
)
