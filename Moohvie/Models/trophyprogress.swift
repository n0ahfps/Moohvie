import Foundation

struct TrophyMilestone: Identifiable {
    let tier: TrophyTier
    let threshold: Int
    let unlockedDate: Date?

    var id: Int { tier.rawValue }
    var isUnlocked: Bool { unlockedDate != nil }
}

struct TrophyProgress: Identifiable {
    let id: String
    let title: String
    let description: String
    let icon: String
    let count: Int
    let tier: TrophyTier?
    let nextThreshold: Int?
    let milestones: [TrophyMilestone]

    var isUnlocked: Bool { tier != nil }
}
