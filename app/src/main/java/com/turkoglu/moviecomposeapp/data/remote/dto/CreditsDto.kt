package com.turkoglu.moviecomposeapp.data.remote.dto

import com.turkoglu.moviecomposeapp.domain.model.Person
import com.turkoglu.moviecomposeapp.util.Constants

data class CreditsDto(
    val cast: List<CastDto>,
    val id: Int
)

fun CreditsDto.toCastList(): List<Person> {
    return cast.map {
        Person(
            it.id,
            it.name,
            it.original_name,
            getImageUrl(it.profile_path),
            it.cast_id,
            it.character,
            it.credit_id,
            it.order
        )
    }
}

private fun getImageUrl(posterImage: String) = Constants.IMAGE_BASE_URL + posterImage
