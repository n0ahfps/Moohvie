import SwiftUI

struct CineTableFilterView: View {
    @Binding var filters: CineTableFilterState
    @Environment(\.dismiss) private var dismiss

    private var availableGenres: [Int] {
        Array(GenreLookup.names.keys).sorted { GenreLookup.name(for: $0) < GenreLookup.name(for: $1) }
    }

    var body: some View {
        ZStack {
            Color.mooCream.ignoresSafeArea()

            ScrollView {
                VStack(alignment: .leading, spacing: 24) {
                    VStack(alignment: .leading, spacing: 10) {
                        Text("Trier par")
                            .font(.system(.headline, design: .rounded, weight: .bold))
                            .foregroundColor(.mooDark)

                        VStack(spacing: 8) {
                            ForEach(CineTableSortOption.allCases, id: \.self) { option in
                                optionRow(label: option.rawValue, isSelected: filters.sortOption == option) {
                                    filters.sortOption = option
                                }
                            }
                        }
                    }

                    VStack(alignment: .leading, spacing: 10) {
                        Text("Genre")
                            .font(.system(.headline, design: .rounded, weight: .bold))
                            .foregroundColor(.mooDark)

                        VStack(spacing: 8) {
                            optionRow(label: "Tous les genres", isSelected: filters.selectedGenreID == nil) {
                                filters.selectedGenreID = nil
                            }
                            ForEach(availableGenres, id: \.self) { genreID in
                                optionRow(label: GenreLookup.name(for: genreID), isSelected: filters.selectedGenreID == genreID) {
                                    filters.selectedGenreID = genreID
                                }
                            }
                        }
                    }

                    VStack(alignment: .leading, spacing: 10) {
                        Text("Ma note minimum")
                            .font(.system(.headline, design: .rounded, weight: .bold))
                            .foregroundColor(.mooDark)

                        HStack(spacing: 8) {
                            ForEach(0...5, id: \.self) { value in
                                Button {
                                    filters.minPersonalRating = value
                                } label: {
                                    Text(value == 0 ? LocalizedStringKey("Toutes") : LocalizedStringKey("\(value)+ ⭐"))
                                        .font(.system(.caption, design: .rounded, weight: .bold))
                                        .padding(.horizontal, 12)
                                        .padding(.vertical, 8)
                                        .background(filters.minPersonalRating == value ? Color.mooOrange : Color.white)
                                        .foregroundColor(filters.minPersonalRating == value ? .mooDark : .mooTaupe)
                                        .cornerRadius(10)
                                        .overlay(
                                            RoundedRectangle(cornerRadius: 10)
                                                .stroke(Color.mooBeige, lineWidth: 1.5)
                                        )
                                }
                            }
                        }
                    }

                    VStack(alignment: .leading, spacing: 10) {
                        Text("Note TMDB minimum")
                            .font(.system(.headline, design: .rounded, weight: .bold))
                            .foregroundColor(.mooDark)

                        HStack(spacing: 8) {
                            ForEach([0.0, 5.0, 6.0, 7.0, 8.0], id: \.self) { value in
                                Button {
                                    filters.minTMDBRating = value
                                } label: {
                                    Text(value == 0 ? LocalizedStringKey("Toutes") : LocalizedStringKey("\(Int(value))+"))
                                        .font(.system(.caption, design: .rounded, weight: .bold))
                                        .padding(.horizontal, 12)
                                        .padding(.vertical, 8)
                                        .background(filters.minTMDBRating == value ? Color.mooOrange : Color.white)
                                        .foregroundColor(filters.minTMDBRating == value ? .mooDark : .mooTaupe)
                                        .cornerRadius(10)
                                        .overlay(
                                            RoundedRectangle(cornerRadius: 10)
                                                .stroke(Color.mooBeige, lineWidth: 1.5)
                                        )
                                }
                            }
                        }
                    }

                    Button("Réinitialiser les filtres") {
                        filters = CineTableFilterState()
                    }
                    .font(.system(.subheadline, design: .rounded, weight: .bold))
                    .foregroundColor(.mooCoral)
                    .padding(.top, 8)
                }
                .padding()
            }
        }
        .navigationTitle("Trier et filtrer")
        .navigationBarTitleDisplayMode(.inline)
        .toolbar {
            ToolbarItem(placement: .topBarTrailing) {
                Button("OK") { dismiss() }
                    .foregroundColor(.mooOrange)
                    .bold()
            }
        }
    }

    private func optionRow(label: String, isSelected: Bool, action: @escaping () -> Void) -> some View {
        Button(action: action) {
            HStack {
                Text(LocalizedStringKey(label))
                    .font(.system(.body, design: .rounded, weight: .bold))
                    .foregroundColor(.mooDark)
                Spacer()
                Image(systemName: isSelected ? "checkmark.circle.fill" : "circle")
                    .foregroundColor(isSelected ? .mooGreen : .mooTaupe)
            }
            .padding(12)
            .background(Color.white)
            .cornerRadius(12)
            .overlay(
                RoundedRectangle(cornerRadius: 12)
                    .stroke(Color.mooBeige, lineWidth: 1.5)
            )
        }
    }
}

#Preview {
    NavigationStack {
        CineTableFilterView(filters: .constant(CineTableFilterState()))
    }
}
