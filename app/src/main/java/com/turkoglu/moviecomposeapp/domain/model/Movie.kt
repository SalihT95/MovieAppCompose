package com.turkoglu.moviecomposeapp.domain.model

data class Movie(
    val id: Int?,
    val title: String,
    val description: String,
    val posterPath: String,
    val backdropPath: String,
    val releaseDate: String?,
    val voteAverage: Double? = null,
)
