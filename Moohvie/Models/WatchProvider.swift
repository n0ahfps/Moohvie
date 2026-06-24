import Foundation

struct WatchProvider: Identifiable, Decodable {
    let id: Int
    let name: String
    let logoPath: String?

    enum CodingKeys: String, CodingKey {
        case id = "provider_id"
        case name = "provider_name"
        case logoPath = "logo_path"
    }

    var logoURL: URL? {
        guard let logoPath else { return nil }
        return URL(string: "https://image.tmdb.org/t/p/w92\(logoPath)")
    }
}

struct WatchProvidersResponse: Decodable {
    let results: [String: CountryProviders]
}

struct CountryProviders: Decodable {
    let flatrate: [WatchProvider]?
    let rent: [WatchProvider]?
    let buy: [WatchProvider]?
}
