package com.turkoglu.moviecomposeapp.presentation.search

sealed class SearchEvent {
    data class Search (val searchString: String): SearchEvent()
}