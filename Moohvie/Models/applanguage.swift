import Foundation

enum AppLanguage: String, CaseIterable {
    case system
    case french
    case english

    var label: String {
        switch self {
        case .system: return "Système"
        case .french: return "Français"
        case .english: return "English"
        }
    }

    /// Identifiant à passer à `.environment(\.locale, ...)` pour piloter les `Text` du catalogue.
    var locale: Locale {
        switch self {
        case .system: return Locale.autoupdatingCurrent
        case .french: return Locale(identifier: "fr")
        case .english: return Locale(identifier: "en")
        }
    }

    /// Code de langue TMDB ("fr-FR", "en-US") utilisé pour récupérer titres/synopsis dans la bonne langue.
    var tmdbLanguageCode: String {
        switch self {
        case .system:
            return Locale.autoupdatingCurrent.language.languageCode?.identifier == "fr" ? "fr-FR" : "en-US"
        case .french: return "fr-FR"
        case .english: return "en-US"
        }
    }
}
