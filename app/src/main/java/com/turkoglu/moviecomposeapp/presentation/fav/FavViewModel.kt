package com.turkoglu.moviecomposeapp.presentation.fav

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.turkoglu.moviecomposeapp.data.local.Favorite
import com.turkoglu.moviecomposeapp.data.repo.FavoritesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavViewModel @Inject constructor(
    private val repo: FavoritesRepo,
) : ViewModel() {
    val favorites = repo.getFavorites()

    fun insertFavorite(favorite: Favorite) {
        viewModelScope.launch {
            repo.insertFavorite(favorite)
        }
    }

    fun isAFavorite(mediaId: Int): LiveData<Boolean> {
        return repo.isFavorite(mediaId)
    }

    fun deleteOneFavorite(favorite: Favorite) {
        viewModelScope.launch {
            repo.deleteOneFavorite(favorite)
        }
    }

    fun deleteAllFavorites() {
        viewModelScope.launch {
            repo.deleteAllFavorites()
        }
    }
}