import SwiftUI

struct TrophiesView: View {
    @ObservedObject private var store = CineTableStore.shared

    private var trophies: [TrophyProgress] {
        TrophyEngine.trophies(for: store.watchedMovies)
    }

    private let columns = [
        GridItem(.flexible(), spacing: 12),
        GridItem(.flexible(), spacing: 12)
    ]

    var body: some View {
        ZStack {
            Color.mooCream.ignoresSafeArea()

            ScrollView {
                LazyVGrid(columns: columns, spacing: 12) {
                    ForEach(trophies) { trophy in
                        NavigationLink {
                            TrophyDetailView(trophy: trophy)
                        } label: {
                            trophyCard(trophy)
                        }
                        .buttonStyle(.plain)
                    }
                }
                .padding()
            }
        }
        .navigationTitle("Trophées")
    }

    private func trophyCard(_ trophy: TrophyProgress) -> some View {
        VStack(spacing: 8) {
            Image(systemName: trophy.icon)
                .font(.system(size: 28, weight: .bold))
                .foregroundColor(trophy.isUnlocked ? trophy.tier!.color : .mooBeige)
                .frame(width: 56, height: 56)
                .background((trophy.isUnlocked ? trophy.tier!.color : .mooBeige).opacity(0.15))
                .clipShape(Circle())

            Text(LocalizedStringKey(trophy.title))
                .font(.system(.caption, design: .rounded, weight: .bold))
                .foregroundColor(.mooDark)
                .multilineTextAlignment(.center)
                .lineLimit(2)

            if let tier = trophy.tier {
                Text(LocalizedStringKey(tier.label))
                    .font(.caption2)
                    .fontWeight(.bold)
                    .foregroundColor(tier.color)
            } else {
                Text("Non débloqué")
                    .font(.caption2)
                    .foregroundColor(.mooTaupe)
            }

            if let nextThreshold = trophy.nextThreshold {
                Text("\(trophy.count)/\(nextThreshold)")
                    .font(.caption2)
                    .foregroundColor(.mooTaupe)
            } else {
                Text("Maîtrisé")
                    .font(.caption2)
                    .foregroundColor(.mooTaupe)
            }
        }
        .padding()
        .frame(maxWidth: .infinity)
        .background(Color.white)
        .cornerRadius(16)
        .overlay(
            RoundedRectangle(cornerRadius: 16)
                .stroke(Color.mooBeige, lineWidth: 1.5)
        )
    }
}

#Preview {
    NavigationStack {
        TrophiesView()
    }
}
