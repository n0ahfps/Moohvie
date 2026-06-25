import SwiftUI

struct SwipeView: View {
    var quizViewModel: QuizViewModel?
    @StateObject private var swipeViewModel = SwipeViewModel()
    @ObservedObject private var shop = ShopStore.shared
    @State private var dragOffset: CGSize = .zero

    var body: some View {
        VStack(spacing: 12) {
            Group {
                if swipeViewModel.isLoading {
                    Spacer()
                    ProgressView("Recherche de films...")
                        .tint(.mooOrange)
                    Spacer()
                } else if let error = swipeViewModel.errorMessage {
                    Spacer()
                    Text(error)
                        .foregroundColor(.mooCoral)
                        .multilineTextAlignment(.center)
                        .padding()
                    Spacer()
                } else if let matched = swipeViewModel.matchedMovie {
                    matchView(matched)
                } else if swipeViewModel.movies.isEmpty {
                    Spacer()
                    Text("Aucun film trouvé avec ces critères 😕")
                        .foregroundColor(.mooDark)
                        .multilineTextAlignment(.center)
                        .padding()
                    Spacer()
                } else if swipeViewModel.isFinished {
                    noMoreMoviesView
                } else {
                    swipeContent
                }
            }
            .padding(.horizontal)
        }
        .background(Color.mooCream.ignoresSafeArea())
        .task {
            await swipeViewModel.loadMovies(from: quizViewModel)
        }
    }

    private var swipeContent: some View {
        VStack(spacing: 12) {
            Text("Swipez ensemble jusqu'au bon film")
                .font(.system(.subheadline, design: .rounded, weight: .bold))
                .foregroundColor(.mooDark)

            if let movie = swipeViewModel.currentMovie {
                SwipeCardView(movie: movie, posterHeight: 280)
                    .offset(dragOffset)
                    .rotationEffect(.degrees(Double(dragOffset.width / 20)))
                    .gesture(
                        DragGesture()
                            .onChanged { value in
                                dragOffset = value.translation
                            }
                            .onEnded { value in
                                if value.translation.width > 100 {
                                    selectThisMovie()
                                } else if value.translation.width < -100 {
                                    skipMovie()
                                } else {
                                    withAnimation {
                                        dragOffset = .zero
                                    }
                                }
                            }
                    )
                    .animation(.spring, value: dragOffset)
            }

            Spacer(minLength: 4)

            randomPickButton

            HStack(spacing: 50) {
                Button {
                    skipMovie()
                } label: {
                    VStack(spacing: 4) {
                        Image(systemName: "pawprint.fill")
                            .font(.system(size: 22, weight: .bold))
                            .foregroundColor(.white)
                            .frame(width: 54, height: 54)
                            .background(Color.mooCoral)
                            .clipShape(Circle())
                        Text("Un autre film")
                            .font(.system(.caption2, design: .rounded, weight: .bold))
                            .foregroundColor(.mooTaupe)
                    }
                }

                Button {
                    selectThisMovie()
                } label: {
                    VStack(spacing: 4) {
                        Image(systemName: "bell.fill")
                            .font(.system(size: 22, weight: .bold))
                            .foregroundColor(.white)
                            .frame(width: 54, height: 54)
                            .background(Color.mooGreen)
                            .clipShape(Circle())
                        Text("On regarde celui-là !")
                            .font(.system(.caption2, design: .rounded, weight: .bold))
                            .foregroundColor(.mooTaupe)
                    }
                }
            }
        }
    }

