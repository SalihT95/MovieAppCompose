package com.turkoglu.moviecomposeapp.presentation.detail

import android.os.Parcelable
import com.turkoglu.moviecomposeapp.data.remote.dto.Cast
import kotlinx.parcelize.Parcelize

@Parcelize
data class CastState(
    val cast: List<Cast> = emptyList(),
    val id: Int = 0
) : Parcelable
