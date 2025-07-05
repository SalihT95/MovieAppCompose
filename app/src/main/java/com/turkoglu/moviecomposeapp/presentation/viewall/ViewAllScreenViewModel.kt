package com.turkoglu.moviecomposeapp.presentation.viewall

import android.os.Build
import androidx.annotation.RequiresExtension
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

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@HiltViewModel
class ViewAllScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val movieRepository: MovieRepositoryImpl
) : ViewModel() {

    private val _moviesFlow = mutableStateOf<Flow<PagingData<Movie>>>(emptyFlow())
    val moviesFlow: State<Flow<PagingData<Movie>>> = _moviesFlow

    private val _screenTitle = mutableStateOf(ViewAllState())
    val screenTitle: State<ViewAllState> = _screenTitle

    private val useIncreasingPage = false

    init {
        val selectedType = savedStateHandle["selectedType"] ?: ""
        _screenTitle.value = ViewAllState(movies = selectedType)
        fetchMovies(selectedType)
    }

    private fun fetchMovies(type: String) {
        val fetcher: suspend () -> Flow<PagingData<Movie>> = when (type) {
            "Popular" -> { { movieRepository.getMovies(useIncreasingPage) } }
            "Top Rated" -> { { movieRepository.getTopRatedMovies(useIncreasingPage) } }
            "Now Playing" -> { { movieRepository.getNowPlayingMovies(useIncreasingPage) } }
            "Upcoming" -> { { movieRepository.getUpcomingMovies(useIncreasingPage) } }
            "Action" -> { { movieRepository.getActionMovies(useIncreasingPage) } }
            "Animation" -> { { movieRepository.getAnimationMovies(useIncreasingPage) } }
            "Comedy" -> { { movieRepository.getComedyMovies(useIncreasingPage) } }
            "Drama" -> { { movieRepository.getDramaMovies(useIncreasingPage) } }
            "Fantasy" -> { { movieRepository.getFantasyMovies(useIncreasingPage) } }
            "History" -> { { movieRepository.getHistoryMovies(useIncreasingPage) } }
            "War" -> { { movieRepository.getWarMovies(useIncreasingPage) } }
            else -> { { emptyFlow() } }
        }

        viewModelScope.launch {
            _moviesFlow.value = fetcher().cachedIn(viewModelScope)
        }
    }
}