    private func selectThisMovie() {
        withAnimation {
            dragOffset = CGSize(width: 500, height: 0)
        }
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.2) {
            swipeViewModel.selectThisMovie()
            dragOffset = .zero
        }
    }

    private func skipMovie() {
        withAnimation {
            dragOffset = CGSize(width: -500, height: 0)
        }
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.2) {
            swipeViewModel.skip()
            dragOffset = .zero
        }
    }

    private var randomPickButton: some View {
        Button(action: pickRandomMovie) {
            HStack(spacing: 6) {
                Image(systemName: "shuffle")
                Text("Tirage aléatoire")
            }
            .font(.system(.caption, design: .rounded, weight: .bold))
            .foregroundColor(.mooOrange)
            .padding(.horizontal, 14)
            .padding(.vertical, 8)
            .background(Color.mooOrange.opacity(0.15))
            .clipShape(Capsule())
        }
        .buttonStyle(.plain)
    }

    private func pickRandomMovie() {
        swipeViewModel.pickRandom()
    }

    private var noMoreMoviesView: some View {
        VStack(spacing: 16) {
            Spacer()
            Text("😅 Plus de films à proposer")
                .font(.system(.title3, design: .rounded, weight: .bold))
                .foregroundColor(.mooDark)
                .multilineTextAlignment(.center)
            Text("Vous avez passé tous les films disponibles. Réessayez avec d'autres critères !")
                .font(.system(.subheadline, design: .rounded))
                .foregroundColor(.mooTaupe)
                .multilineTextAlignment(.center)
                .padding(.horizontal)
            Spacer()
            Button("Recommencer") {
                quizViewModel?.reset()
                swipeViewModel.reset()
            }
            .font(.system(.headline, design: .rounded, weight: .bold))
            .padding()
            .frame(maxWidth: .infinity)
            .background(Color.mooOrange)
            .foregroundColor(.mooDark)
            .cornerRadius(16)
        }
    }

    private func matchView(_ movie: Movie) -> some View {
        VStack(spacing: 8) {
            Text("🎬 C'est parti !")
                .font(.system(.title3, design: .rounded, weight: .bold))
                .foregroundColor(.mooDark)

            SwipeCardView(movie: movie, posterHeight: 170)

            if let providers = swipeViewModel.matchedMovieProviders,
               let flatrate = providers.flatrate, !flatrate.isEmpty {
                VStack(spacing: 4) {
                    Text("Disponible sur")
                        .font(.system(.caption2, design: .rounded, weight: .bold))
                        .foregroundColor(.mooTaupe)

                    HStack(spacing: 6) {
                        ForEach(flatrate.prefix(5)) { provider in
                            AsyncImage(url: provider.logoURL) { phase in
                                if let image = phase.image {
                                    image
                                        .resizable()
                                        .scaledToFit()
                                } else {
                                    Color.mooBeige
                                }
                            }
                            .frame(width: 28, height: 28)
                            .clipShape(RoundedRectangle(cornerRadius: 7))
                        }
                    }
                }
            }

            Spacer(minLength: 4)

            if swipeViewModel.addedToCineTable {
                HStack {
                    Image(systemName: "checkmark.circle.fill")
                        .foregroundColor(.mooGreen)
                    Text("Ajouté à ton Cinétable !")
                        .font(.system(.caption, design: .rounded, weight: .bold))
                        .foregroundColor(.mooGreen)
                }
            } else {
                Button("On l'a regardé, ajouter au Cinétable") {
                    swipeViewModel.confirmWatched()
                }
                .font(.system(.caption, design: .rounded, weight: .bold))
                .padding(8)
                .frame(maxWidth: .infinity)
                .background(Color.white)
                .foregroundColor(.mooDark)
                .cornerRadius(12)
                .overlay(
                    RoundedRectangle(cornerRadius: 12)
                        .stroke(Color.mooBeige, lineWidth: 1.5)
                )
            }

            Button("Recommencer") {
                quizViewModel?.reset()
                swipeViewModel.reset()
            }
            .font(.system(.caption, design: .rounded, weight: .bold))
            .padding(8)
            .frame(maxWidth: .infinity)
            .background(Color.mooOrange)
            .foregroundColor(.mooDark)
            .cornerRadius(12)
        }
    }
}
