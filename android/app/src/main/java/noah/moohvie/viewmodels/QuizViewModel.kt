package noah.moohvie.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import noah.moohvie.models.AnswerOption
import noah.moohvie.models.Question
import noah.moohvie.models.QuestionBank
import noah.moohvie.models.QuestionPriority
import noah.moohvie.models.QuizLength

class QuizViewModel : ViewModel() {
    var currentQuestionIndex: Int by mutableStateOf(0)
        private set
    var isFinished: Boolean by mutableStateOf(false)
        private set

    private var selectedGenres: Set<Int> = emptySet()
    private var excludedGenres: Set<Int> = emptySet()

    var maxRuntime: Int? by mutableStateOf(null)
        private set
    var minVoteAverage: Double? by mutableStateOf(null)
        private set
    var minReleaseYear: Int? by mutableStateOf(null)
        private set
    var maxReleaseYear: Int? by mutableStateOf(null)
        private set

    var questions: List<Question> by mutableStateOf(emptyList())
        private set

    fun start(length: QuizLength) {
        questions = when (length) {
            QuizLength.SHORT -> QuestionBank.questions.filter { it.priority == QuestionPriority.ESSENTIAL }
            QuizLength.MEDIUM -> QuestionBank.questions.filter {
                it.priority == QuestionPriority.ESSENTIAL || it.priority == QuestionPriority.IMPORTANT
            }
            QuizLength.LONG -> QuestionBank.questions
        }
        reset()
    }

    val currentQuestion: Question
        get() = questions[currentQuestionIndex]

    val progress: Float
        get() = if (questions.isEmpty()) 0f else currentQuestionIndex.toFloat() / questions.size

    fun selectAnswer(option: AnswerOption) {
        selectedGenres = selectedGenres + option.genresToAdd
        excludedGenres = excludedGenres + option.genresToExclude

        option.maxRuntime?.let { maxRuntime = minOf(maxRuntime ?: it, it) }
        option.minVoteAverage?.let { minVoteAverage = maxOf(minVoteAverage ?: it, it) }
        option.minReleaseYear?.let { minReleaseYear = maxOf(minReleaseYear ?: it, it) }
        option.maxReleaseYear?.let { maxReleaseYear = minOf(maxReleaseYear ?: it, it) }

        if (currentQuestionIndex < questions.size - 1) {
            currentQuestionIndex += 1
        } else {
            isFinished = true
        }
    }

    val finalGenres: List<Int>
        get() = (selectedGenres - excludedGenres).toList()

    fun reset() {
        currentQuestionIndex = 0
        selectedGenres = emptySet()
        excludedGenres = emptySet()
        maxRuntime = null
        minVoteAverage = null
        minReleaseYear = null
        maxReleaseYear = null
        isFinished = false
    }
}
