import SwiftUI

struct ContentView: View {
    @ObservedObject private var settings = AppSettings.shared

    var body: some View {
        HomeView()
            .environment(\.locale, settings.appLanguage.locale)
    }
}

#Preview {
    ContentView()
}
