package noah.moohvie

import android.os.Bundle
import android.os.SystemClock
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import noah.moohvie.models.QuizLength
import noah.moohvie.ui.screens.CineTableScreen
import noah.moohvie.ui.screens.HomeScreen
import noah.moohvie.ui.screens.ProfileScreen
import noah.moohvie.ui.screens.QuizScreen
import noah.moohvie.ui.screens.SettingsScreen
import noah.moohvie.ui.screens.ShopScreen
import noah.moohvie.ui.screens.SurpriseScreen
import noah.moohvie.ui.screens.TrophiesScreen
import noah.moohvie.ui.screens.TrophyDetailScreen
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
    const val CINETABLE = "cinetable"
    const val SHOP = "shop"
    const val PROFILE = "profile"
    const val TROPHIES = "trophies"
    const val TROPHY_DETAIL = "trophies/{trophyId}"
    const val SETTINGS = "settings"

    fun quiz(quizLength: QuizLength) = "quiz/${quizLength.name}"
    fun trophyDetail(trophyId: String) = "trophies/$trophyId"
}

/**
 * Back and forward nav buttons often sit at the same screen position across consecutive
 * screens (e.g. a top-left back arrow lands where the next screen's icon button is). A fast
 * double tap there fires a second navigate()/popBackStack() while the first is still settling;
 * Navigation Compose then silently drops it instead of crashing, leaving the UI stuck on a
 * half-transitioned, blank frame. Debouncing every nav action on a single shared timestamp
 * closes that window regardless of which screen either tap nominally lands on.
 */
private class NavigationDebouncer(private val minIntervalMs: Long = 500L) {
    private var lastNavTime = 0L

    fun run(action: () -> Unit) {
        val now = SystemClock.elapsedRealtime()
        if (now - lastNavTime >= minIntervalMs) {
            lastNavTime = now
            action()
        }
    }
}

@androidx.compose.runtime.Composable
private fun MoohvieNavHost() {
    val navController = rememberNavController()
    val debouncer = remember { NavigationDebouncer() }

    fun navigate(route: String) = debouncer.run { navController.navigate(route) }
    fun back() = debouncer.run { navController.popBackStack() }

    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            HomeScreen(
                onStartQuiz = { quizLength -> navigate(Routes.quiz(quizLength)) },
                onSurpriseMe = { navigate(Routes.SURPRISE) },
                onOpenCineTable = { navigate(Routes.CINETABLE) },
                onOpenShop = { navigate(Routes.SHOP) },
                onOpenProfile = { navigate(Routes.PROFILE) },
                onOpenTrophies = { navigate(Routes.TROPHIES) },
                onOpenSettings = { navigate(Routes.SETTINGS) },
            )
        }
        composable(Routes.QUIZ) { backStackEntry ->
            val quizLength = QuizLength.valueOf(
                backStackEntry.arguments?.getString("quizLength") ?: QuizLength.SHORT.name
            )
            QuizScreen(quizLength = quizLength, onBack = { back() })
        }
        composable(Routes.SURPRISE) {
            SurpriseScreen(onBack = { back() })
        }
        composable(Routes.CINETABLE) {
            CineTableScreen(onBack = { back() })
        }
        composable(Routes.SHOP) {
            ShopScreen(onBack = { back() })
        }
        composable(Routes.PROFILE) {
            ProfileScreen(onBack = { back() })
        }
        composable(Routes.TROPHIES) {
            TrophiesScreen(
                onBack = { back() },
                onOpenTrophy = { trophy -> navigate(Routes.trophyDetail(trophy.id)) },
            )
        }
        composable(Routes.TROPHY_DETAIL) { backStackEntry ->
            val trophyId = backStackEntry.arguments?.getString("trophyId") ?: return@composable
            TrophyDetailScreen(trophyId = trophyId, onBack = { back() })
        }
        composable(Routes.SETTINGS) {
            SettingsScreen(onClose = { back() })
        }
    }
}
