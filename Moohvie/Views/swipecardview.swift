import SwiftUI

struct SwipeCardView: View {
    @ObservedObject private var shop = ShopStore.shared
    let movie: Movie
    var posterHeight: CGFloat = 420

    var body: some View {
        VStack(spacing: 0) {
            AsyncImage(url: movie.posterURL) { phase in
                if let image = phase.image {
                    image
                        .resizable()
                        .aspectRatio(contentMode: .fill)
                } else if phase.error != nil {
                    Color.mooBeige
                } else {
                    ProgressView()
                        .tint(.mooOrange)
                }
            }
            .frame(height: posterHeight)
            .clipped()

            VStack(alignment: .leading, spacing: 6) {
                Text(movie.title)
                    .font(.system(.title3, design: .rounded, weight: .bold))
                    .foregroundColor(.mooDark)
                    .lineLimit(1)

                HStack {
                    Image(systemName: "star.fill")
                        .foregroundColor(.mooOrange)
                    Text(String(format: "%.1f", movie.voteAverage))
                        .bold()
                        .foregroundColor(.mooDark)
                    if let year = movie.releaseDate?.prefix(4) {
                        Text("• \(year)")
                            .foregroundColor(.mooTaupe)
                    }
                }
                .font(.subheadline)

                Text(movie.overview)
                    .font(.callout)
                    .foregroundColor(.mooTaupe)
                    .lineLimit(2)
            }
            .padding(12)
            .background(Color.white)
        }
        .background(Color.white)
        .cornerRadius(20)
        .overlay(
            RoundedRectangle(cornerRadius: 20)
                .stroke(Color.mooBeige, lineWidth: 1.5)
        )
    }
}
