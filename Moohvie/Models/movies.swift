import Foundation

struct Movie: Identifiable, Decodable {
    let id: Int
    let title: String
    let overview: String
    let posterPath: String?
    let genreIDs: [Int]
    let releaseDate: String?
    let voteAverage: Double

    enum CodingKeys: String, CodingKey {
        case id
        case title
        case overview
        case posterPath = "poster_path"
        case genreIDs = "genre_ids"
        case releaseDate = "release_date"
        case voteAverage = "vote_average"
    }

    var posterURL: URL? {
        guard let posterPath else { return nil }
        return URL(string: "https://image.tmdb.org/t/p/w500\(posterPath)")
    }
}
