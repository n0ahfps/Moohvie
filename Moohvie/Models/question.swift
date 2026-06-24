import Foundation

struct AnswerOption: Identifiable {
    let id = UUID()
    let label: String
    let genresToAdd: [Int]
    let genresToExclude: [Int]

    var maxRuntime: Int? = nil
    var minVoteAverage: Double? = nil
    var minReleaseYear: Int? = nil
    var maxReleaseYear: Int? = nil

    init(
        label: String,
        genresToAdd: [Int] = [],
        genresToExclude: [Int] = [],
        maxRuntime: Int? = nil,
        minVoteAverage: Double? = nil,
        minReleaseYear: Int? = nil,
        maxReleaseYear: Int? = nil
    ) {
        self.label = label
        self.genresToAdd = genresToAdd
        self.genresToExclude = genresToExclude
        self.maxRuntime = maxRuntime
        self.minVoteAverage = minVoteAverage
        self.minReleaseYear = minReleaseYear
        self.maxReleaseYear = maxReleaseYear
    }
}

enum QuestionPriority: Int {
    case essential = 1   // dans le mode court (5 questions)
    case important = 2   // dans le mode moyen (12 questions)
    case detailed = 3    // uniquement dans le mode long (20 questions)
}

struct Question: Identifiable {
    let id = UUID()
    let text: String
    let options: [AnswerOption]
    let priority: QuestionPriority
}
