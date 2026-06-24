package noah.moohvie.services

import noah.moohvie.models.GenreLookup
import noah.moohvie.models.TrophyMilestone
import noah.moohvie.models.TrophyProgress
import noah.moohvie.models.TrophyTier
import noah.moohvie.models.WatchedMovie

object TrophyEngine {
    private val totalThresholds = listOf(1, 10, 25, 50, 100, 200, 350)
    private val genreThresholds = listOf(1, 5, 10, 20, 35, 50, 75)
    private val highRatedThresholds = listOf(1, 3, 8, 15, 25, 40, 60)
    private val decadeThresholds = listOf(1, 2, 3, 4, 5, 6, 7)
    private const val highRatedMinVote = 7.5

    fun trophies(movies: List<WatchedMovie>): List<TrophyProgress> {
        val result = mutableListOf<TrophyProgress>()

        result += makeTrophy(
            id = "total",
            title = "Cinéphile",
            description = "Regarde des films, tout simplement ! Chaque film ajouté au Cinétable compte.",
            icon = "film.fill",
            matching = movies,
            thresholds = totalThresholds,
        )

        val highRated = movies.filter { it.voteAverage >= highRatedMinVote }
        result += makeTrophy(
            id = "high-rated",
            title = "Critique exigeant",
            description = "Regarde des films très bien notés sur TMDB (note moyenne ≥ $highRatedMinVote).",
            icon = "star.fill",
            matching = highRated,
            thresholds = highRatedThresholds,
        )

        result += makeDecadeTrophy(movies)

        GenreLookup.names.entries.sortedBy { it.value }.forEach { (genreID, genreName) ->
            val matching = movies.filter { it.genreIDs.contains(genreID) }
            result += makeTrophy(
                id = "genre-$genreID",
                title = genreName,
                description = "Regarde des films du genre $genreName.",
                icon = iconForGenre(genreID),
                matching = matching,
                thresholds = genreThresholds,
            )
        }

        return result
    }

    private fun makeTrophy(
        id: String,
        title: String,
        description: String,
        icon: String,
        matching: List<WatchedMovie>,
        thresholds: List<Int>,
    ): TrophyProgress {
        val sorted = matching.sortedBy { it.watchedDate }
        val count = sorted.size

        var tier: TrophyTier? = null
        val milestones = thresholds.mapIndexed { index, threshold ->
            val unlockedDate = if (count >= threshold) sorted[threshold - 1].watchedDate else null
            if (unlockedDate != null) tier = TrophyTier.entries[index]
            TrophyMilestone(tier = TrophyTier.entries[index], threshold = threshold, unlockedDate = unlockedDate)
        }

        val nextThreshold = thresholds.firstOrNull { it > count }
        return TrophyProgress(id, title, description, icon, count, tier, nextThreshold, milestones)
    }

    private fun makeDecadeTrophy(movies: List<WatchedMovie>): TrophyProgress {
        val sorted = movies.sortedBy { it.watchedDate }
        val seenDecades = mutableSetOf<Int>()
        val unlockedDateByCount = mutableMapOf<Int, Long>()

        for (movie in sorted) {
            val decade = decadeOf(movie) ?: continue
            if (seenDecades.contains(decade)) continue
            seenDecades += decade
            unlockedDateByCount[seenDecades.size] = movie.watchedDate
        }

        val count = seenDecades.size
        var tier: TrophyTier? = null
        val milestones = decadeThresholds.mapIndexed { index, threshold ->
            val unlockedDate = unlockedDateByCount[threshold]
            if (unlockedDate != null) tier = TrophyTier.entries[index]
            TrophyMilestone(tier = TrophyTier.entries[index], threshold = threshold, unlockedDate = unlockedDate)
        }

        val nextThreshold = decadeThresholds.firstOrNull { it > count }
        return TrophyProgress(
            id = "decades",
            title = "Explorateur des époques",
            description = "Regarde des films sortis dans des décennies différentes, du plus ancien au plus récent.",
            icon = "clock.fill",
            count = count,
            tier = tier,
            nextThreshold = nextThreshold,
            milestones = milestones,
        )
    }

    private fun decadeOf(movie: WatchedMovie): Int? {
        val year = movie.releaseDate?.take(4)?.toIntOrNull() ?: return null
        return (year / 10) * 10
    }

    private fun iconForGenre(genreID: Int): String = when (genreID) {
        28 -> "bolt.fill"
        12 -> "map.fill"
        16 -> "paintpalette.fill"
        35 -> "face.smiling.fill"
        80 -> "magnifyingglass"
        99 -> "doc.text.fill"
        18 -> "theatermasks.fill"
        10751 -> "house.fill"
        14 -> "wand.and.stars"
        27 -> "eye.fill"
        10402 -> "music.note"
        9648 -> "questionmark.circle.fill"
        10749 -> "heart.fill"
        878 -> "atom"
        53 -> "bolt.heart.fill"
        10752 -> "shield.fill"
        37 -> "sun.dust.fill"
        else -> "film"
    }
}
