import Foundation

struct StreamingProviderOption: Identifiable, Hashable {
    let id: Int
    let name: String

    static let all: [StreamingProviderOption] = [
        StreamingProviderOption(id: 8, name: "Netflix"),
        StreamingProviderOption(id: 119, name: "Prime Video"),
        StreamingProviderOption(id: 337, name: "Disney+"),
        StreamingProviderOption(id: 350, name: "Apple TV+"),
        StreamingProviderOption(id: 381, name: "Canal+"),
        StreamingProviderOption(id: 56, name: "OCS"),
        StreamingProviderOption(id: 531, name: "Paramount+"),
        StreamingProviderOption(id: 1899, name: "Max"),
        StreamingProviderOption(id: 283, name: "Crunchyroll"),
        StreamingProviderOption(id: 188, name: "YouTube Premium"),
        StreamingProviderOption(id: 358, name: "Salto"),
        StreamingProviderOption(id: 333, name: "ARTE"),
        StreamingProviderOption(id: 234, name: "Mubi"),
        StreamingProviderOption(id: 552, name: "Filmo"),
        StreamingProviderOption(id: 1968, name: "Ciné+")
    ]
}
