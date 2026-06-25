package noah.moohvie.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import noah.moohvie.models.CineTableDisplayMode
import noah.moohvie.models.CineTableFilterState
import noah.moohvie.models.GenreLookup
import noah.moohvie.models.WatchedMovie
import noah.moohvie.services.CineTableStore

data class CineTableStats(
    val totalMovies: Int,
    val favoriteGenreName: String?,
    val averagePersonalRating: Double?,
    val topRatedMovie: WatchedMovie?,
    val moviesPerMonth: Double?,
)

class CineTableViewModel(application: Application) : AndroidViewModel(application) {
    val store = CineTableStore.getInstance(application)

    var displayMode: CineTableDisplayMode by mutableStateOf(CineTableDisplayMode.GRID)
    var filters: CineTableFilterState by mutableStateOf(CineTableFilterState())

    val displayedMovies: List<WatchedMovie>
        get() = filters.apply(to = store.watchedMovies)

    val stats: CineTableStats
        get() {
            val movies = store.watchedMovies

            val genreCounts = mutableMapOf<Int, Int>()
            for (m in movies) for (g in m.genreIDs) genreCounts[g] = (genreCounts[g] ?: 0) + 1
            val favoriteGenreName = genreCounts.maxByOrNull { it.value }?.key?.let { GenreLookup.name(it) }

            val rated = movies.filter { it.personalRating > 0 }
            val avgRating = if (rated.isEmpty()) null else rated.sumOf { it.personalRating }.toDouble() / rated.size
            val topMovie = rated.maxByOrNull { it.personalRating }

            var moviesPerMonth: Double? = null
            if (movies.size >= 2) {
                val dates = movies.map { it.watchedDate }
                val earliestMs = dates.min()
                val latestMs = dates.max()
                val months = maxOf(1, ((latestMs - earliestMs) / (1000L * 60 * 60 * 24 * 30)).toInt())
                moviesPerMonth = movies.size.toDouble() / months
            }

            return CineTableStats(
                totalMovies = movies.size,
                favoriteGenreName = favoriteGenreName,
                averagePersonalRating = avgRating,
                topRatedMovie = topMovie,
                moviesPerMonth = moviesPerMonth,
            )
        }

    fun remove(movieId: Int) = store.remove(movieId)

    fun setRating(movieId: Int, rating: Int) = store.setRating(movieId, rating)
}
