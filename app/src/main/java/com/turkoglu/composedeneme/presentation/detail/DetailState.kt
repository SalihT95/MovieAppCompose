package com.turkoglu.composedeneme.presentation.detail

import com.turkoglu.composedeneme.data.remote.dto.Genre
import com.turkoglu.composedeneme.data.remote.dto.Video
import com.turkoglu.composedeneme.domain.model.VideoModel

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