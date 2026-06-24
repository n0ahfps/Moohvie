import SwiftUI

struct SettingsView: View {
    @ObservedObject var settings = AppSettings.shared
    @Environment(\.dismiss) private var dismiss

    var body: some View {
        ZStack {
            Color.mooCream.ignoresSafeArea()

            ScrollView {
                VStack(alignment: .leading, spacing: 28) {
                    Text("Paramètres")
                        .font(.system(.largeTitle, design: .rounded, weight: .bold))
                        .foregroundColor(.mooDark)
                        .padding(.top, 8)

                    VStack(alignment: .leading, spacing: 12) {
                        Text("Mes plateformes de streaming")
                            .font(.system(.headline, design: .rounded, weight: .bold))
                            .foregroundColor(.mooDark)
                        Text("Sélectionne tes abonnements pour ne voir que des films que tu peux regarder direct. Laisse tout décoché pour ne pas filtrer.")
                            .font(.system(.caption, design: .rounded))
                            .foregroundColor(.mooTaupe)

                        VStack(spacing: 8) {
                            ForEach(StreamingProviderOption.all) { provider in
                                providerRow(provider)
                            }
                        }
                    }

                    VStack(alignment: .leading, spacing: 12) {
                        Text("Contenu maximum")
                            .font(.system(.headline, design: .rounded, weight: .bold))
                            .foregroundColor(.mooDark)
                        Text("Exclut les films classés au-dessus de ce seuil (violence, sexe, etc.)")
                            .font(.system(.caption, design: .rounded))
                            .foregroundColor(.mooTaupe)

                        VStack(spacing: 8) {
                            ForEach(FrenchCertification.allCases, id: \.self) { cert in
                                certificationRow(cert)
                            }
                        }
                    }

                    VStack(alignment: .leading, spacing: 12) {
                        Text("Langue")
                            .font(.system(.headline, design: .rounded, weight: .bold))
                            .foregroundColor(.mooDark)

                        VStack(spacing: 8) {
                            ForEach(AppLanguage.allCases, id: \.self) { language in
                                languageRow(language)
                            }
                        }
                    }

                    VStack(alignment: .leading, spacing: 12) {
                        Text("Cinétable")
                            .font(.system(.headline, design: .rounded, weight: .bold))
                            .foregroundColor(.mooDark)

                        Toggle(isOn: $settings.allowRewatching) {
                            Text("Revoir les films déjà vus")
                                .font(.system(.body, design: .rounded, weight: .bold))
                                .foregroundColor(.mooDark)
                        }
                        .tint(.mooGreen)
                        .padding()
                        .background(Color.white)
                        .cornerRadius(12)
                        .overlay(
                            RoundedRectangle(cornerRadius: 12)
                                .stroke(Color.mooBeige, lineWidth: 1.5)
                        )

                        Text("Si désactivé, les films de ton Cinétable ne seront plus proposés.")
                            .font(.system(.caption, design: .rounded))
                            .foregroundColor(.mooTaupe)
                    }

                    Spacer(minLength: 20)
                }
                .padding()
            }
        }
        .navigationTitle("")
        .navigationBarTitleDisplayMode(.inline)
        .toolbar {
            ToolbarItem(placement: .topBarTrailing) {
                Button("Fermer") { dismiss() }
                    .foregroundColor(.mooOrange)
            }
        }
    }

    private func providerRow(_ provider: StreamingProviderOption) -> some View {
        let isSelected = settings.selectedProviderIDs.contains(provider.id)
        return Button {
            if isSelected {
                settings.selectedProviderIDs.remove(provider.id)
            } else {
                settings.selectedProviderIDs.insert(provider.id)
            }
        } label: {
            HStack {
                Text(provider.name)
                    .font(.system(.body, design: .rounded, weight: .bold))
                    .foregroundColor(.mooDark)
                Spacer()
                Image(systemName: isSelected ? "checkmark.circle.fill" : "circle")
                    .foregroundColor(isSelected ? .mooGreen : .mooTaupe)
            }
            .padding()
            .background(Color.white)
            .cornerRadius(12)
            .overlay(
                RoundedRectangle(cornerRadius: 12)
                    .stroke(Color.mooBeige, lineWidth: 1.5)
            )
        }
    }

    private func languageRow(_ language: AppLanguage) -> some View {
        let isSelected = settings.appLanguage == language
        return Button {
            settings.appLanguage = language
        } label: {
            HStack {
                Text(LocalizedStringKey(language.label))
                    .font(.system(.body, design: .rounded, weight: .bold))
                    .foregroundColor(.mooDark)
                Spacer()
                Image(systemName: isSelected ? "checkmark.circle.fill" : "circle")
                    .foregroundColor(isSelected ? .mooGreen : .mooTaupe)
            }
            .padding()
            .background(Color.white)
            .cornerRadius(12)
            .overlay(
                RoundedRectangle(cornerRadius: 12)
                    .stroke(Color.mooBeige, lineWidth: 1.5)
            )
        }
    }

    private func certificationRow(_ cert: FrenchCertification) -> some View {
        let isSelected = settings.maxCertification == cert
        return Button {
            settings.maxCertification = cert
        } label: {
            HStack {
                Text(LocalizedStringKey(cert.rawValue))
                    .font(.system(.body, design: .rounded, weight: .bold))
                    .foregroundColor(.mooDark)
                Spacer()
                Image(systemName: isSelected ? "checkmark.circle.fill" : "circle")
                    .foregroundColor(isSelected ? .mooGreen : .mooTaupe)
            }
            .padding()
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
    SettingsView()
}
