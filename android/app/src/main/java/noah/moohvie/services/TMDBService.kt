package noah.moohvie.services

import kotlinx.serialization.Serializable
import noah.moohvie.BuildConfig
import noah.moohvie.models.CountryProviders
import noah.moohvie.models.FrenchCertification
import noah.moohvie.models.Movie
import noah.moohvie.models.WatchProvidersResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap
import okhttp3.MediaType.Companion.toMediaType
import kotlinx.serialization.json.Json

sealed class TMDBException(message: String) : Exception(message) {
    object RequestFailed : TMDBException("TMDB request failed")
    object DecodingFailed : TMDBException("TMDB response decoding failed")
}

@Serializable
private data class TMDBResponse(val results: List<Movie> = emptyList())

private interface TMDBApi {
    @GET("discover/movie")
    suspend fun discoverMovies(@QueryMap params: Map<String, String>): TMDBResponse

    @GET("movie/{movieId}/watch/providers")
    suspend fun watchProviders(
        @Path("movieId") movieId: Int,
        @QueryMap params: Map<String, String>,
    ): WatchProvidersResponse

    @GET("search/movie")
    suspend fun searchMovies(@QueryMap params: Map<String, String>): TMDBResponse
}

class TMDBService(private val appSettings: AppSettings) {
    private val apiKey = BuildConfig.TMDB_API_KEY

    private val api: TMDBApi by lazy {
        val json = Json { ignoreUnknownKeys = true }
        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
            .build()
        Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(TMDBApi::class.java)
    }

    suspend fun discoverMovies(
        genreIDs: List<Int> = emptyList(),
        genresToExclude: List<Int> = emptyList(),
        maxRuntime: Int? = null,
        minVoteAverage: Double? = null,
        minReleaseYear: Int? = null,
        maxReleaseYear: Int? = null,
        providerIDs: List<Int> = emptyList(),
        maxCertification: FrenchCertification = FrenchCertification.ALL,
        page: Int = 1,
    ): List<Movie> {
        val params = mutableMapOf(
            "api_key" to apiKey,
            "language" to appSettings.appLanguage.tmdbLanguageCode,
            "sort_by" to "popularity.desc",
            "page" to page.toString(),
            "vote_count.gte" to "100",
        )
        if (genreIDs.isNotEmpty()) {
            // "|" = OR : on élargit la requête, le tri par pertinence est fait côté app
            params["with_genres"] = genreIDs.joinToString("|")
        }
        if (genresToExclude.isNotEmpty()) {
            params["without_genres"] = genresToExclude.joinToString("|")
        }
        maxRuntime?.let { params["with_runtime.lte"] = it.toString() }
        minVoteAverage?.let { params["vote_average.gte"] = it.toString() }
        minReleaseYear?.let { params["primary_release_date.gte"] = "$it-01-01" }
        maxReleaseYear?.let { params["primary_release_date.lte"] = "$it-12-31" }
        if (providerIDs.isNotEmpty()) {
            params["with_watch_providers"] = providerIDs.joinToString("|")
            params["watch_region"] = "FR"
        }
        if (maxCertification != FrenchCertification.ALL) {
            params["certification_country"] = "FR"
            params["certification.lte"] = maxCertification.maxAllowed
        }

        return try {
            api.discoverMovies(params).results
        } catch (e: Exception) {
            throw TMDBException.RequestFailed
        }
    }

    suspend fun watchProviders(movieId: Int): CountryProviders? {
        val params = mapOf("api_key" to apiKey)
        return try {
            api.watchProviders(movieId, params).results["FR"]
        } catch (e: Exception) {
            throw TMDBException.RequestFailed
        }
    }

    suspend fun searchMovies(query: String): List<Movie> {
        if (query.isBlank()) return emptyList()

        val params = mapOf(
            "api_key" to apiKey,
            "language" to appSettings.appLanguage.tmdbLanguageCode,
            "query" to query,
            "include_adult" to "false",
        )
        return try {
            api.searchMovies(params).results
        } catch (e: Exception) {
            throw TMDBException.RequestFailed
        }
    }
}
