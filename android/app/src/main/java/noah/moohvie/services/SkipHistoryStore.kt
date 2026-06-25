package noah.moohvie.services

import android.content.Context
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import noah.moohvie.models.Movie

class SkipHistoryStore private constructor(context: Context) {
    private val prefs = context.getSharedPreferences("moohvie_settings", Context.MODE_PRIVATE)
    private val storageKey = "genreSkipHistory"
    private val expirationMs = 90L * 24 * 3600 * 1000
    private val json = Json { ignoreUnknownKeys = true }

    private var history: MutableMap<String, MutableList<Long>> = load()

    private fun load(): MutableMap<String, MutableList<Long>> {
        val raw = prefs.getString(storageKey, null) ?: return mutableMapOf()
        return try {
            json.decodeFromString<Map<String, List<Long>>>(raw)
                .mapValues { it.value.toMutableList() }
                .toMutableMap()
        } catch (e: Exception) {
            mutableMapOf()
        }
    }

    private fun save() {
        prefs.edit().putString(storageKey, json.encodeToString(history as Map<String, List<Long>>)).apply()
    }

    fun record(movie: Movie) {
        val now = System.currentTimeMillis()
        for (genreID in movie.genreIDs) {
            history.getOrPut(genreID.toString()) { mutableListOf() }.add(now)
        }
        pruneExpired()
        save()
    }

    // Returns a weight in [0, 1] per genre ID based on recent skip frequency.
    // 1 recent skip ≈ 0.33, 3+ recent skips → 1.0 (capped).
    val skippedGenreWeights: Map<Int, Double>
        get() {
            val cutoff = System.currentTimeMillis() - expirationMs
            return history.entries.mapNotNull { (key, timestamps) ->
                val id = key.toIntOrNull() ?: return@mapNotNull null
                val recent = timestamps.count { it > cutoff }
                if (recent > 0) id to minOf(recent / 3.0, 1.0) else null
            }.toMap()
        }

    private fun pruneExpired() {
        val cutoff = System.currentTimeMillis() - expirationMs
        val iter = history.entries.iterator()
        while (iter.hasNext()) {
            val entry = iter.next()
            entry.value.removeAll { it <= cutoff }
            if (entry.value.isEmpty()) iter.remove()
        }
    }

    companion object {
        @Volatile private var instance: SkipHistoryStore? = null

        fun getInstance(context: Context): SkipHistoryStore =
            instance ?: synchronized(this) {
                instance ?: SkipHistoryStore(context.applicationContext).also { instance = it }
            }
    }
}
