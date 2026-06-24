import Foundation
import Combine
import SwiftUI

@MainActor
class ShopStore: ObservableObject {
    static let shared = ShopStore()

    @Published private(set) var ownedItemIDs: Set<String> {
        didSet {
            UserDefaults.standard.set(Array(ownedItemIDs), forKey: "shopOwnedItemIDs")
        }
    }

    @Published var equippedThemeID: String {
        didSet {
            UserDefaults.standard.set(equippedThemeID, forKey: "shopEquippedThemeID")
        }
    }

    @Published var equippedTitleID: String? {
        didSet {
            UserDefaults.standard.set(equippedTitleID, forKey: "shopEquippedTitleID")
        }
    }

    @Published var equippedBadgeID: String? {
        didSet {
            UserDefaults.standard.set(equippedBadgeID, forKey: "shopEquippedBadgeID")
        }
    }

    private init() {
        let savedOwned = UserDefaults.standard.array(forKey: "shopOwnedItemIDs") as? [String] ?? []
        self.ownedItemIDs = Set(savedOwned).union([
            ShopCatalog.defaultThemeID,
            "title-novice",
            "badge-popcorn"
        ])

        self.equippedThemeID = UserDefaults.standard.string(forKey: "shopEquippedThemeID") ?? ShopCatalog.defaultThemeID
        self.equippedTitleID = UserDefaults.standard.string(forKey: "shopEquippedTitleID")
        self.equippedBadgeID = UserDefaults.standard.string(forKey: "shopEquippedBadgeID")
    }

    func isOwned(_ item: ShopItem) -> Bool {
        ownedItemIDs.contains(item.id)
    }

    @discardableResult
    func purchase(_ item: ShopItem) -> Bool {
        guard !isOwned(item) else { return true }
        guard MoohPointsStore.shared.spend(item.cost) else { return false }
        ownedItemIDs.insert(item.id)
        return true
    }

    func equip(_ item: ShopItem) {
        guard isOwned(item) else { return }
        switch item.category {
        case .theme: equippedThemeID = item.id
        case .title: equippedTitleID = item.id
        case .badge: equippedBadgeID = item.id
        }
    }

    var ownedThemes: [ShopItem] {
        ShopCatalog.items.filter { $0.category == .theme && isOwned($0) }
    }

    var equippedAccentColor: Color {
        ShopCatalog.item(withID: equippedThemeID)?.accentColor ?? ShopCatalog.item(withID: ShopCatalog.defaultThemeID)?.accentColor ?? .orange
    }

    var equippedTitleName: String? {
        equippedTitleID.flatMap(ShopCatalog.item(withID:))?.name
    }

    var equippedBadgeIcon: String? {
        equippedBadgeID.flatMap(ShopCatalog.item(withID:))?.badgeIcon
    }
}
