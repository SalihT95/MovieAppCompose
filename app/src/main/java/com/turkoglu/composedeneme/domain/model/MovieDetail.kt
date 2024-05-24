package com.turkoglu.composedeneme.domain.model

import com.turkoglu.composedeneme.data.remote.dto.Genre

data class MovieDetail(
    val adult: Boolean,
    val backdropPath: String,
    val genres: List<Genre>,
    val id: Int,
    val overview: String,
    val popularity: Double,
    val posterPath: String,
    val releaseDate: String,
    val revenue: Int,
    val title: String,
    val voteAverage: Double
)
