package com.turkoglu.moviecomposeapp.data.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.turkoglu.moviecomposeapp.data.local.AppDatabase
import com.turkoglu.moviecomposeapp.data.local.UserPrefs
import com.turkoglu.moviecomposeapp.data.remote.AuthLanguageInterceptor
import com.turkoglu.moviecomposeapp.data.remote.MovieAPI
import com.turkoglu.moviecomposeapp.util.Constants.BASE_URL
import com.turkoglu.moviecomposeapp.util.LanguagePreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthLanguageInterceptor { LanguagePreference.selectedLanguage })
            .build()
    }

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
    fun provideAppDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application.applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideUserPrefs(@ApplicationContext context: Context): UserPrefs = UserPrefs(context)
}
