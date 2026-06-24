import SwiftUI

enum ShopItemCategory: String, CaseIterable {
    case theme
    case title
    case badge

    var label: String {
        switch self {
        case .theme: return "Thèmes"
        case .title: return "Titres"
        case .badge: return "Badges"
        }
    }
}

struct ShopItem: Identifiable, Hashable {
    let id: String
    let category: ShopItemCategory
    let name: String
    let cost: Int

    /// Couleur d'accent pour les items de type `.theme`.
    var accentColor: Color?
    /// SF Symbol pour les items de type `.badge`.
    var badgeIcon: String?
}
