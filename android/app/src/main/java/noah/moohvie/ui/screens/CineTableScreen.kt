@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package noah.moohvie.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import java.text.SimpleDateFormat
import java.util.Locale
import noah.moohvie.models.CineTableDisplayMode
import noah.moohvie.models.WatchedMovie
import noah.moohvie.ui.theme.MooBeige
import noah.moohvie.ui.theme.MooDark
import noah.moohvie.ui.theme.MooOrangeDefault
import noah.moohvie.ui.theme.MooTaupe
import noah.moohvie.viewmodels.CineTableViewModel

private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

@Composable
fun CineTableScreen(
    onBack: () -> Unit,
    viewModel: CineTableViewModel = viewModel(),
) {
    var showSearch by remember { mutableStateOf(false) }
    var showFilters by remember { mutableStateOf(false) }
    var movieToDelete by remember { mutableStateOf<WatchedMovie?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cinétable") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Retour", tint = MooDark)
                    }
                },
                actions = {
                    if (viewModel.store.watchedMovies.isNotEmpty()) {
                        IconButton(onClick = { showSearch = true }) {
                            Icon(Icons.Filled.Add, contentDescription = "Ajouter un film", tint = MooOrangeDefault)
                        }
                    }
                },
            )
        },
    ) { padding ->
        if (viewModel.store.watchedMovies.isEmpty()) {
            EmptyState(modifier = Modifier.padding(padding)) { showSearch = true }
        } else {
            Column(modifier = Modifier.fillMaxSize().padding(padding)) {
                ControlsBar(
                    displayMode = viewModel.displayMode,
                    onDisplayModeChange = { viewModel.displayMode = it },
                    filtersActive = viewModel.filters.isActive,
                    onFiltersClick = { showFilters = true },
                )

                val displayedMovies = viewModel.displayedMovies
                if (displayedMovies.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Aucun film ne correspond à ces filtres", color = MooTaupe)
                    }
                } else if (viewModel.displayMode == CineTableDisplayMode.GRID) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(displayedMovies) { movie ->
                            GridCard(movie, onDelete = { movieToDelete = movie }, onRate = { viewModel.setRating(movie.id, it) })
                        }
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        items(displayedMovies) { movie ->
                            ListRow(movie, onDelete = { movieToDelete = movie }, onRate = { viewModel.setRating(movie.id, it) })
                        }
                    }
                }
            }
        }
    }

    if (showSearch) {
        ModalBottomSheet(onDismissRequest = { showSearch = false }) {
            MovieSearchScreen(onClose = { showSearch = false })
        }
    }

    if (showFilters) {
        ModalBottomSheet(onDismissRequest = { showFilters = false }) {
            CineTableFilterScreen(
                filters = viewModel.filters,
                onFiltersChange = { viewModel.filters = it },
                onDone = { showFilters = false },
            )
        }
    }

    movieToDelete?.let { movie ->
        AlertDialog(
            onDismissRequest = { movieToDelete = null },
            title = { Text("Retirer ce film du Cinétable ?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.remove(movie.id)
                    movieToDelete = null
                }) {
                    Text("Retirer")
                }
            },
            dismissButton = {
                TextButton(onClick = { movieToDelete = null }) {
                    Text("Annuler")
                }
            },
        )
    }
}

