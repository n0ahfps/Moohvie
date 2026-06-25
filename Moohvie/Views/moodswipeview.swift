import SwiftUI

struct MoodSwipeView: View {
    let preset: MoodPreset
    @StateObject private var quizViewModel = QuizViewModel()
    @Environment(\.dismiss) private var dismiss

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
            .onAppear {
                quizViewModel.applyMood(preset)
            }
    }
}
