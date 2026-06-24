package noah.moohvie.services

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import noah.moohvie.models.Movie
import noah.moohvie.models.WatchedMovie

class CineTableStore private constructor(context: Context) {
    private val prefs = context.getSharedPreferences("moohvie_settings", Context.MODE_PRIVATE)
    private val pointsStore = MoohPointsStore.getInstance(context)
    private val storageKey = "cineTableWatchedMovies"
    private val json = Json { ignoreUnknownKeys = true }

    var watchedMovies: List<WatchedMovie> by mutableStateOf(load())
        private set

    private fun load(): List<WatchedMovie> {
        val raw = prefs.getString(storageKey, null) ?: return emptyList()
        return try {
            json.decodeFromString<List<WatchedMovie>>(raw)
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun save() {
        prefs.edit().putString(storageKey, json.encodeToString(watchedMovies)).apply()
    }

    fun add(movie: Movie) {
        if (isWatched(movie.id)) return
        val points = movie.rarityPoints
        val entry = WatchedMovie(
            id = movie.id,
            title = movie.title,
            posterPath = movie.posterPath,
            voteAverage = movie.voteAverage,
            watchedDate = System.currentTimeMillis(),
            genreIDs = movie.genreIDs,
            pointsEarned = points,
            releaseDate = movie.releaseDate,
        )
        watchedMovies = listOf(entry) + watchedMovies
        save()
        pointsStore.award(points)
    }

    fun isWatched(movieId: Int): Boolean = watchedMovies.any { it.id == movieId }

    val watchedIDs: Set<Int>
        get() = watchedMovies.map { it.id }.toSet()

    fun remove(movieId: Int) {
        watchedMovies.firstOrNull { it.id == movieId }?.let { pointsStore.refund(it.pointsEarned) }
        watchedMovies = watchedMovies.filterNot { it.id == movieId }
        save()
    }

    fun setRating(movieId: Int, rating: Int) {
        watchedMovies = watchedMovies.map { if (it.id == movieId) it.copy(personalRating = rating) else it }
        save()
    }

    companion object {
        @Volatile private var instance: CineTableStore? = null

        fun getInstance(context: Context): CineTableStore =
            instance ?: synchronized(this) {
                instance ?: CineTableStore(context.applicationContext).also { instance = it }
            }
    }
}
