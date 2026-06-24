import SwiftUI

struct CineTableView: View {
    @ObservedObject var store = CineTableStore.shared
    @State private var movieToDelete: WatchedMovie?
    @State private var showSearch = false
    @State private var showFilters = false
    @State private var displayMode: CineTableDisplayMode = .grid
    @State private var filters = CineTableFilterState()

    private let columns = [
        GridItem(.flexible(), spacing: 12),
        GridItem(.flexible(), spacing: 12)
    ]

    private var displayedMovies: [WatchedMovie] {
        filters.apply(to: store.watchedMovies)
    }

    var body: some View {
        ZStack {
            Color.mooCream.ignoresSafeArea()

            if store.watchedMovies.isEmpty {
                emptyStateView
            } else {
                VStack(spacing: 12) {
                    controlsBar

                    if displayedMovies.isEmpty {
                        Spacer()
                        Text("Aucun film ne correspond à ces filtres")
                            .font(.system(.subheadline, design: .rounded))
                            .foregroundColor(.mooTaupe)
                        Spacer()
                    } else {
                        ScrollView {
                            if displayMode == .grid {
                                LazyVGrid(columns: columns, spacing: 16) {
                                    ForEach(displayedMovies) { movie in
                                        gridCard(movie)
                                    }
                                }
                                .padding()
                            } else {
                                LazyVStack(spacing: 10) {
                                    ForEach(displayedMovies) { movie in
                                        listRow(movie)
                                    }
                                }
                                .padding()
                            }
                        }
                    }
                }
            }
        }
        .navigationTitle("Cinétable")
        .toolbar {
            if !store.watchedMovies.isEmpty {
                ToolbarItem(placement: .topBarTrailing) {
                    Button {
                        showSearch = true
                    } label: {
                        Image(systemName: "plus.circle.fill")
                            .foregroundColor(.mooOrange)
                    }
                }
            }
        }
        .sheet(isPresented: $showSearch) {
            NavigationStack {
                MovieSearchView()
            }
        }
        .sheet(isPresented: $showFilters) {
            NavigationStack {
                CineTableFilterView(filters: $filters)
            }
        }
        .confirmationDialog(
            "Retirer ce film du Cinétable ?",
            isPresented: Binding(
                get: { movieToDelete != nil },
                set: { if !$0 { movieToDelete = nil } }
            ),
            titleVisibility: .visible
        ) {
            Button("Retirer", role: .destructive) {
                if let movie = movieToDelete {
                    store.remove(movie.id)
                }
                movieToDelete = nil
            }
            Button("Annuler", role: .cancel) {
                movieToDelete = nil
            }
        }
    }

    private var controlsBar: some View {
        HStack {
            Picker("Affichage", selection: $displayMode) {
                ForEach(CineTableDisplayMode.allCases, id: \.self) { mode in
                    Image(systemName: mode == .grid ? "square.grid.2x2" : "list.bullet")
                        .tag(mode)
                }
            }
            .pickerStyle(.segmented)
            .frame(width: 100)

            Spacer()

            Button {
                showFilters = true
            } label: {
                HStack(spacing: 6) {
                    Image(systemName: "line.3.horizontal.decrease.circle\(filters.isActive ? ".fill" : "")")
                    Text("Filtres")
                }
                .font(.system(.subheadline, design: .rounded, weight: .bold))
                .foregroundColor(filters.isActive ? .mooOrange : .mooTaupe)
            }
        }
        .padding(.horizontal)
        .padding(.top, 8)
    }

    private var emptyStateView: some View {
        VStack(spacing: 16) {
            Text("🐮")
                .font(.system(size: 56))
            Text("Ton Cinétable est vide")
                .font(.system(.title3, design: .rounded, weight: .bold))
                .foregroundColor(.mooDark)
            Text("Les films que vous regardez ensemble apparaîtront ici")
                .font(.system(.subheadline, design: .rounded))
                .foregroundColor(.mooTaupe)
                .multilineTextAlignment(.center)
                .padding(.horizontal, 40)

            Button {
                showSearch = true
            } label: {
                HStack(spacing: 6) {
                    Image(systemName: "magnifyingglass")
                    Text("Ajouter un film")
                }
                .font(.system(.subheadline, design: .rounded, weight: .bold))
                .foregroundColor(.white)
                .padding(.horizontal, 20)
                .padding(.vertical, 12)
                .background(Color.mooOrange)
                .cornerRadius(14)
            }
            .padding(.top, 8)
        }
    }

