package com.turkoglu.composedeneme.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.turkoglu.composedeneme.domain.model.Movie
import com.turkoglu.composedeneme.util.Constants.IMAGE_BASE_URL

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
        Movie(it.id,it.title,it.overview, getImageUrl(it.posterPath),it.releaseDate) }
}
private fun getImageUrl(posterImage : String)=IMAGE_BASE_URL+posterImage

