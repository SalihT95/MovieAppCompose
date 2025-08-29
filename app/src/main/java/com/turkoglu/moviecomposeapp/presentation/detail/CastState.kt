package com.turkoglu.moviecomposeapp.presentation.detail

import com.turkoglu.moviecomposeapp.domain.model.Person

data class CastState(
    val cast: List<Person> = emptyList(),
    val id: Int = 0
)