@Composable
private fun ControlsBar(
    displayMode: CineTableDisplayMode,
    onDisplayModeChange: (CineTableDisplayMode) -> Unit,
    filtersActive: Boolean,
    onFiltersClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row {
            IconButton(onClick = { onDisplayModeChange(CineTableDisplayMode.GRID) }) {
                Icon(
                    Icons.Filled.GridView,
                    contentDescription = "Grille",
                    tint = if (displayMode == CineTableDisplayMode.GRID) MooOrangeDefault else MooTaupe,
                )
            }
            IconButton(onClick = { onDisplayModeChange(CineTableDisplayMode.LIST) }) {
                Icon(
                    Icons.Filled.ViewList,
                    contentDescription = "Liste",
                    tint = if (displayMode == CineTableDisplayMode.LIST) MooOrangeDefault else MooTaupe,
                )
            }
        }

        Button(
            onClick = onFiltersClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = androidx.compose.ui.graphics.Color.Transparent,
            ),
        ) {
            Icon(Icons.Filled.FilterAlt, contentDescription = null, tint = if (filtersActive) MooOrangeDefault else MooTaupe)
            Text(" Filtres", color = if (filtersActive) MooOrangeDefault else MooTaupe)
        }
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier, onAddMovie: () -> Unit) {
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text("🐮", style = MaterialTheme.typography.displaySmall)
        Text("Ton Cinétable est vide", style = MaterialTheme.typography.titleLarge, color = MooDark)
        Text(
            "Les films que vous regardez ensemble apparaîtront ici",
            style = MaterialTheme.typography.bodyMedium,
            color = MooTaupe,
        )
        Button(
            onClick = onAddMovie,
            colors = ButtonDefaults.buttonColors(containerColor = MooOrangeDefault),
        ) {
            Icon(Icons.Filled.Search, contentDescription = null, tint = androidx.compose.ui.graphics.Color.White)
            Text(" Ajouter un film", color = androidx.compose.ui.graphics.Color.White)
        }
    }
}

@Composable
private fun PosterThumbnail(movie: WatchedMovie, height: Int) {
    AsyncImage(
        model = movie.posterUrl,
        contentDescription = movie.title,
        modifier = Modifier
            .fillMaxWidth()
            .height(height.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(MooBeige),
    )
}

@Composable
private fun GridCard(movie: WatchedMovie, onDelete: () -> Unit, onRate: (Int) -> Unit) {
    Column {
        Box {
            PosterThumbnail(movie, height = 180)
            IconButton(onClick = onDelete, modifier = Modifier.align(Alignment.TopEnd)) {
                Icon(Icons.Filled.Cancel, contentDescription = "Retirer", tint = androidx.compose.ui.graphics.Color.White)
            }
        }
        Text(movie.title, style = MaterialTheme.typography.labelMedium, color = MooDark, maxLines = 2)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.Star, contentDescription = null, tint = MooTaupe, modifier = Modifier.size(14.dp))
            Text(String.format("%.1f", movie.voteAverage), style = MaterialTheme.typography.labelSmall, color = MooTaupe)
            Text(
                " ${dateFormat.format(java.util.Date(movie.watchedDate))}",
                style = MaterialTheme.typography.labelSmall,
                color = MooTaupe,
            )
        }
        if (movie.pointsEarned > 0) {
            Text("+${movie.pointsEarned} pts", style = MaterialTheme.typography.labelSmall, color = MooOrangeDefault)
        }
        StarRatingView(rating = movie.personalRating, onRate = onRate)
    }
}

@Composable
private fun ListRow(movie: WatchedMovie, onDelete: () -> Unit, onRate: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        AsyncImage(
            model = movie.posterUrl,
            contentDescription = movie.title,
            modifier = Modifier
                .size(width = 56.dp, height = 84.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MooBeige),
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(movie.title, style = MaterialTheme.typography.bodyMedium, color = MooDark, maxLines = 1)
            Text(movie.primaryGenreName, style = MaterialTheme.typography.labelSmall, color = MooTaupe)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Star, contentDescription = null, tint = MooTaupe, modifier = Modifier.size(14.dp))
                Text(String.format("%.1f", movie.voteAverage), style = MaterialTheme.typography.labelSmall, color = MooTaupe)
                Text(
                    " • ${dateFormat.format(java.util.Date(movie.watchedDate))}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MooTaupe,
                )
                if (movie.pointsEarned > 0) {
                    Text(" • +${movie.pointsEarned} pts", style = MaterialTheme.typography.labelSmall, color = MooOrangeDefault)
                }
            }
            StarRatingView(rating = movie.personalRating, onRate = onRate)
        }

        IconButton(onClick = onDelete) {
            Icon(Icons.Filled.Cancel, contentDescription = "Retirer", tint = MooOrangeDefault)
        }
    }
}
