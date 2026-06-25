import Foundation

struct MoodPreset: Identifiable {
    let id: String
    let emoji: String
    let label: String
    let genreIDs: [Int]

    static let all: [MoodPreset] = [
        MoodPreset(id: "comedy",    emoji: "😂", label: "Comédie",    genreIDs: [35]),
        MoodPreset(id: "thrills",   emoji: "😱", label: "Frissons",   genreIDs: [27, 53]),
        MoodPreset(id: "emotion",   emoji: "🥹", label: "Émotion",    genreIDs: [18]),
        MoodPreset(id: "action",    emoji: "🚀", label: "Action",     genreIDs: [28, 12]),
        MoodPreset(id: "romance",   emoji: "💕", label: "Romance",    genreIDs: [10749]),
        MoodPreset(id: "scifi",     emoji: "🌌", label: "Imaginaire", genreIDs: [878, 14]),
    ]
}
