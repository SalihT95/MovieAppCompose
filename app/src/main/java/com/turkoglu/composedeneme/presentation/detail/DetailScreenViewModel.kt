package com.turkoglu.composedeneme.presentation.detail

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkoglu.composedeneme.data.repo.MovieRepositoryImpl
import com.turkoglu.composedeneme.domain.use_case.GetMovieDetailUseCase
import com.turkoglu.composedeneme.domain.use_case.GetVideoUrlUseCase
import com.turkoglu.composedeneme.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@HiltViewModel
class DetailScreenViewModel @Inject constructor(
    private val getDetailUseCase : GetMovieDetailUseCase,
    private val repo : MovieRepositoryImpl ,
    private val getVideoUrlUseCase: GetVideoUrlUseCase,
    private val savedStateHandle: SavedStateHandle
):ViewModel(){
    private val _state = mutableStateOf(DetailState())
    val state: State<DetailState> = _state
    private val _castState = mutableStateOf(CastState())
    val castState : State<CastState> = _castState
    private val _fragmanState = mutableStateOf(FragmanState())
    val fragmanState: State<FragmanState> = _fragmanState

    init {
        getMovie()
        getCast()
        getVideoUrl()

    }
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    private fun getMovie(){
        val movieId = savedStateHandle.get<Int>("movieId") ?: 0
        getDetailUseCase.executeGetMovieDetail(movieId = movieId).onEach {  
            when (it) {
                is Resource.Success -> {
                    _state.value= DetailState(title = it.data!!.title, overview = it.data.overview, genres = it.data.genres,
                        imdbId = it.data.id, popularity = it.data.popularity, posterPath = it.data
                            .posterPath, releaseDate = it.data.releaseDate, revenue = it.data.revenue, voteAverage = it.data.voteAverage)

                }

                is Resource.Error -> {
                    println("Err")
                    //_state.value = HomeScreenState(errorMessage = it.message ?: "Error!")
                }

                is Resource.Loading -> {
                    //_state.value = HomeScreenState(loading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

   private fun getCast(){
       val movieId = savedStateHandle.get<Int>("movieId") ?: 0
       viewModelScope.launch {
           val dto=repo.getMovieCasts(movieId).data
           _castState.value = CastState(dto!!.cast,dto.id)
       }
    }
    private fun getVideoUrl() {
        val movieId = savedStateHandle.get<Int>("movieId") ?: 0

        getVideoUrlUseCase.executeGetMovieVideo(movieId = movieId).onEach {result->
            println("getVideoUrl = ${result.data!!.firstOrNull()?.url}")
            val url = "https://www.youtube.com/watch?v=" +result.data.firstOrNull()?.url
            when (result) {
                is Resource.Success -> {
                    _fragmanState.value = FragmanState(videoUrl = url)
                }

                is Resource.Error -> {
                    println("Err")
                    //_state.value = HomeScreenState(errorMessage = it.message ?: "Error!")
                }

                is Resource.Loading -> {
                    //_state.value = HomeScreenState(loading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

}