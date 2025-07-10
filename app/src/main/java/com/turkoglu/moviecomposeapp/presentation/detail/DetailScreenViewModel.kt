package com.turkoglu.moviecomposeapp.presentation.detail

import DetailState
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkoglu.moviecomposeapp.data.repo.MovieRepositoryImpl
import com.turkoglu.moviecomposeapp.domain.use_case.GetMovieDetailUseCase
import com.turkoglu.moviecomposeapp.domain.use_case.GetVideoUrlUseCase
import com.turkoglu.moviecomposeapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@HiltViewModel
class DetailScreenViewModel @Inject constructor(
    private val getDetailUseCase: GetMovieDetailUseCase,
    private val repo: MovieRepositoryImpl,
    private val getVideoUrlUseCase: GetVideoUrlUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf(DetailState())
    val state: State<DetailState> = _state

    private val _castState = mutableStateOf(CastState())
    val castState: State<CastState> = _castState

    private val _fragmanState = mutableStateOf(FragmanState())
    val fragmanState: State<FragmanState> = _fragmanState

    private val movieId: Int
        get() = savedStateHandle["movieId"] ?: 0

    init {
        getMovie()
        getCast()
        getVideoUrl()
    }

    private fun getMovie() {
        getDetailUseCase.executeGetMovieDetail(movieId).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    result.data?.let { movie ->
                        _state.value = DetailState(
                            title = movie.title,
                            overview = movie.overview,
                            genres = movie.genres,
                            imdbId = movie.id ,
                            popularity = movie.popularity,
                            posterPath = movie.posterPath,
                            releaseDate = movie.releaseDate,
                            revenue = movie.revenue,
                            voteAverage = movie.voteAverage
                        )
                    }
                }

                is Resource.Error -> {
                    println("getMovie Error: ${result.message}")
                }

                is Resource.Loading -> {

                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getCast() {
        viewModelScope.launch {
            val result = repo.getMovieCasts(movieId)
            result.data?.let { dto ->
                _castState.value = CastState(cast = dto.cast, id = dto.id)
            }
        }
    }

    private fun getVideoUrl() {
        getVideoUrlUseCase.executeGetMovieVideo(movieId).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    val videoKey = result.data?.firstOrNull()?.url
                    val videoUrl = videoKey?.let { "https://www.youtube.com/watch?v=$it" }
                    if (videoUrl != null) {
                        _fragmanState.value = FragmanState(videoUrl = videoUrl)
                    }
                }

                is Resource.Error -> {
                    println("getVideoUrl Error: ${result.message}")
                }

                is Resource.Loading -> {

                }
            }
        }.launchIn(viewModelScope)
    }
}