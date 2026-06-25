@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package noah.moohvie.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import noah.moohvie.models.MoodPreset
import noah.moohvie.ui.theme.MooDark
import noah.moohvie.ui.theme.tr
import noah.moohvie.viewmodels.QuizViewModel

@Composable
fun MoodSwipeScreen(presetId: String, onBack: () -> Unit) {
    val preset = MoodPreset.find(presetId) ?: return
    val quizViewModel: QuizViewModel = viewModel()

    // Apply mood synchronously during composition so SwipeScreen's
    // LaunchedEffect reads the correct genres when loadMovies() starts.
    remember(presetId) { quizViewModel.applyMood(preset) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = tr("Retour"), tint = MooDark)
                    }
                },
            )
        },
    ) { padding ->
        SwipeScreen(
            quizViewModel = quizViewModel,
            modifier = Modifier.padding(padding),
        )
    }
}
