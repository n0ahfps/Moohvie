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

    private let service = TMDBService()

    func loadMovies(from quiz: QuizViewModel?) async {
        isLoading = true
        errorMessage = nil

        let settings = AppSettings.shared
        let providerIDs = Array(settings.selectedProviderIDs)
        let certification = settings.maxCertification
        let watchedIDs = CineTableStore.shared.watchedIDs

        let genreIDs = quiz?.finalGenres ?? []
        let maxRuntime = quiz?.maxRuntime
        let minVoteAverage = quiz?.minVoteAverage
        let minReleaseYear = quiz?.minReleaseYear
        let maxReleaseYear = quiz?.maxReleaseYear

        // Page aléatoire parmi les 15 premières (au-delà, TMDB renvoie des films
        // trop obscurs ou peu pertinents pour des critères précis)
        let randomPage = Int.random(in: 1...15)

        do {
            var results = try await service.discoverMovies(
                genreIDs: genreIDs,
                maxRuntime: maxRuntime,
                minVoteAverage: minVoteAverage,
                minReleaseYear: minReleaseYear,
                maxReleaseYear: maxReleaseYear,
                providerIDs: providerIDs,
                maxCertification: certification,
                page: randomPage
            )

            if results.isEmpty {
                results = try await service.discoverMovies(
                    genreIDs: genreIDs,
                    providerIDs: providerIDs,
                    maxCertification: certification,
                    page: randomPage
                )
            }

            if results.isEmpty, let firstGenre = genreIDs.first {
                results = try await service.discoverMovies(
                    genreIDs: [firstGenre],
                    providerIDs: providerIDs,
                    maxCertification: certification,
                    page: randomPage
                )
            }

            if results.isEmpty {
                results = try await service.discoverMovies(
                    genreIDs: [],
                    providerIDs: providerIDs,
                    maxCertification: certification,
                    page: Int.random(in: 1...10)
                )
            }

            if results.isEmpty {
                results = try await service.discoverMovies(genreIDs: [])
            }

            if !settings.allowRewatching {
                let filtered = results.filter { !watchedIDs.contains($0.id) }
                results = filtered.isEmpty ? results : filtered
            }

            movies = results.shuffled()
        } catch {
            errorMessage = "Impossible de charger les films. Vérifie ta connexion."
        }

        isLoading = false
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
