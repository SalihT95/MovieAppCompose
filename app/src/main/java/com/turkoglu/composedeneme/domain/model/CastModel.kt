package com.turkoglu.composedeneme.domain.model

data class CastModel(
    val id: Int,
    val name: String,
    val original_name: String,
    val profile_path: String,
    val cast_id: Int,
    val character: String,
    val credit_id: String,
    val order: Int
)