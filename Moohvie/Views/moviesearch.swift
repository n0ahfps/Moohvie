import SwiftUI

struct MovieSearchView: View {
    @ObservedObject private var shop = ShopStore.shared
    @Environment(\.dismiss) private var dismiss
    @State private var query = ""
    @State private var results: [Movie] = []
    @State private var isSearching = false
    @State private var addedIDs: Set<Int> = []
    private let service = TMDBService()

    var body: some View {
        ZStack {
            Color.mooCream.ignoresSafeArea()

            VStack(spacing: 16) {
                HStack {
                    Image(systemName: "magnifyingglass")
                        .foregroundColor(.mooTaupe)
                    TextField("Titre du film...", text: $query)
                        .font(.system(.body, design: .rounded))
                        .autocorrectionDisabled()
                        .onSubmit {
                            search()
                        }
                    if !query.isEmpty {
                        Button {
                            query = ""
                            results = []
                        } label: {
                            Image(systemName: "xmark.circle.fill")
                                .foregroundColor(.mooTaupe)
                        }
                    }
                }
                .padding()
                .background(Color.white)
                .cornerRadius(14)
                .overlay(
                    RoundedRectangle(cornerRadius: 14)
                        .stroke(Color.mooBeige, lineWidth: 1.5)
                )
                .padding(.horizontal)
                .padding(.top, 8)

                if isSearching {
                    Spacer()
                    ProgressView()
                        .tint(.mooOrange)
                    Spacer()
                } else if results.isEmpty && !query.isEmpty {
                    Spacer()
                    Text("Aucun film trouvé")
                        .font(.system(.body, design: .rounded))
                        .foregroundColor(.mooTaupe)
                    Spacer()
                } else {
                    ScrollView {
                        VStack(spacing: 10) {
                            ForEach(results) { movie in
                                searchResultRow(movie)
                            }
                        }
                        .padding(.horizontal)
                    }
                }
            }
        }
        .navigationTitle("Ajouter un film")
        .navigationBarTitleDisplayMode(.inline)
        .toolbar {
            ToolbarItem(placement: .topBarTrailing) {
                Button("Fermer") { dismiss() }
                    .foregroundColor(.mooOrange)
            }
        }
        .onChange(of: query) {
            search()
        }
    }

    private func search() {
            Task {
                isSearching = true
                do {
                    results = try await service.searchMovies(query: query)
                } catch {
                    results = []
                }
                isSearching = false
            }
        }

    private func searchResultRow(_ movie: Movie) -> some View {
        let alreadyAdded = addedIDs.contains(movie.id) || CineTableStore.shared.isWatched(movie.id)

        return HStack(spacing: 12) {
            AsyncImage(url: movie.posterURL) { phase in
                if let image = phase.image {
                    image
                        .resizable()
                        .aspectRatio(contentMode: .fill)
                } else {
                    Color.mooBeige
                }
            }
            .frame(width: 50, height: 75)
            .clipShape(RoundedRectangle(cornerRadius: 8))

            VStack(alignment: .leading, spacing: 4) {
                Text(movie.title)
                    .font(.system(.subheadline, design: .rounded, weight: .bold))
                    .foregroundColor(.mooDark)
                    .lineLimit(2)
                if let year = movie.releaseDate?.prefix(4) {
                    Text(String(year))
                        .font(.caption)
                        .foregroundColor(.mooTaupe)
                }
            }

            Spacer()

            Button {
                CineTableStore.shared.add(movie)
                addedIDs.insert(movie.id)
            } label: {
                Image(systemName: alreadyAdded ? "checkmark.circle.fill" : "plus.circle.fill")
                    .font(.system(size: 26))
                    .foregroundColor(alreadyAdded ? .mooGreen : .mooOrange)
            }
            .disabled(alreadyAdded)
        }
        .padding(10)
        .background(Color.white)
        .cornerRadius(14)
        .overlay(
            RoundedRectangle(cornerRadius: 14)
                .stroke(Color.mooBeige, lineWidth: 1.5)
        )
    }
}

#Preview {
    NavigationStack {
        MovieSearchView()
    }
}
