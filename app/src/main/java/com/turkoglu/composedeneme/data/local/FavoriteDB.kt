package com.turkoglu.composedeneme.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Favorite::class] , version = 1)
abstract class FavoriteDB : RoomDatabase(){
    abstract val dao: FavoriteDAO
}