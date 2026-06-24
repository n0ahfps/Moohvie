import Foundation

struct GenreLookup {
    static let names: [Int: String] = [
        28: "Action",
        12: "Aventure",
        16: "Animation",
        35: "Comédie",
        80: "Crime",
        99: "Documentaire",
        18: "Drame",
        10751: "Familial",
        14: "Fantastique",
        27: "Horreur",
        10402: "Musique",
        9648: "Mystère",
        10749: "Romance",
        878: "Science-Fiction",
        53: "Thriller",
        10752: "Guerre",
        37: "Western"
    ]

    static func name(for id: Int) -> String {
        names[id] ?? "Autre"
    }
}
