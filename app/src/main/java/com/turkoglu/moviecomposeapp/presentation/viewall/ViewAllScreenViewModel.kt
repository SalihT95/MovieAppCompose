package com.turkoglu.moviecomposeapp.presentation.viewall

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.turkoglu.moviecomposeapp.data.repo.MovieRepositoryImpl
import com.turkoglu.moviecomposeapp.domain.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewAllScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val movieRepository: MovieRepositoryImpl,
) : ViewModel() {

    private val _moviesFlow = mutableStateOf<Flow<PagingData<Movie>>>(emptyFlow())
    val moviesFlow: State<Flow<PagingData<Movie>>> = _moviesFlow

    private val _screenTitle = mutableStateOf(ViewAllState())
    val screenTitle: State<ViewAllState> = _screenTitle

    private val useIncreasingPage = false

    private val genreMap = mapOf(
        "Action" to 28,
        "Aksiyon" to 28,

        "Adventure" to 12,
        "Macera" to 12,

        "Animation" to 16,
        "Animasyon" to 16,

        "Comedy" to 35,
        "Komedi" to 35,

        "Crime" to 80,
        "Suç" to 80,

        "Documentary" to 99,
        "Belgesel" to 99,

        "Drama" to 18,
        "Dram" to 18,

        "Family" to 10751,
        "Aile" to 10751,

        "Fantasy" to 14,
        "Fantastik" to 14,

        "History" to 36,
        "Tarih" to 36,

        "Horror" to 27,
        "Korku" to 27,

        "Music" to 10402,
        "Müzik" to 10402,

        "Mystery" to 9648,
        "Gizem" to 9648,

        "Romance" to 10749,
        "Romantik" to 10749,

        "Science Fiction" to 878,
        "Bilim-Kurgu" to 878,

        "TV Movie" to 10770,
        "TV film" to 10770,

        "Thriller" to 53,
        "Gerilim" to 53,

        "War" to 10752,
        "Savaş" to 10752,

        "Western" to 37,
        "Vahşi Batı" to 37
    )

    init {
        val selectedType = savedStateHandle["selectedType"] ?: ""
        _screenTitle.value = ViewAllState(movies = selectedType)
        fetchMovies(selectedType)
    }

    private fun fetchMovies(type: String) {
        val fetcher = when (type) {
            "Popular" -> movieRepository.getPopularMovies(useIncreasingPage)
            "Top Rated" -> movieRepository.getTopRatedMovies(useIncreasingPage)
            "Now Playing" -> movieRepository.getNowPlayingMovies(useIncreasingPage)
            "Upcoming" -> movieRepository.getUpcomingMovies(useIncreasingPage)
            else -> genreMap[type]?.let { genreId ->
                movieRepository.genrePager(genreId, useIncreasingPage)
            } ?: emptyFlow()
        }

        viewModelScope.launch {
            _moviesFlow.value = fetcher.cachedIn(viewModelScope)
        }
    }
}
