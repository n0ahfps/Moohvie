import Foundation

enum TMDBError: Error {
    case invalidURL
    case requestFailed
    case decodingFailed
}

struct TMDBResponse: Decodable {
    let results: [Movie]
}

class TMDBService {
    private let baseURL = "https://api.themoviedb.org/3"
    private let apiKey = Secrets.tmdbAPIKey

    func discoverMovies(
        genreIDs: [Int],
        genresToExclude: [Int] = [],
        maxRuntime: Int? = nil,
        minVoteAverage: Double? = nil,
        minReleaseYear: Int? = nil,
        maxReleaseYear: Int? = nil,
        providerIDs: [Int] = [],
        maxCertification: FrenchCertification = .all,
        page: Int = 1
    ) async throws -> [Movie] {
        var components = URLComponents(string: "\(baseURL)/discover/movie")

        var queryItems = [
            URLQueryItem(name: "api_key", value: apiKey),
            URLQueryItem(name: "language", value: await MainActor.run { AppSettings.shared.appLanguage.tmdbLanguageCode }),
            URLQueryItem(name: "sort_by", value: "popularity.desc"),
            URLQueryItem(name: "page", value: String(page)),
            URLQueryItem(name: "vote_count.gte", value: "100")
        ]

        if !genreIDs.isEmpty {
            // "|" = OR : on élargit la requête, le tri par pertinence est fait côté app
            queryItems.append(URLQueryItem(name: "with_genres", value: genreIDs.map(String.init).joined(separator: "|")))
        }
        if !genresToExclude.isEmpty {
            queryItems.append(URLQueryItem(name: "without_genres", value: genresToExclude.map(String.init).joined(separator: "|")))
        }
        if let maxRuntime {
            queryItems.append(URLQueryItem(name: "with_runtime.lte", value: String(maxRuntime)))
        }
        if let minVoteAverage {
            queryItems.append(URLQueryItem(name: "vote_average.gte", value: String(minVoteAverage)))
        }
        if let minReleaseYear {
            queryItems.append(URLQueryItem(name: "primary_release_date.gte", value: "\(minReleaseYear)-01-01"))
        }
        if let maxReleaseYear {
            queryItems.append(URLQueryItem(name: "primary_release_date.lte", value: "\(maxReleaseYear)-12-31"))
        }
        if !providerIDs.isEmpty {
            queryItems.append(URLQueryItem(name: "with_watch_providers", value: providerIDs.map(String.init).joined(separator: "|")))
            queryItems.append(URLQueryItem(name: "watch_region", value: "FR"))
        }
        if maxCertification != .all {
            queryItems.append(URLQueryItem(name: "certification_country", value: "FR"))
            queryItems.append(URLQueryItem(name: "certification.lte", value: maxCertification.maxAllowed))
        }

        components?.queryItems = queryItems

        guard let url = components?.url else {
            throw TMDBError.invalidURL
        }

        let (data, response) = try await URLSession.shared.data(from: url)

        guard let httpResponse = response as? HTTPURLResponse,
              httpResponse.statusCode == 200 else {
            throw TMDBError.requestFailed
        }

        do {
            let decoded = try JSONDecoder().decode(TMDBResponse.self, from: data)
            return decoded.results
        } catch {
            throw TMDBError.decodingFailed
        }
    }

    func watchProviders(movieID: Int) async throws -> CountryProviders? {
        var components = URLComponents(string: "\(baseURL)/movie/\(movieID)/watch/providers")
        components?.queryItems = [
            URLQueryItem(name: "api_key", value: apiKey)
        ]

        guard let url = components?.url else {
            throw TMDBError.invalidURL
        }

        let (data, response) = try await URLSession.shared.data(from: url)

        guard let httpResponse = response as? HTTPURLResponse,
              httpResponse.statusCode == 200 else {
            throw TMDBError.requestFailed
        }

        let decoded = try JSONDecoder().decode(WatchProvidersResponse.self, from: data)
        return decoded.results["FR"]
    }

    func searchMovies(query: String) async throws -> [Movie] {
        guard !query.trimmingCharacters(in: .whitespaces).isEmpty else { return [] }

        var components = URLComponents(string: "\(baseURL)/search/movie")
        components?.queryItems = [
            URLQueryItem(name: "api_key", value: apiKey),
            URLQueryItem(name: "language", value: await MainActor.run { AppSettings.shared.appLanguage.tmdbLanguageCode }),
            URLQueryItem(name: "query", value: query),
            URLQueryItem(name: "include_adult", value: "false")
        ]

        guard let url = components?.url else {
            throw TMDBError.invalidURL
        }

        let (data, response) = try await URLSession.shared.data(from: url)

        guard let httpResponse = response as? HTTPURLResponse,
              httpResponse.statusCode == 200 else {
            throw TMDBError.requestFailed
        }

        do {
            let decoded = try JSONDecoder().decode(TMDBResponse.self, from: data)
            return decoded.results
        } catch {
            throw TMDBError.decodingFailed
        }
    }
}
