package noah.moohvie.models

enum class CineTableSortOption(val label: String) {
    DATE_DESC("Plus récent d'abord"),
    DATE_ASC("Plus ancien d'abord"),
    PERSONAL_RATING_DESC("Ma note (haute → basse)"),
    TMDB_RATING_DESC("Note TMDB (haute → basse)"),
}

enum class CineTableDisplayMode {
    GRID,
    LIST,
}

data class CineTableFilterState(
    val sortOption: CineTableSortOption = CineTableSortOption.DATE_DESC,
    val selectedGenreID: Int? = null,
    val minPersonalRating: Int = 0,
    val minTMDBRating: Double = 0.0,
) {
    fun apply(to: List<WatchedMovie>): List<WatchedMovie> {
        val movies = to
        var filtered = movies

        selectedGenreID?.let { genreID ->
            filtered = filtered.filter { it.genreIDs.contains(genreID) }
        }
        if (minPersonalRating > 0) {
            filtered = filtered.filter { it.personalRating >= minPersonalRating }
        }
        if (minTMDBRating > 0) {
            filtered = filtered.filter { it.voteAverage >= minTMDBRating }
        }

        filtered = when (sortOption) {
            CineTableSortOption.DATE_DESC -> filtered.sortedByDescending { it.watchedDate }
            CineTableSortOption.DATE_ASC -> filtered.sortedBy { it.watchedDate }
            CineTableSortOption.PERSONAL_RATING_DESC -> filtered.sortedByDescending { it.personalRating }
            CineTableSortOption.TMDB_RATING_DESC -> filtered.sortedByDescending { it.voteAverage }
        }

        return filtered
    }

    val isActive: Boolean
        get() = selectedGenreID != null || minPersonalRating > 0 || minTMDBRating > 0
}
