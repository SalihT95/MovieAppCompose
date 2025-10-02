package com.turkoglu.moviecomposeapp.presentation.home

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.turkoglu.moviecomposeapp.data.repo.GenreRepository
import com.turkoglu.moviecomposeapp.data.repo.MovieRepositoryImpl
import com.turkoglu.moviecomposeapp.domain.model.Genre
import com.turkoglu.moviecomposeapp.domain.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val movieRepository: MovieRepositoryImpl,
    private val genreRepository: GenreRepository
) : ViewModel() {

    private val useIncreasingPage = true

    private val _popularState = mutableStateOf<Flow<PagingData<Movie>>>(emptyFlow())
    val popularState: State<Flow<PagingData<Movie>>> = _popularState

    private val _topRatedState = mutableStateOf<Flow<PagingData<Movie>>>(emptyFlow())
    val topRatedState: State<Flow<PagingData<Movie>>> = _topRatedState

    private val _nowPlayingState = mutableStateOf<Flow<PagingData<Movie>>>(emptyFlow())
    val nowPlayingState: State<Flow<PagingData<Movie>>> = _nowPlayingState

    private val _upComingState = mutableStateOf<Flow<PagingData<Movie>>>(emptyFlow())
    val upComingState: State<Flow<PagingData<Movie>>> = _upComingState

    private val _genres = mutableStateOf<Flow<List<Genre>>>(emptyFlow())
    val genres: State<Flow<List<Genre>>> = _genres

    private val _actionState = mutableStateOf<Flow<PagingData<Movie>>>(emptyFlow())

    private val _animationState = mutableStateOf<Flow<PagingData<Movie>>>(emptyFlow())

    private val _comedyState = mutableStateOf<Flow<PagingData<Movie>>>(emptyFlow())

    private val _dramaState = mutableStateOf<Flow<PagingData<Movie>>>(emptyFlow())

    private val _fantasyState = mutableStateOf<Flow<PagingData<Movie>>>(emptyFlow())

    private val _historyState = mutableStateOf<Flow<PagingData<Movie>>>(emptyFlow())

    private val _warState = mutableStateOf<Flow<PagingData<Movie>>>(emptyFlow())

    private val _adventureState = mutableStateOf<Flow<PagingData<Movie>>>(emptyFlow())

    private val _crimeState = mutableStateOf<Flow<PagingData<Movie>>>(emptyFlow())

    private val _documentaryState = mutableStateOf<Flow<PagingData<Movie>>>(emptyFlow())

    private val _familyState = mutableStateOf<Flow<PagingData<Movie>>>(emptyFlow())

    private val _horrorState = mutableStateOf<Flow<PagingData<Movie>>>(emptyFlow())

    private val _musicState = mutableStateOf<Flow<PagingData<Movie>>>(emptyFlow())

    private val _mysteryState = mutableStateOf<Flow<PagingData<Movie>>>(emptyFlow())

    private val _romanceState = mutableStateOf<Flow<PagingData<Movie>>>(emptyFlow())

    private val _scienceFictionState = mutableStateOf<Flow<PagingData<Movie>>>(emptyFlow())

    private val _tvMovieState = mutableStateOf<Flow<PagingData<Movie>>>(emptyFlow())

    private val _thrillerState = mutableStateOf<Flow<PagingData<Movie>>>(emptyFlow())

    private val _westernState = mutableStateOf<Flow<PagingData<Movie>>>(emptyFlow())

    init {
        loadMovies()
    }

    private fun loadMovies() {
        getGenres()
        getPopularMovies()
        getTopRatedMovies()
        getNowPlayingMovies()
        getUpcomingMovies()
        getActionMovies()
        getAdventureMovies()
        getAnimationMovies()
        getComedyMovies()
        getCrimeMovies()
        getDocumentaryMovies()
        getDramaMovies()
        getFamilyMovies()
        getFantasyMovies()
        getHistoryMovies()
        getHorrorMovies()
        getMusicMovies()
        getMysteryMovies()
        getRomanceMovies()
        getScienceFictionMovies()
        getTvMovieMovies()
        getThrillerMovies()
        getWarMovies()
        getWesternMovies()
    }

    private fun getGenres() = viewModelScope.launch {
        _genres.value = genreRepository.getGenres()
    }

    private fun getPopularMovies() = viewModelScope.launch {
        _popularState.value =
            movieRepository.getPopularMovies(useIncreasingPage).cachedIn(viewModelScope)
    }

    private fun getTopRatedMovies() = viewModelScope.launch {
        _topRatedState.value =
            movieRepository.getTopRatedMovies(useIncreasingPage).cachedIn(viewModelScope)
    }

    private fun getNowPlayingMovies() = viewModelScope.launch {
        _nowPlayingState.value =
            movieRepository.getNowPlayingMovies(useIncreasingPage).cachedIn(viewModelScope)
    }

    private fun getUpcomingMovies() = viewModelScope.launch {
        _upComingState.value =
            movieRepository.getUpcomingMovies(useIncreasingPage).cachedIn(viewModelScope)
    }

    private fun getActionMovies() = viewModelScope.launch {
        _actionState.value =
            movieRepository.getActionMovies(useIncreasingPage).cachedIn(viewModelScope)
    }

    private fun getAnimationMovies() = viewModelScope.launch {
        _animationState.value =
            movieRepository.getAnimationMovies(useIncreasingPage).cachedIn(viewModelScope)
    }

    private fun getComedyMovies() = viewModelScope.launch {
        _comedyState.value =
            movieRepository.getComedyMovies(useIncreasingPage).cachedIn(viewModelScope)
    }

    private fun getDramaMovies() = viewModelScope.launch {
        _dramaState.value =
            movieRepository.getDramaMovies(useIncreasingPage).cachedIn(viewModelScope)
    }

    private fun getFantasyMovies() = viewModelScope.launch {
        _fantasyState.value =
            movieRepository.getFantasyMovies(useIncreasingPage).cachedIn(viewModelScope)
    }

    private fun getHistoryMovies() = viewModelScope.launch {
        _historyState.value =
            movieRepository.getHistoryMovies(useIncreasingPage).cachedIn(viewModelScope)
    }

    private fun getWarMovies() = viewModelScope.launch {
        _warState.value = movieRepository.getWarMovies(useIncreasingPage).cachedIn(viewModelScope)
    }

    private fun getAdventureMovies() = viewModelScope.launch {
        _adventureState.value =
            movieRepository.getAdventureMovies(useIncreasingPage).cachedIn(viewModelScope)
    }

    private fun getCrimeMovies() = viewModelScope.launch {
        _crimeState.value =
            movieRepository.getCrimeMovies(useIncreasingPage).cachedIn(viewModelScope)
    }

    private fun getDocumentaryMovies() = viewModelScope.launch {
        _documentaryState.value =
            movieRepository.getDocumentaryMovies(useIncreasingPage).cachedIn(viewModelScope)
    }

    private fun getFamilyMovies() = viewModelScope.launch {
        _familyState.value =
            movieRepository.getFamilyMovies(useIncreasingPage).cachedIn(viewModelScope)
    }

    private fun getHorrorMovies() = viewModelScope.launch {
        _horrorState.value =
            movieRepository.getHorrorMovies(useIncreasingPage).cachedIn(viewModelScope)
    }

    private fun getMusicMovies() = viewModelScope.launch {
        _musicState.value =
            movieRepository.getMusicMovies(useIncreasingPage).cachedIn(viewModelScope)
    }

    private fun getMysteryMovies() = viewModelScope.launch {
        _mysteryState.value =
            movieRepository.getMysteryMovies(useIncreasingPage).cachedIn(viewModelScope)
    }

    private fun getRomanceMovies() = viewModelScope.launch {
        _romanceState.value =
            movieRepository.getRomanceMovies(useIncreasingPage).cachedIn(viewModelScope)
    }

    private fun getScienceFictionMovies() = viewModelScope.launch {
        _scienceFictionState.value =
            movieRepository.getScienceFictionMovies(useIncreasingPage).cachedIn(viewModelScope)
    }

    private fun getTvMovieMovies() = viewModelScope.launch {
        _tvMovieState.value =
            movieRepository.getTvMovieMovies(useIncreasingPage).cachedIn(viewModelScope)
    }

    private fun getThrillerMovies() = viewModelScope.launch {
        _thrillerState.value =
            movieRepository.getThrillerMovies(useIncreasingPage).cachedIn(viewModelScope)
    }

    private fun getWesternMovies() = viewModelScope.launch {
        _westernState.value =
            movieRepository.getWesternMovies(useIncreasingPage).cachedIn(viewModelScope)
    }
}