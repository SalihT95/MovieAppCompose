package com.turkoglu.composedeneme.presentation.viewall

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.turkoglu.composedeneme.data.repo.MovieRepositoryImpl
import com.turkoglu.composedeneme.domain.model.Movie

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@HiltViewModel
class ViewAllScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle ,
    private val movieRepository: MovieRepositoryImpl
    ):ViewModel() {
    private val _warState = mutableStateOf<Flow<PagingData<Movie>>>(emptyFlow())
    val warState : State<Flow<PagingData<Movie>>> = _warState
    private val useIncreasingPage = false

    private val _nameState = mutableStateOf(ViewAllState())
    val nameState : State<ViewAllState> = _nameState

    init {
        val selectedType=savedStateHandle.get<String>("selectedType") ?: ""
        getMovies(selectedType)
        nameState.value.movies = selectedType
    }

    private fun getMovies(selectedType : String){
        when (selectedType) {
            "Popular" -> {
                viewModelScope.launch{
                    _warState.value =movieRepository.getMovies(useIncreasingPage).cachedIn(viewModelScope)
                }
            }
            "Top Rated" -> {
                viewModelScope.launch {
                    _warState.value = movieRepository.getTopRatedMovies(useIncreasingPage).cachedIn(viewModelScope)
                }
            }
            "Now Playing" -> {
                viewModelScope.launch {
                    _warState.value = movieRepository.getNowPlayingMovies(useIncreasingPage).cachedIn(viewModelScope)
                }
            }
            "Upcoming" -> {
                viewModelScope.launch {
                    _warState.value = movieRepository.getUpcomingMovies(useIncreasingPage).cachedIn(viewModelScope)
                }
            }
            "Action" -> {
                viewModelScope.launch {
                    _warState.value = movieRepository.getActionMovies(useIncreasingPage).cachedIn(viewModelScope)
                }
            }
            "Animation" -> {
                viewModelScope.launch {
                    _warState.value = movieRepository.getAnimationMovies(useIncreasingPage).cachedIn(viewModelScope)
                }
            }
            "Comedy" -> {
                viewModelScope.launch {
                    _warState.value = movieRepository.getComedyMovies(useIncreasingPage).cachedIn(viewModelScope)
                }
            }
            "Drama" ->{
                viewModelScope.launch{
                    _warState.value = movieRepository.getDramaMovies(useIncreasingPage).cachedIn(viewModelScope)
                }
            }
            "Fantasy" -> {
                viewModelScope.launch {
                    _warState.value = movieRepository.getFantasyMovies(useIncreasingPage).cachedIn(viewModelScope)
                }
            }
            "History" -> {
                viewModelScope.launch {
                    _warState.value = movieRepository.getHistoryMovies(useIncreasingPage).cachedIn(viewModelScope)
                }
            }
            "War" -> {
                viewModelScope.launch {
                    _warState.value = movieRepository.getWarMovies(useIncreasingPage).cachedIn(viewModelScope)
                }
            }
        }
    }
}