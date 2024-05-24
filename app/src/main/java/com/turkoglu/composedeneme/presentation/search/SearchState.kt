package com.turkoglu.composedeneme.presentation.search

import com.turkoglu.composedeneme.data.remote.dto.Result

data class SearchState (
    val isLoading : Boolean = false,
    val movies : List<Result> = emptyList(),
    val error  : String = "",
    val search : String = "Star Wars"
)