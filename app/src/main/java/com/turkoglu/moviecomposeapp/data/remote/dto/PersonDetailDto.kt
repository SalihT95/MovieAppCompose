package com.turkoglu.moviecomposeapp.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.turkoglu.moviecomposeapp.domain.model.PersonDetail

data class PersonDetailDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("also_known_as") val alsoKnownAs: List<String>?,
    @SerializedName("biography") val biography: String?,
    @SerializedName("birthday") val birthday: String?,
    @SerializedName("deathday") val deathday: String?,
    @SerializedName("gender") val gender: Int?,
    @SerializedName("homepage") val homepage: String?,
    @SerializedName("imdb_id") val imdbId: String?,
    @SerializedName("known_for_department") val knownForDepartment: String?,
    @SerializedName("place_of_birth") val placeOfBirth: String?,
    @SerializedName("popularity") val popularity: Double?,
    @SerializedName("profile_path") val profilePath: String?
)

fun PersonDetailDto.toDomain(): PersonDetail {
    return PersonDetail(
        id = id,
        name = name,
        alsoKnownAs = alsoKnownAs ?: emptyList(),
        biography = biography ?: "",
        birthday = birthday,
        deathday = deathday,
        gender = when (gender) {
            1 -> "Female"
            2 -> "Male"
            else -> "Other"
        },
        homepage = homepage,
        imdbId = imdbId,
        knownForDepartment = knownForDepartment,
        placeOfBirth = placeOfBirth,
        popularity = popularity ?: 0.0,
        profilePath = profilePath
    )
}
