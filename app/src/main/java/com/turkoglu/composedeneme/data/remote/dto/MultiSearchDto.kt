package com.turkoglu.composedeneme.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.turkoglu.composedeneme.domain.model.Search

data class MultiSearchDto(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val searches: List<Search>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)