@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package noah.moohvie.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import noah.moohvie.models.MoodPreset
import noah.moohvie.models.QuizLength
import noah.moohvie.services.MoohPointsStore
import noah.moohvie.services.ProfileStore
import noah.moohvie.ui.theme.MooDark
import noah.moohvie.ui.theme.LocalAccentColor
import noah.moohvie.ui.theme.MooTaupe
import noah.moohvie.ui.theme.tr

@Composable
fun HomeScreen(
    onStartQuiz: (QuizLength) -> Unit,
    onSurpriseMe: () -> Unit,
    onStartMood: (String) -> Unit,
    onOpenCineTable: () -> Unit,
    onOpenShop: () -> Unit,
    onOpenProfile: () -> Unit,
    onOpenTrophies: () -> Unit,
    onOpenSettings: () -> Unit,
) {
    val context = LocalContext.current
    val pointsStore = remember { MoohPointsStore.getInstance(context) }
    val profile = remember { ProfileStore.getInstance(context) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onOpenShop) {
                        Icon(Icons.Filled.Storefront, contentDescription = tr("Boutique"), tint = LocalAccentColor.current)
                    }
                },
                actions = {
                    IconButton(onClick = onOpenSettings) {
                        Icon(Icons.Filled.Settings, contentDescription = tr("Réglages"), tint = LocalAccentColor.current)
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            val avatarPath = profile.profileImagePath
            if (avatarPath != null) {
                AsyncImage(
                    model = avatarPath,
                    contentDescription = tr("Profil"),
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .clickable(onClick = onOpenProfile),
                )
            } else {
                Text(
                    "🐮",
                    style = MaterialTheme.typography.displayMedium,
                    modifier = Modifier.clickable(onClick = onOpenProfile),
                )
            }
            Text(
                "MoohVie",
                style = MaterialTheme.typography.headlineLarge,
                color = MooDark,
            )
            Text(
                tr("Trouvez le film du soir, ensemble"),
                style = MaterialTheme.typography.bodyMedium,
                color = MooTaupe,
            )
            Text(
                "🔶 ${pointsStore.totalPoints} ${tr("points")}",
                color = LocalAccentColor.current,
            )

            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    tr("Envie rapide ?"),
                    style = MaterialTheme.typography.labelMedium,
                    color = MooTaupe,
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                ) {
                    MoodPreset.all.forEach { preset ->
                        AssistChip(
                            onClick = { onStartMood(preset.id) },
                            label = { Text("${preset.emoji} ${tr(preset.label)}") },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = androidx.compose.ui.graphics.Color.White,
                                labelColor = MooDark,
                            ),
                        )
                    }
                }
            }

            ModeButton(tr("Rapide"), tr("5 questions essentielles")) { onStartQuiz(QuizLength.SHORT) }
            ModeButton(tr("Équilibré"), tr("12 questions pour mieux cibler")) { onStartQuiz(QuizLength.MEDIUM) }
            ModeButton(tr("Précis"), tr("20 questions pour un choix parfait")) { onStartQuiz(QuizLength.LONG) }
            ModeButton(tr("🎲 Surprends-moi"), tr("Aucune question, juste le hasard"), onClick = onSurpriseMe)

            TextButton(onClick = onOpenCineTable) {
                Icon(Icons.Filled.GridView, contentDescription = null, tint = LocalAccentColor.current)
                Text(" ${tr("Mon Cinétable")}", color = LocalAccentColor.current)
            }
            TextButton(onClick = onOpenTrophies) {
                Icon(Icons.Filled.EmojiEvents, contentDescription = null, tint = LocalAccentColor.current)
                Text(" ${tr("Mes trophées")}", color = LocalAccentColor.current)
            }
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
