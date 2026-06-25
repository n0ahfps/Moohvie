import SwiftUI

struct StarRatingView: View {
    @ObservedObject private var shop = ShopStore.shared
    let rating: Int
    var onRate: (Int) -> Void

    var body: some View {
        HStack(spacing: 4) {
            ForEach(1...5, id: \.self) { star in
                Button {
                    onRate(star)
                } label: {
                    Image(systemName: star <= rating ? "star.fill" : "star")
                        .foregroundColor(.mooOrange)
                        .font(.system(size: 16))
                }
            }
        }
    }
}
