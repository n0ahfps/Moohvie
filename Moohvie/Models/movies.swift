import Foundation

struct Movie: Identifiable, Decodable {
    let id: Int
    let title: String
    let overview: String
    let posterPath: String?
    let genreIDs: [Int]
    let releaseDate: String?
    let voteAverage: Double
    let popularity: Double

    enum CodingKeys: String, CodingKey {
        case id
        case title
        case overview
        case posterPath = "poster_path"
        case genreIDs = "genre_ids"
        case releaseDate = "release_date"
        case voteAverage = "vote_average"
        case popularity
    }

    var posterURL: URL? {
        guard let posterPath else { return nil }
        return URL(string: "https://image.tmdb.org/t/p/w500\(posterPath)")
    }

    /// Points gagnés en ajoutant ce film au Cinétable : moins le film est populaire
    /// sur TMDB, plus il est considéré "rare" et rapporte de points.
    var rarityPoints: Int {
        switch popularity {
        case ..<5: return 50
        case ..<20: return 35
        case ..<50: return 20
        case ..<100: return 15
        default: return 10
        }
    }
}
