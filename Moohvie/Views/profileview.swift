import SwiftUI
import PhotosUI

struct ProfileView: View {
    @ObservedObject private var profile = ProfileStore.shared
    @ObservedObject private var shop = ShopStore.shared
    @State private var photoPickerItem: PhotosPickerItem?
    @State private var isEditingUsername = false
    @State private var draftUsername = ""

    var body: some View {
        ZStack {
            Color.mooCream.ignoresSafeArea()

            ScrollView {
                VStack(spacing: 20) {
                    avatarPicker
                        .padding(.top, 16)

                    if isEditingUsername {
                        HStack {
                            TextField("Pseudo", text: $draftUsername)
                                .font(.system(.title3, design: .rounded, weight: .bold))
                                .multilineTextAlignment(.center)
                                .textFieldStyle(.roundedBorder)

                            Button {
                                profile.username = draftUsername.isEmpty ? profile.username : draftUsername
                                isEditingUsername = false
                            } label: {
                                Image(systemName: "checkmark.circle.fill")
                                    .foregroundColor(.mooGreen)
                            }
                        }
                        .padding(.horizontal, 32)
                    } else {
                        Button {
                            draftUsername = profile.username
                            isEditingUsername = true
                        } label: {
                            HStack(spacing: 6) {
                                Text(profile.username)
                                    .font(.system(.title3, design: .rounded, weight: .bold))
                                    .foregroundColor(.mooDark)
                                Image(systemName: "pencil")
                                    .font(.caption)
                                    .foregroundColor(.mooTaupe)
                            }
                        }
                    }

                    if let titleName = shop.equippedTitleName {
                        Text(LocalizedStringKey(titleName))
                            .font(.system(.subheadline, design: .rounded, weight: .bold))
                            .foregroundColor(.mooOrange)
                    }

                    if let badgeIcon = shop.equippedBadgeIcon {
                        Text(badgeIcon)
                            .font(.system(size: 32))
                    }

                    sectionPicker(category: .title, title: "Titre équipé")
                    sectionPicker(category: .badge, title: "Badge équipé")
                }
                .padding(.bottom, 24)
            }
        }
        .navigationTitle("Profil")
        .navigationBarTitleDisplayMode(.inline)
    }

    private var avatarPicker: some View {
        PhotosPicker(selection: $photoPickerItem, matching: .images) {
            ZStack(alignment: .bottomTrailing) {
                if let image = profile.profileImage {
                    image
                        .resizable()
                        .scaledToFill()
                        .frame(width: 100, height: 100)
                        .clipShape(Circle())
                } else {
                    Text("🐮")
                        .font(.system(size: 56))
                        .frame(width: 100, height: 100)
                        .background(Color.white)
                        .clipShape(Circle())
                }

                Image(systemName: "camera.circle.fill")
                    .font(.system(size: 24))
                    .foregroundColor(.mooOrange)
                    .background(Color.white, in: Circle())
            }
            .overlay(Circle().stroke(Color.mooBeige, lineWidth: 1.5))
        }
        .onChange(of: photoPickerItem) { _, newItem in
            Task {
                if let data = try? await newItem?.loadTransferable(type: Data.self) {
                    profile.profileImageData = data
                }
            }
        }
    }

    private func sectionPicker(category: ShopItemCategory, title: String) -> some View {
        let ownedItems = ShopCatalog.items.filter { $0.category == category && shop.isOwned($0) }

        return VStack(alignment: .leading, spacing: 10) {
            Text(LocalizedStringKey(title))
                .font(.system(.headline, design: .rounded, weight: .bold))
                .foregroundColor(.mooDark)
                .padding(.horizontal)

            VStack(spacing: 8) {
                ForEach(ownedItems) { item in
                    Button {
                        shop.equip(item)
                    } label: {
                        HStack {
                            Text(LocalizedStringKey(item.name))
                                .font(.system(.body, design: .rounded, weight: .bold))
                                .foregroundColor(.mooDark)
                            Spacer()
                            Image(systemName: isEquipped(item) ? "checkmark.circle.fill" : "circle")
                                .foregroundColor(isEquipped(item) ? .mooGreen : .mooTaupe)
                        }
                        .padding()
                        .background(Color.white)
                        .cornerRadius(12)
                        .overlay(
                            RoundedRectangle(cornerRadius: 12)
                                .stroke(Color.mooBeige, lineWidth: 1.5)
                        )
                    }
                }
            }
            .padding(.horizontal)
        }
    }

    private func isEquipped(_ item: ShopItem) -> Bool {
        switch item.category {
        case .theme: return shop.equippedThemeID == item.id
        case .title: return shop.equippedTitleID == item.id
        case .badge: return shop.equippedBadgeID == item.id
        }
    }
}

#Preview {
    NavigationStack {
        ProfileView()
    }
}
