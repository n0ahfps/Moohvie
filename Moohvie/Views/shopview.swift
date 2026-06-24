import SwiftUI

struct ShopView: View {
    @ObservedObject private var shop = ShopStore.shared
    @ObservedObject private var points = MoohPointsStore.shared
    @State private var selectedCategory: ShopItemCategory = .theme

    private var itemsInCategory: [ShopItem] {
        ShopCatalog.items.filter { $0.category == selectedCategory }
    }

    var body: some View {
        ZStack {
            Color.mooCream.ignoresSafeArea()

            VStack(spacing: 0) {
                HStack(spacing: 6) {
                    Image(systemName: "seal.fill")
                    Text("\(points.totalPoints) points")
                }
                .font(.system(.subheadline, design: .rounded, weight: .bold))
                .foregroundColor(.mooOrange)
                .padding(.horizontal, 14)
                .padding(.vertical, 6)
                .background(Color.mooOrange.opacity(0.15))
                .clipShape(Capsule())
                .padding(.top, 8)

                Picker("Catégorie", selection: $selectedCategory) {
                    ForEach(ShopItemCategory.allCases, id: \.self) { category in
                        Text(LocalizedStringKey(category.label)).tag(category)
                    }
                }
                .pickerStyle(.segmented)
                .padding()

                ScrollView {
                    LazyVStack(spacing: 10) {
                        ForEach(itemsInCategory) { item in
                            itemRow(item)
                        }
                    }
                    .padding(.horizontal)
                    .padding(.bottom, 20)
                }
            }
        }
        .navigationTitle("Boutique")
        .navigationBarTitleDisplayMode(.inline)
    }

    private func itemRow(_ item: ShopItem) -> some View {
        let isOwned = shop.isOwned(item)
        let isEquipped = isItemEquipped(item)

        return HStack(spacing: 12) {
            itemIcon(item)

            VStack(alignment: .leading, spacing: 2) {
                Text(LocalizedStringKey(item.name))
                    .font(.system(.body, design: .rounded, weight: .bold))
                    .foregroundColor(.mooDark)

                if !isOwned {
                    HStack(spacing: 4) {
                        Image(systemName: "seal.fill")
                            .font(.caption2)
                        Text("\(item.cost)")
                            .font(.caption)
                    }
                    .foregroundColor(.mooTaupe)
                }
            }

            Spacer()

            actionButton(item, isOwned: isOwned, isEquipped: isEquipped)
        }
        .padding(12)
        .background(Color.white)
        .cornerRadius(14)
        .overlay(
            RoundedRectangle(cornerRadius: 14)
                .stroke(isEquipped ? Color.mooOrange : Color.mooBeige, lineWidth: isEquipped ? 2 : 1.5)
        )
    }

    private func itemIcon(_ item: ShopItem) -> some View {
        Group {
            switch item.category {
            case .theme:
                Circle()
                    .fill(item.accentColor ?? .mooOrange)
                    .frame(width: 32, height: 32)
            case .title:
                Image(systemName: "text.badge.star")
                    .foregroundColor(.mooOrange)
                    .frame(width: 32, height: 32)
            case .badge:
                Text(item.badgeIcon ?? "🏅")
                    .font(.system(size: 24))
                    .frame(width: 32, height: 32)
            }
        }
    }

    private func actionButton(_ item: ShopItem, isOwned: Bool, isEquipped: Bool) -> some View {
        Group {
            if isEquipped {
                Text("Équipé")
                    .font(.system(.caption, design: .rounded, weight: .bold))
                    .foregroundColor(.mooGreen)
            } else if isOwned {
                Button {
                    shop.equip(item)
                } label: {
                    Text("Équiper")
                        .font(.system(.caption, design: .rounded, weight: .bold))
                        .foregroundColor(.white)
                        .padding(.horizontal, 12)
                        .padding(.vertical, 6)
                        .background(Color.mooOrange)
                        .clipShape(Capsule())
                }
            } else {
                Button {
                    shop.purchase(item)
                } label: {
                    Text("Acheter")
                        .font(.system(.caption, design: .rounded, weight: .bold))
                        .foregroundColor(.mooDark)
                        .padding(.horizontal, 12)
                        .padding(.vertical, 6)
                        .background(points.totalPoints >= item.cost ? Color.mooOrange.opacity(0.3) : Color.mooBeige)
                        .clipShape(Capsule())
                }
                .disabled(points.totalPoints < item.cost)
            }
        }
    }

    private func isItemEquipped(_ item: ShopItem) -> Bool {
        switch item.category {
        case .theme: return shop.equippedThemeID == item.id
        case .title: return shop.equippedTitleID == item.id
        case .badge: return shop.equippedBadgeID == item.id
        }
    }
}

#Preview {
    NavigationStack {
        ShopView()
    }
}
