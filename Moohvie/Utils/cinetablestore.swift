import Foundation
import Combine

struct CineTableStats {
    let totalMovies: Int
    let favoriteGenreName: String?
    let averagePersonalRating: Double?
    let topRatedMovie: WatchedMovie?
    let moviesPerMonth: Double?
}

@MainActor
class CineTableStore: ObservableObject {
    static let shared = CineTableStore()

    @Published var watchedMovies: [WatchedMovie] {
        didSet {
            save()
        }
    }

    private let storageKey = "cineTableWatchedMovies"

    private init() {
        if let data = UserDefaults.standard.data(forKey: storageKey),
           let decoded = try? JSONDecoder().decode([WatchedMovie].self, from: data) {
            self.watchedMovies = decoded
        } else {
            self.watchedMovies = []
        }
    }

    private func save() {
        if let encoded = try? JSONEncoder().encode(watchedMovies) {
            UserDefaults.standard.set(encoded, forKey: storageKey)
        }
    }

    func add(_ movie: Movie) {
        guard !isWatched(movie.id) else { return }
        let points = movie.rarityPoints
        let entry = WatchedMovie(
            id: movie.id,
            title: movie.title,
            posterPath: movie.posterPath,
            voteAverage: movie.voteAverage,
            watchedDate: Date(),
            genreIDs: movie.genreIDs,
            pointsEarned: points,
            releaseDate: movie.releaseDate
        )
        watchedMovies.insert(entry, at: 0)
        MoohPointsStore.shared.award(points)
    }

    func isWatched(_ movieID: Int) -> Bool {
        watchedMovies.contains { $0.id == movieID }
    }

    var watchedIDs: Set<Int> {
        Set(watchedMovies.map { $0.id })
    }

    func remove(_ movieID: Int) {
        if let movie = watchedMovies.first(where: { $0.id == movieID }) {
            MoohPointsStore.shared.refund(movie.pointsEarned)
        }
        watchedMovies.removeAll { $0.id == movieID }
    }

    func setRating(_ movieID: Int, rating: Int) {
        guard let index = watchedMovies.firstIndex(where: { $0.id == movieID }) else { return }
        watchedMovies[index].personalRating = rating
    }

    var stats: CineTableStats {
        let movies = watchedMovies

        var genreCounts: [Int: Int] = [:]
        for m in movies { for g in m.genreIDs { genreCounts[g, default: 0] += 1 } }
        let favoriteGenreName = genreCounts.max(by: { $0.value < $1.value }).map { GenreLookup.name(for: $0.key) }

        let rated = movies.filter { $0.personalRating > 0 }
        let avgRating: Double? = rated.isEmpty ? nil : Double(rated.reduce(0) { $0 + $1.personalRating }) / Double(rated.count)
        let topMovie = rated.max(by: { $0.personalRating < $1.personalRating })

        var moviesPerMonth: Double? = nil
        if movies.count >= 2 {
            let dates = movies.map { $0.watchedDate }
            if let earliest = dates.min(), let latest = dates.max() {
                let months = max(1, Calendar.current.dateComponents([.month], from: earliest, to: latest).month ?? 1)
                moviesPerMonth = Double(movies.count) / Double(months)
            }
        }

        return CineTableStats(
            totalMovies: movies.count,
            favoriteGenreName: favoriteGenreName,
            averagePersonalRating: avgRating,
            topRatedMovie: topMovie,
            moviesPerMonth: moviesPerMonth
        )
    }

    // Returns a normalized weight [0, 1] per genre from highly-rated movies.
    // Requires at least 3 qualifying films to avoid biasing on too little data.
    func favoriteGenreWeights(minRating: Int = 4) -> [Int: Double] {
        let qualified = watchedMovies.filter { $0.personalRating >= minRating }
        guard qualified.count >= 3 else { return [:] }

        var rawScores: [Int: Double] = [:]
        for movie in qualified {
            let weight = Double(movie.personalRating) / 5.0
            for genreID in movie.genreIDs {
                rawScores[genreID, default: 0] += weight
            }
        }

        guard let maxScore = rawScores.values.max(), maxScore > 0 else { return [:] }
        return rawScores.mapValues { $0 / maxScore }
    }
}
