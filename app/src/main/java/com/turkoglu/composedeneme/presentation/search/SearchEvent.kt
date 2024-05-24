package com.turkoglu.composedeneme.presentation.search

sealed class SearchEvent {
    data class Search (val searchString: String): SearchEvent()
}