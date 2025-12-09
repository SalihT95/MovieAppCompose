import com.turkoglu.moviecomposeapp.R
import com.turkoglu.moviecomposeapp.domain.model.Genre

import com.turkoglu.moviecomposeapp.util.Constants

data class DetailState(
    val genres: List<Genre> = emptyList(),
    val imdbId: Int = 0,
    val overview: String = "",
    val popularity: Double = 0.0,
    val posterPath: String? = null,
    val backdropPath: String?= null,
    val releaseDate: String = "",
    val revenue: Int = 0,
    val title: String = "",
    val voteAverage: Double = 0.0
) {
    val safePosterPath: String
        get() = if (posterPath.isNullOrEmpty()) "${R.drawable.ic_placeholder}" else "${Constants.IMAGE_BASE_URL}/$posterPath"
    val safeBackdropPath: String
        get() = if (backdropPath.isNullOrEmpty()) "${R.drawable.ic_placeholder}" else "${Constants.IMAGE_BASE_URL}/$backdropPath"

    val safeReleaseDate: String
        get() = if (releaseDate.isBlank()) "0000-00-00" else releaseDate

    val safeGenres: String
        get() = if (genres.isEmpty()) "Unknown" else genres.joinToString { it.name }
}
