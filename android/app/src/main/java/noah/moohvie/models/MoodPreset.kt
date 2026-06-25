package noah.moohvie.models

data class MoodPreset(
    val id: String,
    val emoji: String,
    val label: String,
    val genreIDs: List<Int>,
) {
    companion object {
        val all: List<MoodPreset> = listOf(
            MoodPreset("comedy",  "😂", "Comédie",    listOf(35)),
            MoodPreset("thrills", "😱", "Frissons",   listOf(27, 53)),
            MoodPreset("emotion", "🥹", "Émotion",    listOf(18)),
            MoodPreset("action",  "🚀", "Action",     listOf(28, 12)),
            MoodPreset("romance", "💕", "Romance",    listOf(10749)),
            MoodPreset("scifi",   "🌌", "Imaginaire", listOf(878, 14)),
        )

        fun find(id: String): MoodPreset? = all.firstOrNull { it.id == id }
    }
}
