package com.turkoglu.moviecomposeapp.data.remote.dto

import com.turkoglu.moviecomposeapp.domain.model.Genre

data class GenreDto(
    val id: Int,
    val name: String
)

data class GenreListDto(
    val genres: List<GenreDto>
)

fun GenreDto.toGenre(): Genre {
    return Genre(id = id, name = name)
}

