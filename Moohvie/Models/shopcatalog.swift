import SwiftUI

struct ShopCatalog {
    /// Le thème "Classique" est toujours possédé et équipé par défaut, gratuitement.
    static let defaultThemeID = "theme-classic"

    static let items: [ShopItem] = [
        ShopItem(id: defaultThemeID, category: .theme, name: "Classique", cost: 0, accentColor: .mooOrangeBase),
        ShopItem(id: "theme-ocean", category: .theme, name: "Océan", cost: 50, accentColor: Color(red: 0.20, green: 0.55, blue: 0.85)),
        ShopItem(id: "theme-forest", category: .theme, name: "Forêt", cost: 50, accentColor: Color(red: 0.20, green: 0.55, blue: 0.30)),
        ShopItem(id: "theme-ruby", category: .theme, name: "Rubis", cost: 75, accentColor: Color(red: 0.75, green: 0.15, blue: 0.25)),
        ShopItem(id: "theme-mystic", category: .theme, name: "Mystère", cost: 100, accentColor: Color(red: 0.45, green: 0.25, blue: 0.70)),
        ShopItem(id: "theme-gold", category: .theme, name: "Doré", cost: 150, accentColor: Color(red: 0.80, green: 0.65, blue: 0.10)),

        ShopItem(id: "title-novice", category: .title, name: "Spectateur novice", cost: 0),
        ShopItem(id: "title-curious", category: .title, name: "Curieux du septième art", cost: 30),
        ShopItem(id: "title-et", category: .title, name: "Maison, téléphone", cost: 40),
        ShopItem(id: "title-explorer", category: .title, name: "Explorateur de genres", cost: 60),
        ShopItem(id: "title-jedi", category: .title, name: "Que la force soit avec toi", cost: 70),
        ShopItem(id: "title-westworld", category: .title, name: "Il était une fois au cinéma", cost: 80),
        ShopItem(id: "title-pulp", category: .title, name: "Pulp Spectateur", cost: 90),
        ShopItem(id: "title-critic", category: .title, name: "Critique redouté", cost: 100),
        ShopItem(id: "title-odyssey", category: .title, name: "2001 : l'Odyssée du Popcorn", cost: 110),
        ShopItem(id: "title-ring", category: .title, name: "Un Cinétable pour les gouverner tous", cost: 130),
        ShopItem(id: "title-kane", category: .title, name: "Citizen Cinéphile", cost: 150),
        ShopItem(id: "title-godfather", category: .title, name: "Le Parrain du Cinétable", cost: 175),
        ShopItem(id: "title-legend", category: .title, name: "Légende du Cinétable", cost: 200),

        ShopItem(id: "badge-popcorn", category: .badge, name: "Popcorn d'or", cost: 0, badgeIcon: "🍿"),
        ShopItem(id: "badge-clapper", category: .badge, name: "Clap de cinéma", cost: 30, badgeIcon: "🎬"),
        ShopItem(id: "badge-shark", category: .badge, name: "Les Dents de la mer", cost: 40, badgeIcon: "🦈"),
        ShopItem(id: "badge-ghost", category: .badge, name: "S.O.S. Fantômes", cost: 50, badgeIcon: "👻"),
        ShopItem(id: "badge-dino", category: .badge, name: "Jurassic Cinétable", cost: 60, badgeIcon: "🦖"),
        ShopItem(id: "badge-star", category: .badge, name: "Étoile filante", cost: 70, badgeIcon: "🌟"),
        ShopItem(id: "badge-ring", category: .badge, name: "L'Anneau Unique", cost: 80, badgeIcon: "💍"),
        ShopItem(id: "badge-ufo", category: .badge, name: "Rencontre du troisième type", cost: 90, badgeIcon: "🛸"),
        ShopItem(id: "badge-ship", category: .badge, name: "Le Roi du monde", cost: 100, badgeIcon: "🚢"),
        ShopItem(id: "badge-spider", category: .badge, name: "Avec de grands pouvoirs...", cost: 110, badgeIcon: "🕷️"),
        ShopItem(id: "badge-bat", category: .badge, name: "Le Chevalier Noir", cost: 120, badgeIcon: "🦇"),
        ShopItem(id: "badge-dragon", category: .badge, name: "Mère des Dragons", cost: 140, badgeIcon: "🐉"),
        ShopItem(id: "badge-crown", category: .badge, name: "Couronne du Cinétable", cost: 175, badgeIcon: "👑")
    ]

    static func item(withID id: String) -> ShopItem? {
        items.first { $0.id == id }
    }
}

private extension Color {
    static let mooOrangeBase = Color(red: 244 / 255, green: 162 / 255, blue: 38 / 255)
}
