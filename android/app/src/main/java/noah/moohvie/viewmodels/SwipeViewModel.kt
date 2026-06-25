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
import noah.moohvie.services.SkipHistoryStore
import noah.moohvie.services.TMDBService

class SwipeViewModel(application: Application) : AndroidViewModel(application) {
    private val settings = AppSettings.getInstance(application)
    private val cineTableStore = CineTableStore.getInstance(application)
    private val skipHistoryStore = SkipHistoryStore.getInstance(application)
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
    var criteriaRelaxed: Boolean by mutableStateOf(false)
        private set

    fun loadMovies(quiz: QuizViewModel?) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            criteriaRelaxed = false

            val providerIDs = settings.selectedProviderIDs.toList()
            val certification = settings.maxCertification
            val watchedIDs = cineTableStore.watchedIDs

            val genreIDs = quiz?.finalGenres ?: emptyList()
            val excludedGenreIDs = quiz?.finalExcludedGenres ?: emptyList()
            val maxRuntime = quiz?.maxRuntime
            val minVoteAverage = quiz?.minVoteAverage
            val minReleaseYear = quiz?.minReleaseYear
            val maxReleaseYear = quiz?.maxReleaseYear

            // Pages 1-5 pour les requêtes précises : au-delà, TMDB trie par popularité
            // décroissante et les résultats perdent en pertinence par rapport aux genres.
            val strictPage = (1..5).random()

            try {
                var results = service.discoverMovies(
                    genreIDs = genreIDs,
                    genresToExclude = excludedGenreIDs,
                    maxRuntime = maxRuntime,
                    minVoteAverage = minVoteAverage,
                    minReleaseYear = minReleaseYear,
                    maxReleaseYear = maxReleaseYear,
                    providerIDs = providerIDs,
                    maxCertification = certification,
                    page = strictPage,
                )

                if (results.isEmpty()) {
                    criteriaRelaxed = true
                    results = service.discoverMovies(
                        genreIDs = genreIDs,
                        genresToExclude = excludedGenreIDs,
                        providerIDs = providerIDs,
                        maxCertification = certification,
                        page = strictPage,
                    )
                }

                if (results.isEmpty() && genreIDs.isNotEmpty()) {
                    results = service.discoverMovies(
                        genreIDs = listOf(genreIDs.first()),
                        genresToExclude = excludedGenreIDs,
                        providerIDs = providerIDs,
                        maxCertification = certification,
                        page = strictPage,
                    )
                }

                if (results.isEmpty()) {
                    results = service.discoverMovies(
                        genresToExclude = excludedGenreIDs,
                        providerIDs = providerIDs,
                        maxCertification = certification,
                        page = (1..8).random(),
                    )
                }

                if (results.isEmpty()) {
                    results = service.discoverMovies()
                }

                if (!settings.allowRewatching) {
                    val filtered = results.filterNot { watchedIDs.contains(it.id) }
                    results = filtered.ifEmpty { results }
                }

                movies = sortedByRelevance(results, genreIDs)
            } catch (e: Exception) {
                errorMessage = "Impossible de charger les films. Vérifie ta connexion."
            }

            isLoading = false
        }
    }

    private fun sortedByRelevance(movies: List<Movie>, wantedGenres: List<Int>): List<Movie> {
        val wanted = wantedGenres.toSet()
        val skipWeights = skipHistoryStore.skippedGenreWeights
        val cinetableWeights = cineTableStore.favoriteGenreWeights()
        if (wanted.isEmpty() && skipWeights.isEmpty() && cinetableWeights.isEmpty()) return movies.shuffled()
        return movies.sortedByDescending { movie -> relevanceScore(movie, wanted, skipWeights, cinetableWeights) }
    }

    private fun relevanceScore(movie: Movie, wanted: Set<Int>, skipWeights: Map<Int, Double>, cinetableWeights: Map<Int, Double>): Double {
        val match = movie.genreIDs.count { it in wanted }.toDouble()
        // 0.5 factor keeps quiz signal dominant over history
        val bonus = movie.genreIDs.sumOf { (cinetableWeights[it] ?: 0.0) * 0.5 }
        val penalty = movie.genreIDs.sumOf { skipWeights[it] ?: 0.0 }
        return match + bonus - penalty
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
        currentMovie?.let { skipHistoryStore.record(it) }
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
