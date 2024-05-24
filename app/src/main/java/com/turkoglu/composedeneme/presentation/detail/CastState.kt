package com.turkoglu.composedeneme.presentation.detail

import android.os.Parcelable
import com.turkoglu.composedeneme.data.remote.dto.Cast
import kotlinx.parcelize.Parcelize

@Parcelize
data class CastState(
    val cast: List<Cast> = emptyList(),
    val id: Int = 0
) : Parcelable
