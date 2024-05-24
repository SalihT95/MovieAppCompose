package com.turkoglu.composedeneme.data.remote.dto

import com.turkoglu.composedeneme.domain.model.MovieDetail
import com.turkoglu.composedeneme.util.Constants

data class MovieDetailDto(
    val adult: Boolean,
    val backdrop_path: String,
    val belongs_to_collection: BelongsToCollection,
    val budget: Int,
    val genres: List<Genre>,
    val homepage: String,
    val id: Int,
    val imdb_id: String,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val production_companies: List<ProductionCompany>,
    val production_countries: List<ProductionCountry>,
    val release_date: String,
    val revenue: Int,
    val runtime: Int,
    val spoken_languages: List<SpokenLanguage>,
    val status: String,
    val tagline: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
)
fun MovieDetailDto.toMovie() :MovieDetail{
    return MovieDetail(adult,backdrop_path, genres,id,overview,popularity, getImageUrl(poster_path),release_date,revenue,title,vote_average)
}
private fun getImageUrl(posterImage : String)= Constants.IMAGE_BASE_URL +posterImage

