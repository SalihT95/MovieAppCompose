package com.turkoglu.moviecomposeapp.presentation.detail

import com.turkoglu.moviecomposeapp.data.remote.dto.Genre

data class DetailState (
    val genres: List<Genre> = emptyList(),
    val imdbId: Int = 0,
    val overview: String="",
    val popularity: Double=0.0,
    val posterPath: String ="",
    val releaseDate: String="",
    val revenue: Int=0,
    val title: String="",
    val voteAverage: Double=0.0
)