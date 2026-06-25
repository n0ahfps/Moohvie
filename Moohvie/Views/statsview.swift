import SwiftUI

struct StatsView: View {
    @ObservedObject private var store = CineTableStore.shared

    var body: some View {
        let stats = store.stats
        ZStack {
            Color.mooCream.ignoresSafeArea()

            if stats.totalMovies == 0 {
                Text("Ajoutez des films au Cinétable pour voir vos stats")
                    .font(.system(.subheadline, design: .rounded))
                    .foregroundColor(.mooTaupe)
                    .multilineTextAlignment(.center)
                    .padding()
            } else {
                ScrollView {
                    VStack(spacing: 14) {
                        StatCard(emoji: "🎬", label: "Films vus ensemble", value: "\(stats.totalMovies)")

                        if let genre = stats.favoriteGenreName {
                            StatCard(emoji: "🎭", label: "Genre favori", value: genre)
                        }

                        if let avg = stats.averagePersonalRating {
                            StatCard(emoji: "⭐", label: "Note moyenne", value: String(format: "%.1f / 5", avg))
                        }

                        if let perMonth = stats.moviesPerMonth {
                            StatCard(emoji: "📅", label: "Films par mois", value: String(format: "%.1f", perMonth))
                        }

                        if let top = stats.topRatedMovie {
                            TopMovieCard(movie: top)
                        }
                    }
                    .padding()
                }
            }
        }
        .navigationTitle("Nos stats")
    }
}

private struct StatCard: View {
    let emoji: String
    let label: String
    let value: String

    var body: some View {
        HStack(spacing: 14) {
            Text(emoji)
                .font(.title2)
            VStack(alignment: .leading, spacing: 2) {
                Text(label)
                    .font(.system(.caption, design: .rounded))
                    .foregroundColor(.mooTaupe)
                Text(value)
                    .font(.system(.title3, design: .rounded, weight: .bold))
                    .foregroundColor(.mooDark)
            }
            Spacer()
        }
        .padding()
        .background(Color.white)
        .cornerRadius(16)
        .overlay(RoundedRectangle(cornerRadius: 16).stroke(Color.mooBeige, lineWidth: 1.5))
    }
}

private struct TopMovieCard: View {
    let movie: WatchedMovie

    var body: some View {
        HStack(spacing: 12) {
            AsyncImage(url: movie.posterURL) { phase in
                if let image = phase.image {
                    image.resizable().aspectRatio(contentMode: .fill)
                } else {
                    Color.mooBeige
                }
            }
            .frame(width: 56, height: 84)
            .clipShape(RoundedRectangle(cornerRadius: 10))

            VStack(alignment: .leading, spacing: 6) {
                Text("🏆 Film préféré")
                    .font(.system(.caption, design: .rounded))
                    .foregroundColor(.mooTaupe)
                Text(movie.title)
                    .font(.system(.subheadline, design: .rounded, weight: .bold))
                    .foregroundColor(.mooDark)
                    .lineLimit(2)
                HStack(spacing: 2) {
                    ForEach(1...5, id: \.self) { i in
                        Image(systemName: i <= movie.personalRating ? "star.fill" : "star")
                            .font(.caption2)
                            .foregroundColor(.mooOrange)
                    }
                }
            }
            Spacer()
        }
        .padding()
        .background(Color.white)
        .cornerRadius(16)
        .overlay(RoundedRectangle(cornerRadius: 16).stroke(Color.mooBeige, lineWidth: 1.5))
    }
}

#Preview {
    NavigationStack {
        StatsView()
    }
}
