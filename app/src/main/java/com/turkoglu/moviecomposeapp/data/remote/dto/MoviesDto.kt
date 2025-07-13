package com.turkoglu.moviecomposeapp.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.turkoglu.moviecomposeapp.domain.model.Movie
import com.turkoglu.moviecomposeapp.util.Constants.IMAGE_BASE_URL

data class MoviesDto(
    val page: Int,
    val results: List<Result>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)
fun MoviesDto.toMovieList() : List<Movie> {
    return results.map{
        Movie(it.id,it.title,it.overview, getImageUrl(it.posterPath),it.releaseDate,it.voteAverage) }
}
private fun getImageUrl(posterImage : String)=IMAGE_BASE_URL+posterImage

