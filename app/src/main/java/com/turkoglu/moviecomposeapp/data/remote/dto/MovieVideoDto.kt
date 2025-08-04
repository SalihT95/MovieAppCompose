package com.turkoglu.moviecomposeapp.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.turkoglu.moviecomposeapp.domain.model.VideoModel


data class MovieVideoDto(
    val id: Int,
    val results: List<Video>
)
data class Video(
    val id: String,
    @SerializedName("iso_639_1")
    val language: String,
    @SerializedName("iso_3166_1")
    val region: String,
    val name : String,
    val key: String, // wPe6BNPUmI0
    val site: String,
    val size: Int,
    val type: String,
    val official: Boolean,
    @SerializedName("published_at")
    val publishedAt: String?
)

fun MovieVideoDto.toVideo() : List<VideoModel> {
    return results.map {
        VideoModel(it.key)
    }
}