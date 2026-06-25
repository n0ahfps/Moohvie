import Foundation
import Combine

enum QuizLength: Int {
    case short = 5
    case medium = 12
    case long = 20
}

@MainActor
class QuizViewModel: ObservableObject {
    @Published var currentQuestionIndex = 0
    @Published var selectedGenres: Set<Int> = []
    @Published var excludedGenres: Set<Int> = []
    @Published var isFinished = false

    @Published var maxRuntime: Int? = nil
    @Published var minVoteAverage: Double? = nil
    @Published var minReleaseYear: Int? = nil
    @Published var maxReleaseYear: Int? = nil

    var questions: [Question] = []

    func start(length: QuizLength) {
        switch length {
        case .short:
            questions = QuestionBank.questions.filter { $0.priority == .essential }
        case .medium:
            questions = QuestionBank.questions.filter { $0.priority == .essential || $0.priority == .important }
        case .long:
            questions = QuestionBank.questions
        }
        reset()
    }

    var currentQuestion: Question {
        questions[currentQuestionIndex]
    }

    var progress: Double {
        guard !questions.isEmpty else { return 0 }
        return Double(currentQuestionIndex) / Double(questions.count)
    }

    func selectAnswer(_ option: AnswerOption) {
        selectedGenres.formUnion(option.genresToAdd)
        excludedGenres.formUnion(option.genresToExclude)

        if let runtime = option.maxRuntime {
            maxRuntime = min(maxRuntime ?? runtime, runtime)
        }
        if let vote = option.minVoteAverage {
            minVoteAverage = max(minVoteAverage ?? vote, vote)
        }
        if let minYear = option.minReleaseYear {
            minReleaseYear = max(minReleaseYear ?? minYear, minYear)
        }
        if let maxYear = option.maxReleaseYear {
            maxReleaseYear = min(maxReleaseYear ?? maxYear, maxYear)
        }

        if currentQuestionIndex < questions.count - 1 {
            currentQuestionIndex += 1
        } else {
            isFinished = true
        }
    }

    var finalGenres: [Int] {
        Array(selectedGenres.subtracting(excludedGenres))
    }

    var finalExcludedGenres: [Int] {
        Array(excludedGenres.subtracting(selectedGenres))
    }

    func applyMood(_ preset: MoodPreset) {
        selectedGenres = Set(preset.genreIDs)
        excludedGenres = []
    }

    func reset() {
        currentQuestionIndex = 0
        selectedGenres = []
        excludedGenres = []
        maxRuntime = nil
        minVoteAverage = nil
        minReleaseYear = nil
        maxReleaseYear = nil
        isFinished = false
    }
}
