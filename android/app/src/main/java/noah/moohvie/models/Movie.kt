package noah.moohvie.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("genre_ids") val genreIDs: List<Int> = emptyList(),
    @SerialName("release_date") val releaseDate: String? = null,
    @SerialName("vote_average") val voteAverage: Double = 0.0,
    val popularity: Double = 0.0,
) {
    val posterUrl: String?
        get() = posterPath?.let { "https://image.tmdb.org/t/p/w500$it" }

    /**
     * Points gagnés en ajoutant ce film au Cinétable : moins le film est populaire
     * sur TMDB, plus il est considéré "rare" et rapporte de points.
     */
    val rarityPoints: Int
        get() = when {
            popularity < 5 -> 50
            popularity < 20 -> 35
            popularity < 50 -> 20
            popularity < 100 -> 15
            else -> 10
        }
}
