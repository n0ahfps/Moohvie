package noah.moohvie.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import noah.moohvie.models.QuizLength
import noah.moohvie.services.MoohPointsStore
import noah.moohvie.ui.theme.MooDark
import noah.moohvie.ui.theme.MooOrangeDefault
import noah.moohvie.ui.theme.MooTaupe

@Composable
fun HomeScreen(onStartQuiz: (QuizLength) -> Unit, onSurpriseMe: () -> Unit) {
    val pointsStore = MoohPointsStore.getInstance(LocalContext.current)

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            Text("🐮", style = MaterialTheme.typography.displayMedium)
            Text(
                "MoohVie",
                style = MaterialTheme.typography.headlineLarge,
                color = MooDark,
            )
            Text(
                "Trouvez le film du soir, ensemble",
                style = MaterialTheme.typography.bodyMedium,
                color = MooTaupe,
            )
            Text(
                "🔶 ${pointsStore.totalPoints} points",
                color = MooOrangeDefault,
            )

            ModeButton("Rapide", "5 questions essentielles") { onStartQuiz(QuizLength.SHORT) }
            ModeButton("Équilibré", "12 questions pour mieux cibler") { onStartQuiz(QuizLength.MEDIUM) }
            ModeButton("Précis", "20 questions pour un choix parfait") { onStartQuiz(QuizLength.LONG) }
            ModeButton("🎲 Surprends-moi", "Aucune question, juste le hasard", onClick = onSurpriseMe)
        }
    }
}

@Composable
private fun ModeButton(title: String, subtitle: String, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(),
    ) {
        Column(modifier = Modifier.padding(PaddingValues(16.dp))) {
            Text(title, style = MaterialTheme.typography.titleMedium, color = MooDark)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MooTaupe)
        }
    }
}
