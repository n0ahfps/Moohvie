package noah.moohvie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import noah.moohvie.models.QuizLength
import noah.moohvie.ui.screens.HomeScreen
import noah.moohvie.ui.screens.QuizScreen
import noah.moohvie.ui.screens.SurpriseScreen
import noah.moohvie.ui.theme.MoohvieTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoohvieTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MoohvieNavHost()
                }
            }
        }
    }
}

private object Routes {
    const val HOME = "home"
    const val QUIZ = "quiz/{quizLength}"
    const val SURPRISE = "surprise"

    fun quiz(quizLength: QuizLength) = "quiz/${quizLength.name}"
}

@androidx.compose.runtime.Composable
private fun MoohvieNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            HomeScreen(
                onStartQuiz = { quizLength ->
                    navController.navigate(Routes.quiz(quizLength))
                },
                onSurpriseMe = { navController.navigate(Routes.SURPRISE) },
            )
        }
        composable(Routes.QUIZ) { backStackEntry ->
            val quizLength = QuizLength.valueOf(
                backStackEntry.arguments?.getString("quizLength") ?: QuizLength.SHORT.name
            )
            QuizScreen(quizLength = quizLength, onBack = { navController.popBackStack() })
        }
        composable(Routes.SURPRISE) {
            SurpriseScreen(onBack = { navController.popBackStack() })
        }
    }
}
