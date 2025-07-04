package com.turkoglu.moviecomposeapp.data.repo

import androidx.lifecycle.LiveData
import com.turkoglu.moviecomposeapp.data.local.Favorite
import com.turkoglu.moviecomposeapp.data.local.FavoriteDB
import javax.inject.Inject

class FavoritesRepo @Inject constructor(private val database: FavoriteDB) {
    suspend fun insertFavorite(favorite: Favorite) {
        database.dao.insertFavorite(favorite)
    }

    fun getFavorites(): LiveData<List<Favorite>> {
        return database.dao.getAllFavorites()
    }

    fun isFavorite(mediaId: Int): LiveData<Boolean> {
        return database.dao.isFavorite(mediaId)
    }

    fun getAFavorites(mediaId: Int): LiveData<Favorite?> {
        return database.dao.getAFavorites(mediaId)
    }

    suspend fun deleteOneFavorite(favorite: Favorite) {
        database.dao.deleteAFavorite(favorite)
    }

    suspend fun deleteAllFavorites() {
        database.dao.deleteAllFavorites()
    }
}