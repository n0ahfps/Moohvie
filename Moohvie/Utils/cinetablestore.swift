import Foundation
import Combine

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
            pointsEarned: points
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
}
