package com.turkoglu.moviecomposeapp.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.turkoglu.moviecomposeapp.domain.model.MovieCast
import com.turkoglu.moviecomposeapp.domain.model.MovieCrew
import com.turkoglu.moviecomposeapp.domain.model.PersonCredits

data class PersonCreditsDto(
    @SerializedName("cast") val cast: List<MovieCastDto>,
    @SerializedName("crew") val crew: List<MovieCrewDto>,
    @SerializedName("id") val id: Int
)

data class MovieCastDto(
    @SerializedName("adult") val adult: Boolean,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("genre_ids") val genreIds: List<Int>,
    @SerializedName("id") val id: Int,
    @SerializedName("original_language") val originalLanguage: String,
    @SerializedName("original_title") val originalTitle: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("popularity") val popularity: Double,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("title") val title: String,
    @SerializedName("video") val video: Boolean,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("vote_count") val voteCount: Int,
    @SerializedName("character") val character: String?,
    @SerializedName("credit_id") val creditId: String,
    @SerializedName("order") val order: Int
)

data class MovieCrewDto(
    @SerializedName("adult") val adult: Boolean,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("genre_ids") val genreIds: List<Int>?,
    @SerializedName("id") val id: Int,
    @SerializedName("original_language") val originalLanguage: String,
    @SerializedName("original_title") val originalTitle: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("popularity") val popularity: Double,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("title") val title: String,
    @SerializedName("video") val video: Boolean,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("vote_count") val voteCount: Int,
    @SerializedName("department") val department: String?,
    @SerializedName("job") val job: String?,
    @SerializedName("credit_id") val creditId: String
)

fun PersonCreditsDto.toDomain(): PersonCredits {
    return PersonCredits(
        cast = cast.map { it.toDomain() },
        crew = crew.map { it.toDomain() },
        personId = id
    )
}

fun MovieCastDto.toDomain(): MovieCast {
    return MovieCast(
        id = id,
        title = title,
        originalTitle = originalTitle,
        posterPath = posterPath,
        backdropPath = backdropPath,
        releaseDate = releaseDate,
        overview = overview,
        character = character,
        order = order,
        voteAverage = voteAverage,
        popularity = popularity
    )
}

fun MovieCrewDto.toDomain(): MovieCrew {
    return MovieCrew(
        id = id,
        title = title,
        originalTitle = originalTitle,
        posterPath = posterPath,
        backdropPath = backdropPath,
        releaseDate = releaseDate,
        overview = overview,
        department = department,
        job = job,
        voteAverage = voteAverage,
        popularity = popularity
    )
}
