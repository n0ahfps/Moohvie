import Foundation
import Combine
import SwiftUI
import UIKit

@MainActor
class ProfileStore: ObservableObject {
    static let shared = ProfileStore()

    @Published var username: String {
        didSet {
            UserDefaults.standard.set(username, forKey: "profileUsername")
        }
    }

    @Published var profileImageData: Data? {
        didSet {
            UserDefaults.standard.set(profileImageData, forKey: "profileImageData")
        }
    }

    var profileImage: Image? {
        guard let data = profileImageData, let uiImage = UIImage(data: data) else { return nil }
        return Image(uiImage: uiImage)
    }

    private init() {
        self.username = UserDefaults.standard.string(forKey: "profileUsername") ?? "Cinéphile"
        self.profileImageData = UserDefaults.standard.data(forKey: "profileImageData")
    }
}
