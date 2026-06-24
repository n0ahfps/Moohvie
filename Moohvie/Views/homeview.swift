import SwiftUI

struct HomeView: View {
    @State private var showSettings = false
    @ObservedObject private var pointsStore = MoohPointsStore.shared
    @ObservedObject private var profile = ProfileStore.shared

    var body: some View {
        NavigationStack {
            ZStack {
                Color.mooCream.ignoresSafeArea()

                VStack(spacing: 32) {
                    Spacer()

                    VStack(spacing: 8) {
                        NavigationLink {
                            ProfileView()
                        } label: {
                            if let profileImage = profile.profileImage {
                                profileImage
                                    .resizable()
                                    .scaledToFill()
                                    .frame(width: 64, height: 64)
                                    .clipShape(Circle())
                            } else {
                                Text("🐮")
                                    .font(.system(size: 64))
                            }
                        }
                        Text("MoohVie")
                            .font(.system(.largeTitle, design: .rounded, weight: .bold))
                            .foregroundColor(.mooDark)
                        Text("Trouvez le film du soir, ensemble")
                            .font(.system(.subheadline, design: .rounded, weight: .bold))
                            .foregroundColor(.mooTaupe)

                        HStack(spacing: 6) {
                            Image(systemName: "seal.fill")
                            Text("\(pointsStore.totalPoints) points")
                        }
                        .font(.system(.subheadline, design: .rounded, weight: .bold))
                        .foregroundColor(.mooOrange)
                        .padding(.horizontal, 14)
                        .padding(.vertical, 6)
                        .background(Color.mooOrange.opacity(0.15))
                        .clipShape(Capsule())
                        .padding(.top, 4)
                    }

                    Spacer()

                    VStack(spacing: 14) {
                        NavigationLink {
                            QuizView(quizLength: .short)
                        } label: {
                            modeButtonLabel(title: "Rapide", subtitle: "5 questions essentielles")
                        }

                        NavigationLink {
                            QuizView(quizLength: .medium)
                        } label: {
                            modeButtonLabel(title: "Équilibré", subtitle: "12 questions pour mieux cibler")
                        }

                        NavigationLink {
                            QuizView(quizLength: .long)
                        } label: {
                            modeButtonLabel(title: "Précis", subtitle: "20 questions pour un choix parfait")
                        }

                        NavigationLink {
                            SurpriseView()
                        } label: {
                            modeButtonLabel(title: "🎲 Surprends-moi", subtitle: "Aucune question, juste le hasard")
                        }
                    }
                    .padding(.horizontal)

                    VStack(spacing: 10) {
                        NavigationLink {
                            CineTableView()
                        } label: {
                            HStack {
                                Image(systemName: "square.grid.2x2.fill")
                                Text("Mon Cinétable")
                            }
                            .font(.system(.subheadline, design: .rounded, weight: .bold))
                            .foregroundColor(.mooOrange)
                        }

                        NavigationLink {
                            TrophiesView()
                        } label: {
                            HStack {
                                Image(systemName: "trophy.fill")
                                Text("Mes trophées")
                            }
                            .font(.system(.subheadline, design: .rounded, weight: .bold))
                            .foregroundColor(.mooOrange)
                        }
                    }
                    .padding(.top, 8)

                    Spacer()
                }
            }
            .toolbar {
                ToolbarItem(placement: .topBarLeading) {
                    NavigationLink {
                        ShopView()
                    } label: {
                        Image(systemName: "storefront.fill")
                            .foregroundColor(.mooOrange)
                    }
                }
                ToolbarItem(placement: .topBarTrailing) {
                    Button {
                        showSettings = true
                    } label: {
                        Image(systemName: "gearshape.fill")
                            .foregroundColor(.mooOrange)
                    }
                }
            }
            .sheet(isPresented: $showSettings) {
                NavigationStack {
                    SettingsView()
                }
            }
        }
    }

    private func modeButtonLabel(title: String, subtitle: String) -> some View {
        VStack(alignment: .leading, spacing: 4) {
            Text(LocalizedStringKey(title))
                .font(.system(.headline, design: .rounded, weight: .bold))
                .foregroundColor(.mooDark)
            Text(LocalizedStringKey(subtitle))
                .font(.system(.subheadline, design: .rounded))
                .foregroundColor(.mooTaupe)
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding()
        .background(Color.white)
        .cornerRadius(16)
        .overlay(
            RoundedRectangle(cornerRadius: 16)
                .stroke(Color.mooBeige, lineWidth: 1.5)
        )
    }
}

#Preview {
    HomeView()
}
