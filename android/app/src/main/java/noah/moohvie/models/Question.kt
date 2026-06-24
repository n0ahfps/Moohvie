package noah.moohvie.models

import java.util.UUID

data class AnswerOption(
    val id: UUID = UUID.randomUUID(),
    val label: String,
    val genresToAdd: List<Int> = emptyList(),
    val genresToExclude: List<Int> = emptyList(),
    val maxRuntime: Int? = null,
    val minVoteAverage: Double? = null,
    val minReleaseYear: Int? = null,
    val maxReleaseYear: Int? = null,
)

enum class QuestionPriority(val rank: Int) {
    ESSENTIAL(1), // dans le mode court (5 questions)
    IMPORTANT(2), // dans le mode moyen (12 questions)
    DETAILED(3),  // uniquement dans le mode long (20 questions)
}

data class Question(
    val id: UUID = UUID.randomUUID(),
    val text: String,
    val options: List<AnswerOption>,
    val priority: QuestionPriority,
)
