import SwiftUI

struct QuizView: View {
    @StateObject private var viewModel = QuizViewModel()
    @Environment(\.dismiss) private var dismiss
    let quizLength: QuizLength

    var body: some View {
        ZStack {
            Color.mooCream.ignoresSafeArea()

            VStack(spacing: 32) {
                if !viewModel.isFinished {
                    ProgressView(value: viewModel.progress)
                        .tint(.mooOrange)
                        .padding(.horizontal)
                        .padding(.top, 8)
                }

                Spacer()

                if viewModel.isFinished {
                    SwipeView(quizViewModel: viewModel)
                } else if !viewModel.questions.isEmpty {
                    VStack(spacing: 24) {
                        Text(viewModel.currentQuestion.text)
                            .font(.system(.title2, design: .rounded, weight: .bold))
                            .foregroundColor(.mooDark)
                            .multilineTextAlignment(.center)
                            .padding(.horizontal)

                        VStack(spacing: 10) {
                            ForEach(viewModel.currentQuestion.options) { option in
                                Button {
                                    withAnimation {
                                        viewModel.selectAnswer(option)
                                    }
                                } label: {
                                    Text(option.label)
                                        .font(.system(.headline, design: .rounded, weight: .bold))
                                        .foregroundColor(.mooDark)
                                        .frame(maxWidth: .infinity)
                                        .padding()
                                        .background(Color.mooOrange)
                                        .cornerRadius(16)
                                }
                            }
                        }
                        .padding(.horizontal)
                    }
                }

                Spacer()
            }
        }
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
                        .overlay(
                            Circle().stroke(Color.mooBeige, lineWidth: 1.5)
                        )
                }
            }
        }
        .onAppear {
            viewModel.start(length: quizLength)
        }
    }
}

#Preview {
    QuizView(quizLength: .short)
}
