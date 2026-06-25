import Foundation

@MainActor
class SkipHistoryStore {
    static let shared = SkipHistoryStore()

    private let storageKey = "genreSkipHistory"
    private let expirationDays: Double = 90
    private var history: [String: [Double]]

    private init() {
        history = (UserDefaults.standard.dictionary(forKey: "genreSkipHistory") as? [String: [Double]]) ?? [:]
    }

    func record(_ movie: Movie) {
        let now = Date().timeIntervalSince1970
        for genreID in movie.genreIDs {
            history[String(genreID), default: []].append(now)
        }
        pruneExpired()
        save()
    }

    // Returns a weight in [0, 1] per genre ID based on recent skip frequency.
    // 1 recent skip ≈ 0.33, 3+ recent skips → 1.0 (capped).
    var skippedGenreWeights: [Int: Double] {
        let cutoff = Date().timeIntervalSince1970 - expirationDays * 86400
        return history.reduce(into: [:]) { result, pair in
            guard let id = Int(pair.key) else { return }
            let recent = pair.value.filter { $0 > cutoff }.count
            if recent > 0 {
                result[id] = min(Double(recent) / 3.0, 1.0)
            }
        }
    }

    private func pruneExpired() {
        let cutoff = Date().timeIntervalSince1970 - expirationDays * 86400
        history = history.compactMapValues { timestamps in
            let kept = timestamps.filter { $0 > cutoff }
            return kept.isEmpty ? nil : kept
        }
    }

    private func save() {
        UserDefaults.standard.set(history, forKey: storageKey)
    }
}
