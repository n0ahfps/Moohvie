import Foundation

struct WatchedMovie: Identifiable, Codable {
    let id: Int
    let title: String
    let posterPath: String?
    let voteAverage: Double
    let watchedDate: Date
    var personalRating: Int = 0
    var genreIDs: [Int] = []
    var pointsEarned: Int = 0
    var releaseDate: String? = nil

    var posterURL: URL? {
        guard let posterPath else { return nil }
        return URL(string: "https://image.tmdb.org/t/p/w500\(posterPath)")
    }

    var primaryGenreName: String {
        guard let firstID = genreIDs.first else { return "Non classé" }
        return GenreLookup.name(for: firstID)
    }
}