    private func gridCard(_ movie: WatchedMovie) -> some View {
        VStack(alignment: .leading, spacing: 6) {
            ZStack(alignment: .topTrailing) {
                AsyncImage(url: movie.posterURL) { phase in
                    if let image = phase.image {
                        image
                            .resizable()
                            .aspectRatio(contentMode: .fill)
                    } else {
                        Color.mooBeige
                    }
                }
                .frame(height: 180)
                .clipShape(RoundedRectangle(cornerRadius: 14))

                Button {
                    movieToDelete = movie
                } label: {
                    Image(systemName: "xmark.circle.fill")
                        .font(.system(size: 20))
                        .foregroundColor(.white)
                        .shadow(radius: 2)
                }
                .padding(6)
            }

            Text(movie.title)
                .font(.system(.caption, design: .rounded, weight: .bold))
                .foregroundColor(.mooDark)
                .lineLimit(2)

            HStack(spacing: 4) {
                Image(systemName: "star.fill")
                    .font(.caption2)
                    .foregroundColor(.mooTaupe)
                Text(String(format: "%.1f", movie.voteAverage))
                    .font(.caption2)
                    .foregroundColor(.mooTaupe)
                Spacer()
                Text(movie.watchedDate.formatted(.dateTime.day().month().year()))
                    .font(.caption2)
                    .foregroundColor(.mooTaupe)
            }

            if movie.pointsEarned > 0 {
                pointsBadge(movie.pointsEarned)
            }

            StarRatingView(rating: movie.personalRating) { newRating in
                store.setRating(movie.id, rating: newRating)
            }
        }
    }

    private func pointsBadge(_ points: Int) -> some View {
        HStack(spacing: 3) {
            Image(systemName: "seal.fill")
            Text("+\(points)")
        }
        .font(.caption2)
        .fontWeight(.bold)
        .foregroundColor(.mooOrange)
    }

    private func listRow(_ movie: WatchedMovie) -> some View {
        HStack(spacing: 12) {
            AsyncImage(url: movie.posterURL) { phase in
                if let image = phase.image {
                    image
                        .resizable()
                        .aspectRatio(contentMode: .fill)
                } else {
                    Color.mooBeige
                }
            }
            .frame(width: 56, height: 84)
            .clipShape(RoundedRectangle(cornerRadius: 10))

            VStack(alignment: .leading, spacing: 4) {
                Text(movie.title)
                    .font(.system(.subheadline, design: .rounded, weight: .bold))
                    .foregroundColor(.mooDark)
                    .lineLimit(1)

                Text(LocalizedStringKey(movie.primaryGenreName))
                    .font(.caption2)
                    .foregroundColor(.mooTaupe)

                HStack(spacing: 4) {
                    Image(systemName: "star.fill")
                        .font(.caption2)
                        .foregroundColor(.mooTaupe)
                    Text(String(format: "%.1f", movie.voteAverage))
                        .font(.caption2)
                        .foregroundColor(.mooTaupe)
                    Text("• \(movie.watchedDate.formatted(.dateTime.day().month().year()))")
                        .font(.caption2)
                        .foregroundColor(.mooTaupe)
                    if movie.pointsEarned > 0 {
                        Text("•")
                            .font(.caption2)
                            .foregroundColor(.mooTaupe)
                        pointsBadge(movie.pointsEarned)
                    }
                }

                StarRatingView(rating: movie.personalRating) { newRating in
                    store.setRating(movie.id, rating: newRating)
                }
            }

            Spacer()

            Button {
                movieToDelete = movie
            } label: {
                Image(systemName: "xmark.circle.fill")
                    .foregroundColor(.mooCoral)
            }
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
        CineTableView()
    }
}
