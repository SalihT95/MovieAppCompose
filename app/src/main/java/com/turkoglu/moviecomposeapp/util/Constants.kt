package com.turkoglu.moviecomposeapp.util

import com.turkoglu.moviecomposeapp.BuildConfig

object Constants {
    const val BASE_URL = "https://api.themoviedb.org/3/"
    val API_KEY: String = BuildConfig.TMDB_API_KEY
    const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"
    const val DEFAULT_PAGE = 1
    val supportedLanguages = listOf(
        "tr" to "Turkish",
        "en" to "English",
        "fr" to "French",
        "de" to "German",
        "es" to "Spanish",
        "it" to "Italian",
        "ru" to "Russian",
        "ja" to "Japanese",
        "zh" to "Chinese",
        "ko" to "Korean",
        "pt" to "Portuguese",
        "ar" to "Arabic",
        "hi" to "Hindi",
    )
}
