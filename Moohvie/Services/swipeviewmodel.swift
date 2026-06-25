import Foundation
import Combine

@MainActor
class SwipeViewModel: ObservableObject {
    @Published var movies: [Movie] = []
    @Published var currentIndex = 0
    @Published var isLoading = false
    @Published var errorMessage: String?

    @Published var matchedMovie: Movie?
    @Published var matchedMovieProviders: CountryProviders?
    @Published var addedToCineTable = false
    @Published var criteriaRelaxed = false

    private let service = TMDBService()

    func loadMovies(from quiz: QuizViewModel?) async {
        isLoading = true
        errorMessage = nil
        criteriaRelaxed = false

        let settings = AppSettings.shared
        let providerIDs = Array(settings.selectedProviderIDs)
        let certification = settings.maxCertification
        let watchedIDs = CineTableStore.shared.watchedIDs

        let genreIDs = quiz?.finalGenres ?? []
        let excludedGenreIDs = quiz?.finalExcludedGenres ?? []
        let maxRuntime = quiz?.maxRuntime
        let minVoteAverage = quiz?.minVoteAverage
        let minReleaseYear = quiz?.minReleaseYear
        let maxReleaseYear = quiz?.maxReleaseYear

        // Pages 1-5 pour les requêtes précises : au-delà, TMDB trie par popularité
        // décroissante et les résultats perdent en pertinence par rapport aux genres.
        let strictPage = Int.random(in: 1...5)

        do {
            var results = try await service.discoverMovies(
                genreIDs: genreIDs,
                genresToExclude: excludedGenreIDs,
                maxRuntime: maxRuntime,
                minVoteAverage: minVoteAverage,
                minReleaseYear: minReleaseYear,
                maxReleaseYear: maxReleaseYear,
                providerIDs: providerIDs,
                maxCertification: certification,
                page: strictPage
            )

            if results.isEmpty {
                criteriaRelaxed = true
                results = try await service.discoverMovies(
                    genreIDs: genreIDs,
                    genresToExclude: excludedGenreIDs,
                    providerIDs: providerIDs,
                    maxCertification: certification,
                    page: strictPage
                )
            }

            if results.isEmpty, let firstGenre = genreIDs.first {
                results = try await service.discoverMovies(
                    genreIDs: [firstGenre],
                    genresToExclude: excludedGenreIDs,
                    providerIDs: providerIDs,
                    maxCertification: certification,
                    page: strictPage
                )
            }

            if results.isEmpty {
                results = try await service.discoverMovies(
                    genreIDs: [],
                    genresToExclude: excludedGenreIDs,
                    providerIDs: providerIDs,
                    maxCertification: certification,
                    page: Int.random(in: 1...8)
                )
            }

            if results.isEmpty {
                results = try await service.discoverMovies(genreIDs: [])
            }

            if !settings.allowRewatching {
                let filtered = results.filter { !watchedIDs.contains($0.id) }
                results = filtered.isEmpty ? results : filtered
            }

            movies = sortedByRelevance(results, wantedGenres: genreIDs)
        } catch {
            errorMessage = "Impossible de charger les films. Vérifie ta connexion."
        }

        isLoading = false
    }

    private func sortedByRelevance(_ movies: [Movie], wantedGenres: [Int]) -> [Movie] {
        let wanted = Set(wantedGenres)
        let skipWeights = SkipHistoryStore.shared.skippedGenreWeights
        guard !wanted.isEmpty || !skipWeights.isEmpty else { return movies.shuffled() }
        return movies.sorted { relevanceScore($0, wanted: wanted, skipWeights: skipWeights) > relevanceScore($1, wanted: wanted, skipWeights: skipWeights) }
    }

    private func relevanceScore(_ movie: Movie, wanted: Set<Int>, skipWeights: [Int: Double]) -> Double {
        let match = Double(movie.genreIDs.filter { wanted.contains($0) }.count)
        let penalty = movie.genreIDs.reduce(0.0) { $0 + (skipWeights[$1] ?? 0) }
        return match - penalty
    }

    var currentMovie: Movie? {
        guard currentIndex < movies.count else { return nil }
        return movies[currentIndex]
    }

    var isFinished: Bool { currentIndex >= movies.count }

    func selectThisMovie() {
        guard let movie = currentMovie else { return }
        applyMatch(movie)
    }

    func pickRandom() {
        let remaining = Array(movies[currentIndex...])
        guard let randomMovie = remaining.randomElement() else { return }
        applyMatch(randomMovie)
    }

    private func applyMatch(_ movie: Movie) {
        matchedMovie = movie
        Task {
            matchedMovieProviders = try? await service.watchProviders(movieID: movie.id)
        }
    }

    func skip() {
        if let movie = currentMovie {
            SkipHistoryStore.shared.record(movie)
        }
        currentIndex += 1
    }

    func confirmWatched() {
        guard let movie = matchedMovie else { return }
        CineTableStore.shared.add(movie)
        addedToCineTable = true
    }

    func reset() {
        currentIndex = 0
        matchedMovie = nil
        matchedMovieProviders = nil
        addedToCineTable = false
    }
}
