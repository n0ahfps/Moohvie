package noah.moohvie.models

import java.util.Date

data class WatchedMovie(
    val id: Int,
    val title: String,
    val posterPath: String?,
    val voteAverage: Double,
    val watchedDate: Date,
    val personalRating: Int = 0,
    val genreIDs: List<Int> = emptyList(),
    val pointsEarned: Int = 0,
    val releaseDate: String? = null,
) {
    val posterUrl: String?
        get() = posterPath?.let { "https://image.tmdb.org/t/p/w500$it" }

    val primaryGenreName: String
        get() = genreIDs.firstOrNull()?.let { GenreLookup.name(forId = it) } ?: "Non classé"
}
