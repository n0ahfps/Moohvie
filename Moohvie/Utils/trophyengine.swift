import Foundation

struct TrophyEngine {
    private static let totalThresholds = [1, 10, 25, 50, 100, 200, 350]
    private static let genreThresholds = [1, 5, 10, 20, 35, 50, 75]
    private static let highRatedThresholds = [1, 3, 8, 15, 25, 40, 60]
    private static let decadeThresholds = [1, 2, 3, 4, 5, 6, 7]
    private static let highRatedMinVote = 7.5

    static func trophies(for movies: [WatchedMovie]) -> [TrophyProgress] {
        var result: [TrophyProgress] = []

        result.append(makeTrophy(
            id: "total",
            title: "Cinéphile",
            description: "Regarde des films, tout simplement ! Chaque film ajouté au Cinétable compte.",
            icon: "film.fill",
            matching: movies,
            thresholds: totalThresholds
        ))

        let highRated = movies.filter { $0.voteAverage >= highRatedMinVote }
        result.append(makeTrophy(
            id: "high-rated",
            title: "Critique exigeant",
            description: "Regarde des films très bien notés sur TMDB (note moyenne ≥ \(highRatedMinVote.formatted()).",
            icon: "star.fill",
            matching: highRated,
            thresholds: highRatedThresholds
        ))

        result.append(makeDecadeTrophy(movies: movies))

        for (genreID, genreName) in GenreLookup.names.sorted(by: { $0.value < $1.value }) {
            let matching = movies.filter { $0.genreIDs.contains(genreID) }
            result.append(makeTrophy(
                id: "genre-\(genreID)",
                title: genreName,
                description: "Regarde des films du genre \(genreName).",
                icon: icon(forGenre: genreID),
                matching: matching,
                thresholds: genreThresholds
            ))
        }

        return result
    }

    private static func makeTrophy(id: String, title: String, description: String, icon: String, matching: [WatchedMovie], thresholds: [Int]) -> TrophyProgress {
        let sorted = matching.sorted { $0.watchedDate < $1.watchedDate }
        let count = sorted.count

        var tier: TrophyTier?
        let milestones: [TrophyMilestone] = thresholds.enumerated().map { index, threshold in
            let unlockedDate = count >= threshold ? sorted[threshold - 1].watchedDate : nil
            if unlockedDate != nil {
                tier = TrophyTier(rawValue: index)
            }
            return TrophyMilestone(tier: TrophyTier(rawValue: index)!, threshold: threshold, unlockedDate: unlockedDate)
        }

        let nextThreshold = thresholds.first { $0 > count }
        return TrophyProgress(id: id, title: title, description: description, icon: icon, count: count, tier: tier, nextThreshold: nextThreshold, milestones: milestones)
    }

    private static func makeDecadeTrophy(movies: [WatchedMovie]) -> TrophyProgress {
        let sorted = movies.sorted { $0.watchedDate < $1.watchedDate }
        var seenDecades = Set<Int>()
        var unlockedDateByCount: [Int: Date] = [:]

        for movie in sorted {
            guard let d = decade(from: movie), !seenDecades.contains(d) else { continue }
            seenDecades.insert(d)
            unlockedDateByCount[seenDecades.count] = movie.watchedDate
        }

        let count = seenDecades.count
        var tier: TrophyTier?
        let milestones: [TrophyMilestone] = decadeThresholds.enumerated().map { index, threshold in
            let unlockedDate = unlockedDateByCount[threshold]
            if unlockedDate != nil {
                tier = TrophyTier(rawValue: index)
            }
            return TrophyMilestone(tier: TrophyTier(rawValue: index)!, threshold: threshold, unlockedDate: unlockedDate)
        }

        let nextThreshold = decadeThresholds.first { $0 > count }
        return TrophyProgress(
            id: "decades",
            title: "Explorateur des époques",
            description: "Regarde des films sortis dans des décennies différentes, du plus ancien au plus récent.",
            icon: "clock.fill",
            count: count,
            tier: tier,
            nextThreshold: nextThreshold,
            milestones: milestones
        )
    }

    private static func decade(from movie: WatchedMovie) -> Int? {
        guard let releaseDate = movie.releaseDate, let year = Int(releaseDate.prefix(4)) else { return nil }
        return (year / 10) * 10
    }

    private static func icon(forGenre genreID: Int) -> String {
        switch genreID {
        case 28: return "bolt.fill"
        case 12: return "map.fill"
        case 16: return "paintpalette.fill"
        case 35: return "face.smiling.fill"
        case 80: return "magnifyingglass"
        case 99: return "doc.text.fill"
        case 18: return "theatermasks.fill"
        case 10751: return "house.fill"
        case 14: return "wand.and.stars"
        case 27: return "eye.fill"
        case 10402: return "music.note"
        case 9648: return "questionmark.circle.fill"
        case 10749: return "heart.fill"
        case 878: return "atom"
        case 53: return "bolt.heart.fill"
        case 10752: return "shield.fill"
        case 37: return "sun.dust.fill"
        default: return "film"
        }
    }
}
