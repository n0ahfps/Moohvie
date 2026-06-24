import SwiftUI

enum TrophyTier: Int, CaseIterable {
    case bronze
    case argent
    case or
    case platine
    case emeraude
    case diamant
    case cinephile

    var label: String {
        switch self {
        case .bronze: return "Bronze"
        case .argent: return "Argent"
        case .or: return "Or"
        case .platine: return "Platine"
        case .emeraude: return "Émeraude"
        case .diamant: return "Diamant"
        case .cinephile: return "Cinéphile"
        }
    }

    var color: Color {
        switch self {
        case .bronze: return Color(red: 0.72, green: 0.45, blue: 0.2)
        case .argent: return Color(white: 0.68)
        case .or: return Color(red: 1.0, green: 0.84, blue: 0.0)
        case .platine: return Color(red: 0.7, green: 0.85, blue: 0.9)
        case .emeraude: return Color(red: 0.0, green: 0.6, blue: 0.4)
        case .diamant: return Color(red: 0.3, green: 0.75, blue: 0.95)
        case .cinephile: return Color(red: 0.6, green: 0.2, blue: 0.8)
        }
    }
}
