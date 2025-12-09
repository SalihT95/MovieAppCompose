package com.turkoglu.moviecomposeapp.presentation.detail

import DetailState
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkoglu.moviecomposeapp.data.remote.dto.toCastList
import com.turkoglu.moviecomposeapp.data.repo.MovieRepositoryImpl
import com.turkoglu.moviecomposeapp.domain.use_case.GetMovieDetailUseCase
import com.turkoglu.moviecomposeapp.domain.use_case.GetVideoUrlUseCase
import com.turkoglu.moviecomposeapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.google.firebase.auth.FirebaseAuth
import com.turkoglu.moviecomposeapp.data.repo.UserRepository
import com.turkoglu.moviecomposeapp.domain.model.Movie

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@HiltViewModel
class DetailScreenViewModel @Inject constructor(
    private val getDetailUseCase: GetMovieDetailUseCase,
    private val repo: MovieRepositoryImpl,
    private val getVideoUrlUseCase: GetVideoUrlUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _state = mutableStateOf(DetailState())
    val state: State<DetailState> = _state

    private val _castState = mutableStateOf(CastState())
    val castState: State<CastState> = _castState

    private val _fragmanState = mutableStateOf(FragmanState())
    val fragmanState: State<FragmanState> = _fragmanState

    var isFavorite = mutableStateOf(false)
        private set
    private val movieId: Int
        get() = savedStateHandle["movieId"] ?: 0

    init {
        getMovie()
        getCast()
        getVideoUrl()
        val id = savedStateHandle.get<Int>("movieId") ?: 0
        checkFavoriteStatus(id)
    }

    fun checkFavoriteStatus(id: Int) {
        val currentUser = auth.currentUser
        if (currentUser != null && id != 0) {
            viewModelScope.launch {
                isFavorite.value = userRepository.isFavorite(currentUser.uid, id)
            }
        }
    }

    fun toggleFavorite(detail: DetailState) {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            viewModelScope.launch {
                if (isFavorite.value) {
                    // Favoriden Çıkar
                    userRepository.removeFavorite(currentUser.uid, detail.imdbId)
                    isFavorite.value = false
                } else {
                    // Favoriye Ekle
                    // BURADA DÖNÜŞÜM YAPIYORUZ (DetailState -> Movie)
                    val movie = Movie(
                        id = detail.imdbId,
                        title = detail.title,
                        description = detail.overview, // API'den gelen 'overview'u senin 'description'a atadık
                        posterPath = detail.posterPath ?: "",
                        backdropPath = detail.backdropPath ?:"",
                        releaseDate = detail.releaseDate,
                        voteAverage = detail.voteAverage
                    )

                    userRepository.addFavorite(currentUser.uid, movie)
                    isFavorite.value = true
                }
            }
        }
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
                            imdbId = movie.id,
                            popularity = movie.popularity,
                            posterPath = movie.posterPath,
                            backdropPath = movie.backdropPath,
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
                _castState.value = CastState(cast = dto.toCastList(), id = dto.id)
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