package com.turkoglu.moviecomposeapp.presentation.cast

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkoglu.moviecomposeapp.data.remote.dto.toDomain
import com.turkoglu.moviecomposeapp.data.repo.MovieRepositoryImpl
import com.turkoglu.moviecomposeapp.domain.model.MovieCast
import com.turkoglu.moviecomposeapp.domain.model.PersonDetail
import com.turkoglu.moviecomposeapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CastUiState(
    val isLoading: Boolean = false,
    val person: PersonDetail? = null,
    val movies: List<MovieCast> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class CastScreenViewModel @Inject constructor(
    private val repo: MovieRepositoryImpl,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val personId: Int = checkNotNull(savedStateHandle["personId"])

    private val _state = MutableStateFlow(CastUiState(isLoading = true))
    val state: StateFlow<CastUiState> = _state

    init {
        fetchPersonDetails()
    }

    private fun fetchPersonDetails() {
        viewModelScope.launch {
            _state.value = CastUiState(isLoading = true)

            // Person detail
            val personResult = repo.getPersonDetail(personId)
            val personDetail = when (personResult) {
                is Resource.Success -> personResult.data?.toDomain()
                is Resource.Error -> null
                else -> null
            }
            val personError = (personResult as? Resource.Error)?.message

            // Person movies
            val movieResult = repo.getPersonMovieCredits(personId)
            val personMovies = when (movieResult) {
                is Resource.Success -> movieResult.data ?: emptyList()
                is Resource.Error -> emptyList()
                else -> emptyList()
            }
            val movieError = (movieResult as? Resource.Error)?.message

            // Final state
            _state.value = CastUiState(
                isLoading = false,
                person = personDetail,
                movies = personMovies,
                error = personError ?: movieError
            )
        }
    }
}
