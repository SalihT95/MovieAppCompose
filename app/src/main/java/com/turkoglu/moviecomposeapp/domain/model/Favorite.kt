package com.turkoglu.moviecomposeapp.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites_table")
data class Favorite(
    val favorite: Boolean,
    @PrimaryKey val mediaId: Int,
    val image: String,
    val title: String,
    val releaseDate: String,
    val rating: Float
)
