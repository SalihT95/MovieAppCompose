package com.turkoglu.moviecomposeapp.domain.model

data class PersonCredits(
    val cast: List<MovieCast>,
    val crew: List<MovieCrew>,
    val personId: Int
)

data class MovieCast(
    val id: Int,
    val title: String,
    val originalTitle: String,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: String?,
    val overview: String,
    val character: String?,
    val order: Int,
    val voteAverage: Double,
    val popularity: Double
)

data class MovieCrew(
    val id: Int,
    val title: String,
    val originalTitle: String,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: String?,
    val overview: String,
    val department: String?,
    val job: String?,
    val voteAverage: Double,
    val popularity: Double
)
