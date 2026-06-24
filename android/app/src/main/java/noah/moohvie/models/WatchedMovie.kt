package noah.moohvie.models

import kotlinx.serialization.Serializable

@Serializable
data class WatchedMovie(
    val id: Int,
    val title: String,
    val posterPath: String?,
    val voteAverage: Double,
    /** Epoch millis (équivalent de `Date` côté iOS). */
    val watchedDate: Long,
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
