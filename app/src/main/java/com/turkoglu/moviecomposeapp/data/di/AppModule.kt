package com.turkoglu.moviecomposeapp.data.di

import android.app.Application
import androidx.room.Room
import com.turkoglu.moviecomposeapp.data.local.FavoriteDB
import com.turkoglu.moviecomposeapp.data.remote.AuthLanguageInterceptor
import com.turkoglu.moviecomposeapp.data.remote.MovieAPI
import com.turkoglu.moviecomposeapp.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Interceptor'ı ekleyen OkHttpClient
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthLanguageInterceptor())
            .build()
    }

    // OkHttpClient kullanan Retrofit + API
    @Provides
    @Singleton
    fun provideMovieApi(client: OkHttpClient): MovieAPI {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideFavoritesDatabase(application: Application): FavoriteDB {
        return Room.databaseBuilder(
            application.applicationContext,
            FavoriteDB::class.java,
            "favorites_database"
        ).fallbackToDestructiveMigration().build()
    }
}
