import SwiftUI

struct TrophyDetailView: View {
    let trophy: TrophyProgress

    var body: some View {
        ZStack {
            Color.mooCream.ignoresSafeArea()

            ScrollView {
                VStack(spacing: 20) {
                    VStack(spacing: 10) {
                        Image(systemName: trophy.icon)
                            .font(.system(size: 40, weight: .bold))
                            .foregroundColor(trophy.isUnlocked ? trophy.tier!.color : .mooBeige)
                            .frame(width: 84, height: 84)
                            .background((trophy.isUnlocked ? trophy.tier!.color : .mooBeige).opacity(0.15))
                            .clipShape(Circle())

                        Text(trophy.title)
                            .font(.system(.title3, design: .rounded, weight: .bold))
                            .foregroundColor(.mooDark)

                        Text("\(trophy.count) film\(trophy.count == 1 ? "" : "s") comptabilisé\(trophy.count == 1 ? "" : "s")")
                            .font(.system(.subheadline, design: .rounded))
                            .foregroundColor(.mooTaupe)
                    }
                    .padding(.top, 12)

                    VStack(spacing: 10) {
                        ForEach(trophy.milestones) { milestone in
                            milestoneRow(milestone)
                        }
                    }
                    .padding(.horizontal)

                    Text(trophy.description)
                        .font(.system(.footnote, design: .rounded))
                        .foregroundColor(.mooTaupe)
                        .multilineTextAlignment(.center)
                        .padding(.horizontal, 24)
                }
                .padding(.bottom, 24)
            }
        }
        .navigationTitle(trophy.title)
        .navigationBarTitleDisplayMode(.inline)
    }

    private func milestoneRow(_ milestone: TrophyMilestone) -> some View {
        HStack(spacing: 12) {
            Image(systemName: milestone.isUnlocked ? "checkmark.seal.fill" : "lock.fill")
                .foregroundColor(milestone.isUnlocked ? milestone.tier.color : .mooBeige)
                .frame(width: 24)

            VStack(alignment: .leading, spacing: 2) {
                Text(milestone.tier.label)
                    .font(.system(.subheadline, design: .rounded, weight: .bold))
                    .foregroundColor(.mooDark)

                if let date = milestone.unlockedDate {
                    Text("Débloqué le \(date.formatted(.dateTime.day().month().year()))")
                        .font(.caption2)
                        .foregroundColor(.mooTaupe)
                } else {
                    let remaining = milestone.threshold - trophy.count
                    Text("Encore \(remaining) film\(remaining == 1 ? "" : "s") pour ce palier (\(milestone.threshold) requis)")
                        .font(.caption2)
                        .foregroundColor(.mooTaupe)
                }
            }

            Spacer()

            Text("\(milestone.threshold)")
                .font(.system(.subheadline, design: .rounded, weight: .bold))
                .foregroundColor(milestone.isUnlocked ? milestone.tier.color : .mooTaupe)
        }
        .padding(12)
        .background(Color.white)
        .cornerRadius(14)
        .overlay(
            RoundedRectangle(cornerRadius: 14)
                .stroke(milestone.isUnlocked ? milestone.tier.color.opacity(0.4) : Color.mooBeige, lineWidth: 1.5)
        )
    }
}

#Preview {
    NavigationStack {
        TrophyDetailView(trophy: TrophyEngine.trophies(for: []).first!)
    }
}
