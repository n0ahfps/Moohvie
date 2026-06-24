package noah.moohvie.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import noah.moohvie.models.Movie
import noah.moohvie.services.AppSettings
import noah.moohvie.services.CineTableStore
import noah.moohvie.services.TMDBService

class MovieSearchViewModel(application: Application) : AndroidViewModel(application) {
    private val settings = AppSettings.getInstance(application)
    val cineTableStore = CineTableStore.getInstance(application)
    private val service = TMDBService(settings)

    var query: String by mutableStateOf("")
    var results: List<Movie> by mutableStateOf(emptyList())
        private set
    var isSearching: Boolean by mutableStateOf(false)
        private set
    var addedIDs: Set<Int> by mutableStateOf(emptySet())
        private set

    private var searchJob: Job? = null

    fun onQueryChange(newQuery: String) {
        query = newQuery
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            isSearching = true
            results = try {
                service.searchMovies(newQuery)
            } catch (e: Exception) {
                emptyList()
            }
            isSearching = false
        }
    }

    fun addToCineTable(movie: Movie) {
        cineTableStore.add(movie)
        addedIDs = addedIDs + movie.id
    }

    fun isAlreadyAdded(movieId: Int): Boolean =
        addedIDs.contains(movieId) || cineTableStore.isWatched(movieId)
}
