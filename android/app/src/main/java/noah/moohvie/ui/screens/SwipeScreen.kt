package noah.moohvie.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import noah.moohvie.ui.theme.MooBeige
import noah.moohvie.ui.theme.MooCoral
import noah.moohvie.ui.theme.MooDark
import noah.moohvie.ui.theme.MooGreen
import noah.moohvie.ui.theme.LocalAccentColor
import noah.moohvie.ui.theme.MooTaupe
import noah.moohvie.ui.theme.tr
import noah.moohvie.viewmodels.QuizViewModel
import noah.moohvie.viewmodels.SwipeViewModel

@Composable
fun SwipeScreen(
    quizViewModel: QuizViewModel?,
    modifier: Modifier = Modifier,
    swipeViewModel: SwipeViewModel = viewModel(),
) {
    LaunchedEffect(Unit) {
        swipeViewModel.loadMovies(quizViewModel)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        when {
            swipeViewModel.isLoading -> LoadingState()
            swipeViewModel.errorMessage != null -> Text(
                tr(swipeViewModel.errorMessage.orEmpty()),
                color = MooCoral,
            )
            swipeViewModel.matchedMovie != null -> MatchView(swipeViewModel, quizViewModel)
            swipeViewModel.movies.isEmpty() -> Text(
                tr("Aucun film trouvé avec ces critères 😕"),
                color = MooDark,
            )
            swipeViewModel.isFinished -> NoMoreMoviesView(swipeViewModel, quizViewModel)
            else -> SwipeContent(swipeViewModel)
        }
    }
}

@Composable
private fun LoadingState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = LocalAccentColor.current)
    }
}

@Composable
private fun SwipeContent(viewModel: SwipeViewModel) {
    val scope = rememberCoroutineScope()
    val offsetX = remember { Animatable(0f) }

    Text(
        tr("Swipez ensemble jusqu'au bon film"),
        style = MaterialTheme.typography.titleSmall,
        color = MooDark,
    )

    val movie = viewModel.currentMovie ?: return

    Box(
        modifier = Modifier
            .graphicsLayer { translationX = offsetX.value }
            .pointerInput(movie.id) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        scope.launch {
                            when {
                                offsetX.value > 100f -> {
                                    offsetX.animateTo(1000f)
                                    viewModel.selectThisMovie()
                                    offsetX.snapTo(0f)
                                }
                                offsetX.value < -100f -> {
                                    offsetX.animateTo(-1000f)
                                    viewModel.skip()
                                    offsetX.snapTo(0f)
                                }
                                else -> offsetX.animateTo(0f)
                            }
                        }
                    },
                ) { change, dragAmount ->
                    change.consume()
                    scope.launch { offsetX.snapTo(offsetX.value + dragAmount) }
                }
            },
    ) {
        SwipeCardView(movie = movie, posterHeight = 280)
    }

    Button(
        onClick = { viewModel.pickRandom() },
        colors = ButtonDefaults.buttonColors(containerColor = LocalAccentColor.current.copy(alpha = 0.15f)),
    ) {
        Icon(Icons.Filled.Shuffle, contentDescription = null, tint = LocalAccentColor.current)
        Text(" ${tr("Tirage aléatoire")}", color = LocalAccentColor.current)
    }

    Row(horizontalArrangement = Arrangement.spacedBy(50.dp)) {
        RoundActionButton(icon = Icons.Filled.Pets, background = MooCoral, label = tr("Un autre film")) {
            viewModel.skip()
        }
        RoundActionButton(icon = Icons.Filled.NotificationsActive, background = MooGreen, label = tr("On regarde celui-là !")) {
            viewModel.selectThisMovie()
        }
    }
}

@Composable
private fun RoundActionButton(
    icon: ImageVector,
    background: Color,
    label: String,
    onClick: () -> Unit,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .size(54.dp)
                .background(background, CircleShape),
        ) {
            Icon(icon, contentDescription = label, tint = Color.White)
        }
        Text(label, style = MaterialTheme.typography.labelSmall, color = MooTaupe)
    }
}

@Composable
private fun NoMoreMoviesView(swipeViewModel: SwipeViewModel, quizViewModel: QuizViewModel?) {
    Text(tr("😅 Plus de films à proposer"), style = MaterialTheme.typography.titleMedium, color = MooDark)
    Text(
        tr("Vous avez passé tous les films disponibles. Réessayez avec d'autres critères !"),
        style = MaterialTheme.typography.bodyMedium,
        color = MooTaupe,
    )
    Button(
        onClick = {
            quizViewModel?.reset()
            swipeViewModel.reset()
        },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = LocalAccentColor.current),
    ) {
        Text(tr("Recommencer"), color = MooDark)
    }
}

@Composable
private fun MatchView(swipeViewModel: SwipeViewModel, quizViewModel: QuizViewModel?) {
    val movie = swipeViewModel.matchedMovie ?: return

    Text(tr("🎬 C'est parti !"), style = MaterialTheme.typography.titleMedium, color = MooDark)

    SwipeCardView(movie = movie, posterHeight = 170)

    swipeViewModel.matchedMovieProviders?.flatrate?.takeIf { it.isNotEmpty() }?.let { flatrate ->
        Text(tr("Disponible sur"), style = MaterialTheme.typography.labelSmall, color = MooTaupe)
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            flatrate.take(5).forEach { provider ->
                AsyncImage(
                    model = provider.logoUrl,
                    contentDescription = provider.name,
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(7.dp))
                        .background(MooBeige),
                )
            }
        }
    }

    if (swipeViewModel.addedToCineTable) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = MooGreen)
            Text(" ${tr("Ajouté à ton Cinétable !")}", color = MooGreen)
        }
    } else {
        Button(
            onClick = { swipeViewModel.confirmWatched() },
            modifier = Modifier
                .fillMaxWidth()
                .border(1.5.dp, MooBeige, RoundedCornerShape(12.dp)),
        ) {
            Text(tr("On l'a regardé, ajouter au Cinétable"), color = MooDark)
        }
    }

    Button(
        onClick = {
            quizViewModel?.reset()
            swipeViewModel.reset()
        },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = LocalAccentColor.current),
    ) {
        Text(tr("Recommencer"), color = MooDark)
    }
}
