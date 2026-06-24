@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package noah.moohvie.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import noah.moohvie.models.QuizLength
import noah.moohvie.ui.theme.MooDark
import noah.moohvie.ui.theme.MooOrangeDefault
import noah.moohvie.viewmodels.QuizViewModel

@Composable
fun QuizScreen(
    quizLength: QuizLength,
    onBack: () -> Unit,
    viewModel: QuizViewModel = viewModel(),
) {
    LaunchedEffect(quizLength) {
        viewModel.start(quizLength)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour", tint = MooDark)
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            if (viewModel.questions.isEmpty()) return@Column

            if (!viewModel.isFinished) {
                LinearProgressIndicator(
                    progress = { viewModel.progress },
                    modifier = Modifier.fillMaxWidth(),
                    color = MooOrangeDefault,
                )

                Text(
                    viewModel.currentQuestion.text,
                    style = MaterialTheme.typography.titleLarge,
                    color = MooDark,
                )

                viewModel.currentQuestion.options.forEach { option ->
                    Button(
                        onClick = { viewModel.selectAnswer(option) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MooOrangeDefault),
                    ) {
                        Text(option.label, color = MooDark)
                    }
                }
            } else {
                SwipeScreen(quizViewModel = viewModel)
            }
        }
    }
}
