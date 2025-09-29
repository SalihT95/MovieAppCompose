package com.turkoglu.moviecomposeapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.turkoglu.moviecomposeapp.domain.model.Favorite
import com.turkoglu.moviecomposeapp.domain.model.UserAccount

@Database(entities = [Favorite::class, UserAccount::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract val favoriteDao: FavoriteDAO
    abstract val userAccountDao: UserAccountDAO
}