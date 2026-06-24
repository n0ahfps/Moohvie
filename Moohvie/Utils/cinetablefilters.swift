import Foundation

enum CineTableSortOption: String, CaseIterable {
    case dateDesc = "Plus récent d'abord"
    case dateAsc = "Plus ancien d'abord"
    case personalRatingDesc = "Ma note (haute → basse)"
    case tmdbRatingDesc = "Note TMDB (haute → basse)"
}

enum CineTableDisplayMode: String, CaseIterable {
    case grid = "Grille"
    case list = "Liste"
}

struct CineTableFilterState {
    var sortOption: CineTableSortOption = .dateDesc
    var selectedGenreID: Int? = nil
    var minPersonalRating: Int = 0
    var minTMDBRating: Double = 0

    func apply(to movies: [WatchedMovie]) -> [WatchedMovie] {
        var filtered = movies

        if let genreID = selectedGenreID {
            filtered = filtered.filter { $0.genreIDs.contains(genreID) }
        }
        if minPersonalRating > 0 {
            filtered = filtered.filter { $0.personalRating >= minPersonalRating }
        }
        if minTMDBRating > 0 {
            filtered = filtered.filter { $0.voteAverage >= minTMDBRating }
        }

        switch sortOption {
        case .dateDesc:
            filtered.sort { $0.watchedDate > $1.watchedDate }
        case .dateAsc:
            filtered.sort { $0.watchedDate < $1.watchedDate }
        case .personalRatingDesc:
            filtered.sort { $0.personalRating > $1.personalRating }
        case .tmdbRatingDesc:
            filtered.sort { $0.voteAverage > $1.voteAverage }
        }

        return filtered
    }

    var isActive: Bool {
        selectedGenreID != nil || minPersonalRating > 0 || minTMDBRating > 0
    }
}
