import Foundation
import Combine

@MainActor
class MoohPointsStore: ObservableObject {
    static let shared = MoohPointsStore()

    @Published private(set) var totalPoints: Int {
        didSet {
            UserDefaults.standard.set(totalPoints, forKey: storageKey)
        }
    }

    private let storageKey = "moohPointsTotal"

    private init() {
        self.totalPoints = UserDefaults.standard.integer(forKey: storageKey)
    }

    func award(_ points: Int) {
        totalPoints += points
    }

    func refund(_ points: Int) {
        totalPoints = max(0, totalPoints - points)
    }

    @discardableResult
    func spend(_ points: Int) -> Bool {
        guard totalPoints >= points else { return false }
        totalPoints -= points
        return true
    }
}
