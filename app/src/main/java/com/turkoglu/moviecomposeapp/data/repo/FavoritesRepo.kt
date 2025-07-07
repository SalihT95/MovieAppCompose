package com.turkoglu.moviecomposeapp.data.repo

import com.turkoglu.moviecomposeapp.data.local.Favorite
import com.turkoglu.moviecomposeapp.data.local.FavoriteDB
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoritesRepo @Inject constructor(private val database: FavoriteDB) {

    fun getFavorites(): Flow<List<Favorite>> {
        return database.dao.getAllFavorites()
    }

    fun getAFavorite(mediaId: Int): Flow<Favorite?> {
        return database.dao.getAFavorite(mediaId)
    }

    suspend fun insertFavorite(favorite: Favorite) {
        database.dao.insertFavorite(favorite)
    }

    suspend fun deleteOneFavorite(favorite: Favorite) {
        database.dao.deleteAFavorite(favorite)
    }

    suspend fun deleteAllFavorites() {
        database.dao.deleteAllFavorites()
    }
}