import SwiftUI

struct MoodSwipeView: View {
    let preset: MoodPreset
    @StateObject private var quizViewModel: QuizViewModel
    @Environment(\.dismiss) private var dismiss

    init(preset: MoodPreset) {
        self.preset = preset
        // Apply the mood before SwipeView is created so loadMovies()
        // reads the correct genres when its .task fires on appear.
        let vm = QuizViewModel()
        vm.applyMood(preset)
        _quizViewModel = StateObject(wrappedValue: vm)
    }

    var body: some View {
        SwipeView(quizViewModel: quizViewModel)
            .navigationBarBackButtonHidden(true)
            .toolbar {
                ToolbarItem(placement: .topBarLeading) {
                    Button {
                        dismiss()
                    } label: {
                        Image(systemName: "chevron.left")
                            .font(.system(size: 18, weight: .bold))
                            .foregroundColor(.mooDark)
                            .frame(width: 36, height: 36)
                            .background(Color.white)
                            .clipShape(Circle())
                            .overlay(Circle().stroke(Color.mooBeige, lineWidth: 1.5))
                    }
                }
            }
    }
}
