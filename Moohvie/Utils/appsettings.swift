import Foundation
import Combine

@MainActor
class AppSettings: ObservableObject {
    static let shared = AppSettings()

    @Published var selectedProviderIDs: Set<Int> {
        didSet {
            UserDefaults.standard.set(Array(selectedProviderIDs), forKey: "selectedProviderIDs")
        }
    }

    @Published var maxCertification: FrenchCertification {
        didSet {
            UserDefaults.standard.set(maxCertification.rawValue, forKey: "maxCertification")
        }
    }

    @Published var allowRewatching: Bool {
        didSet {
            UserDefaults.standard.set(allowRewatching, forKey: "allowRewatching")
        }
    }

    private init() {
        let savedIDs = UserDefaults.standard.array(forKey: "selectedProviderIDs") as? [Int] ?? []
        self.selectedProviderIDs = Set(savedIDs)

        let savedCert = UserDefaults.standard.string(forKey: "maxCertification") ?? FrenchCertification.all.rawValue
        self.maxCertification = FrenchCertification(rawValue: savedCert) ?? .all

        self.allowRewatching = UserDefaults.standard.bool(forKey: "allowRewatching")
    }
}

enum FrenchCertification: String, CaseIterable {
    case all = "Tous publics"
    case twelve = "-12"
    case sixteen = "-16"
    case eighteen = "-18"

    var maxAllowed: String {
        switch self {
        case .all: return "U"
        case .twelve: return "12"
        case .sixteen: return "16"
        case .eighteen: return "18"
        }
    }
}
