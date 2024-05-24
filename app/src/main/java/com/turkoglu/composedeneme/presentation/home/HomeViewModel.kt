package com.turkoglu.composedeneme.presentation.home

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.turkoglu.composedeneme.data.remote.dto.Genre
import com.turkoglu.composedeneme.data.repo.MovieRepositoryImpl
import com.turkoglu.composedeneme.domain.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val movieRepository: MovieRepositoryImpl
) : ViewModel() {
    private val useIncreasingPage = true
    private val _popularState = mutableStateOf<Flow<PagingData<Movie>>>(emptyFlow())
    val popularState : State<Flow<PagingData<Movie>>> = _popularState
    private val _topratedState = mutableStateOf<Flow<PagingData<Movie>>>(emptyFlow())
    val topretedState : State<Flow<PagingData<Movie>>> = _topratedState
    private val _nowPlayingState = mutableStateOf<Flow<PagingData<Movie>>>(emptyFlow())
    val notPlayingState : State<Flow<PagingData<Movie>>> = _nowPlayingState
    private val _upCommingState = mutableStateOf<Flow<PagingData<Movie>>>(emptyFlow())
    val upCommingState : State<Flow<PagingData<Movie>>> = _upCommingState
    private val _actionState = mutableStateOf<Flow<PagingData<Movie>>>(emptyFlow())
    val actionState : State<Flow<PagingData<Movie>>> = _actionState
    private val _animationState = mutableStateOf<Flow<PagingData<Movie>>>(emptyFlow())
    val animationState : State<Flow<PagingData<Movie>>> = _animationState
    private val _comedyState = mutableStateOf<Flow<PagingData<Movie>>>(emptyFlow())
    val comedystate : State<Flow<PagingData<Movie>>> = _comedyState
    private val _dramaState = mutableStateOf<Flow<PagingData<Movie>>>(emptyFlow())
    val dramaState : State<Flow<PagingData<Movie>>> = _dramaState
    private val _fantasyState = mutableStateOf<Flow<PagingData<Movie>>>(emptyFlow())
    val fantasyState : State<Flow<PagingData<Movie>>> = _fantasyState
    private val _historyState = mutableStateOf<Flow<PagingData<Movie>>>(emptyFlow())
    val historyState : State<Flow<PagingData<Movie>>> = _historyState
    private val _warState = mutableStateOf<Flow<PagingData<Movie>>>(emptyFlow())
    val warState : State<Flow<PagingData<Movie>>> = _warState

    private val _moviesGenres = mutableStateOf<List<Genre>>(emptyList())
    val moviesGenres: State<List<Genre>> = _moviesGenres


    init {
        getPopularMovies()
        getTopRatedMovies()
        getNowPlayingMovies()
        getUpCommingMovies()
        getActionMovies()
        getAnimationMovies()
        getComedyMovies()
        getDramaMovies()
        getFantasyMovies()
        getHistoryMovies()
        getWarMovies()
    }




    private fun getPopularMovies(){
        viewModelScope.launch{
            _popularState.value =movieRepository.getMovies(useIncreasingPage).cachedIn(viewModelScope)
        }
    }
    private fun getTopRatedMovies(){
        viewModelScope.launch {
            _topratedState.value = movieRepository.getTopRatedMovies(useIncreasingPage).cachedIn(viewModelScope)
        }
    }
    private fun getNowPlayingMovies(){
        viewModelScope.launch {
            _nowPlayingState.value = movieRepository.getNowPlayingMovies(useIncreasingPage).cachedIn(viewModelScope)
        }
    }
    private fun getUpCommingMovies(){
        viewModelScope.launch {
            _upCommingState.value = movieRepository.getUpcomingMovies(useIncreasingPage).cachedIn(viewModelScope)
        }
    }
    private fun getActionMovies(){
        viewModelScope.launch {
            _actionState.value = movieRepository.getActionMovies(useIncreasingPage).cachedIn(viewModelScope)
        }
    }
    private fun getAnimationMovies(){
        viewModelScope.launch {
            _animationState.value = movieRepository.getAnimationMovies(useIncreasingPage).cachedIn(viewModelScope)
        }
    }
    private fun getComedyMovies(){
        viewModelScope.launch {
            _comedyState.value = movieRepository.getComedyMovies(useIncreasingPage).cachedIn(viewModelScope)
        }
    }
    private fun getDramaMovies(){
       viewModelScope.launch {
           _dramaState.value = movieRepository.getDramaMovies(useIncreasingPage).cachedIn(viewModelScope)
        }
    }
    private fun getFantasyMovies(){
        viewModelScope.launch {
            _fantasyState.value = movieRepository.getFantasyMovies(useIncreasingPage).cachedIn(viewModelScope)
        }
    }
    private fun getHistoryMovies(){
        viewModelScope.launch {
            _historyState.value = movieRepository.getHistoryMovies(useIncreasingPage).cachedIn(viewModelScope)
        }
    }
    private fun getWarMovies(){
        viewModelScope.launch {
            _warState.value = movieRepository.getWarMovies(useIncreasingPage).cachedIn(viewModelScope)
        }
    }
    /*
    private fun getMoviesGenres() {
        viewModelScope.launch {
            when (val result = genresRepository.getMoviesGenres()) {
                is Resource.Success -> {
                    _moviesGenres.value = result.data?.genres!!
                }
                is Resource.Error -> {
                    //loadingError.value = result.message.toString()
                }
                else -> {}
            }
        }
    }
    // Genleri getirmek istiyosak search ekranında eklememiz lazım gen isteğini
     */
}

