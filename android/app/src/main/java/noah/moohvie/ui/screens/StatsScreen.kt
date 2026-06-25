@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package noah.moohvie.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import noah.moohvie.models.WatchedMovie
import noah.moohvie.ui.theme.MooBeige
import noah.moohvie.ui.theme.MooDark
import noah.moohvie.ui.theme.LocalAccentColor
import noah.moohvie.ui.theme.MooTaupe
import noah.moohvie.ui.theme.tr
import noah.moohvie.viewmodels.CineTableViewModel

@Composable
fun StatsScreen(onBack: () -> Unit, viewModel: CineTableViewModel = viewModel()) {
    val stats = viewModel.stats

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(tr("Nos stats")) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = tr("Retour"), tint = MooDark)
                    }
                },
            )
        },
    ) { padding ->
        if (stats.totalMovies == 0) {
            Column(
                modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(tr("Ajoutez des films au Cinétable pour voir vos stats"), color = MooTaupe)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                StatCard(emoji = "🎬", label = tr("Films vus ensemble"), value = "${stats.totalMovies}")

                stats.favoriteGenreName?.let {
                    StatCard(emoji = "🎭", label = tr("Genre favori"), value = tr(it))
                }

                stats.averagePersonalRating?.let {
                    StatCard(emoji = "⭐", label = tr("Note moyenne"), value = String.format("%.1f / 5", it))
                }

                stats.moviesPerMonth?.let {
                    StatCard(emoji = "📅", label = tr("Films par mois"), value = String.format("%.1f", it))
                }

                stats.topRatedMovie?.let {
                    TopMovieCard(movie = it)
                }
            }
        }
    }
}

@Composable
private fun StatCard(emoji: String, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Text(emoji, style = MaterialTheme.typography.titleLarge)
        Column {
            Text(label, style = MaterialTheme.typography.labelSmall, color = MooTaupe)
            Text(value, style = MaterialTheme.typography.titleMedium, color = MooDark, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun TopMovieCard(movie: WatchedMovie) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = movie.posterUrl,
            contentDescription = movie.title,
            modifier = Modifier
                .width(56.dp)
                .height(84.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MooBeige),
        )
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("🏆 ${tr("Film préféré")}", style = MaterialTheme.typography.labelSmall, color = MooTaupe)
            Text(movie.title, style = MaterialTheme.typography.bodyMedium, color = MooDark, fontWeight = FontWeight.Bold, maxLines = 2)
            Row {
                (1..5).forEach { i ->
                    Icon(
                        imageVector = if (i <= movie.personalRating) Icons.Filled.Star else Icons.Filled.StarBorder,
                        contentDescription = null,
                        tint = LocalAccentColor.current,
                        modifier = Modifier.size(14.dp),
                    )
                }
            }
        }
    }
}
