package noah.moohvie.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import noah.moohvie.models.CountryProviders
import noah.moohvie.models.Movie
import noah.moohvie.services.AppSettings
import noah.moohvie.services.CineTableStore
import noah.moohvie.services.TMDBService

class SwipeViewModel(application: Application) : AndroidViewModel(application) {
    private val settings = AppSettings.getInstance(application)
    private val cineTableStore = CineTableStore.getInstance(application)
    private val service = TMDBService(settings)

    var movies: List<Movie> by mutableStateOf(emptyList())
        private set
    var currentIndex: Int by mutableStateOf(0)
        private set
    var isLoading: Boolean by mutableStateOf(false)
        private set
    var errorMessage: String? by mutableStateOf(null)
        private set

    var matchedMovie: Movie? by mutableStateOf(null)
        private set
    var matchedMovieProviders: CountryProviders? by mutableStateOf(null)
        private set
    var addedToCineTable: Boolean by mutableStateOf(false)
        private set

    fun loadMovies(quiz: QuizViewModel?) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            val providerIDs = settings.selectedProviderIDs.toList()
            val certification = settings.maxCertification
            val watchedIDs = cineTableStore.watchedIDs

            val genreIDs = quiz?.finalGenres ?: emptyList()
            val maxRuntime = quiz?.maxRuntime
            val minVoteAverage = quiz?.minVoteAverage
            val minReleaseYear = quiz?.minReleaseYear
            val maxReleaseYear = quiz?.maxReleaseYear

            // Page aléatoire parmi les 15 premières (au-delà, TMDB renvoie des films
            // trop obscurs ou peu pertinents pour des critères précis)
            val randomPage = (1..15).random()

            try {
                var results = service.discoverMovies(
                    genreIDs = genreIDs,
                    maxRuntime = maxRuntime,
                    minVoteAverage = minVoteAverage,
                    minReleaseYear = minReleaseYear,
                    maxReleaseYear = maxReleaseYear,
                    providerIDs = providerIDs,
                    maxCertification = certification,
                    page = randomPage,
                )

                if (results.isEmpty()) {
                    results = service.discoverMovies(
                        genreIDs = genreIDs,
                        providerIDs = providerIDs,
                        maxCertification = certification,
                        page = randomPage,
                    )
                }

                if (results.isEmpty() && genreIDs.isNotEmpty()) {
                    results = service.discoverMovies(
                        genreIDs = listOf(genreIDs.first()),
                        providerIDs = providerIDs,
                        maxCertification = certification,
                        page = randomPage,
                    )
                }

                if (results.isEmpty()) {
                    results = service.discoverMovies(
                        providerIDs = providerIDs,
                        maxCertification = certification,
                        page = (1..10).random(),
                    )
                }

                if (results.isEmpty()) {
                    results = service.discoverMovies()
                }

                if (!settings.allowRewatching) {
                    val filtered = results.filterNot { watchedIDs.contains(it.id) }
                    results = filtered.ifEmpty { results }
                }

                movies = results.shuffled()
            } catch (e: Exception) {
                errorMessage = "Impossible de charger les films. Vérifie ta connexion."
            }

            isLoading = false
        }
    }

    val currentMovie: Movie?
        get() = movies.getOrNull(currentIndex)

    val isFinished: Boolean
        get() = currentIndex >= movies.size

    fun selectThisMovie() {
        currentMovie?.let { applyMatch(it) }
    }

    fun pickRandom() {
        movies.drop(currentIndex).randomOrNull()?.let { applyMatch(it) }
    }

    private fun applyMatch(movie: Movie) {
        matchedMovie = movie
        viewModelScope.launch {
            matchedMovieProviders = try {
                service.watchProviders(movie.id)
            } catch (e: Exception) {
                null
            }
        }
    }

    fun skip() {
        currentIndex += 1
    }

    fun confirmWatched() {
        val movie = matchedMovie ?: return
        cineTableStore.add(movie)
        addedToCineTable = true
    }

    fun reset() {
        currentIndex = 0
        matchedMovie = null
        matchedMovieProviders = null
        addedToCineTable = false
    }
}
