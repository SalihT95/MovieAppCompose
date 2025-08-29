package com.turkoglu.moviecomposeapp.domain.model

data class PersonDetail(
    val id: Int,
    val name: String,
    val alsoKnownAs: List<String>,
    val biography: String,
    val birthday: String?,
    val deathday: String?,
    val gender: String,
    val homepage: String?,
    val imdbId: String?,
    val knownForDepartment: String?,
    val placeOfBirth: String?,
    val popularity: Double,
    val profilePath: String?
)
