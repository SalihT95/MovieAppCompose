package com.turkoglu.composedeneme.data.remote.dto

import com.turkoglu.composedeneme.domain.model.CastModel
import com.turkoglu.composedeneme.util.Constants

data class CreditsDto(
    val cast: List<Cast>,
    val id: Int
)

fun CreditsDto.toCastList() : List<CastModel> {
    return cast.map{
        CastModel(it.id,it.name,it.original_name, getImageUrl(it.profile_path),it.cast_id,it.character,it.credit_id,it.order)
         }
}

private fun getImageUrl(posterImage : String)= Constants.IMAGE_BASE_URL +posterImage


