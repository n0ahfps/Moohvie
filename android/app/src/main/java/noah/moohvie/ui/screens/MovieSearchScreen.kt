@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package noah.moohvie.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import noah.moohvie.models.Movie
import noah.moohvie.ui.theme.MooBeige
import noah.moohvie.ui.theme.MooDark
import noah.moohvie.ui.theme.MooGreen
import noah.moohvie.ui.theme.MooOrangeDefault
import noah.moohvie.ui.theme.MooTaupe
import noah.moohvie.viewmodels.MovieSearchViewModel

@Composable
fun MovieSearchScreen(onClose: () -> Unit, viewModel: MovieSearchViewModel = viewModel()) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ajouter un film") },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Fermer", tint = MooDark)
                    }
                },
            )
        },
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            OutlinedTextField(
                value = viewModel.query,
                onValueChange = viewModel::onQueryChange,
                placeholder = { Text("Titre du film...") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )

            when {
                viewModel.isSearching -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = MooOrangeDefault)
                }
                viewModel.results.isEmpty() && viewModel.query.isNotEmpty() -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("Aucun film trouvé", color = MooTaupe)
                }
                else -> LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(top = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    items(viewModel.results) { movie ->
                        SearchResultRow(
                            movie = movie,
                            alreadyAdded = viewModel.isAlreadyAdded(movie.id),
                            onAdd = { viewModel.addToCineTable(movie) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchResultRow(movie: Movie, alreadyAdded: Boolean, onAdd: () -> Unit) {
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
                .size(width = 50.dp, height = 75.dp)
                .clip(RoundedCornerShape(8.dp)),
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(movie.title, style = MaterialTheme.typography.bodyMedium, color = MooDark, maxLines = 2)
            movie.releaseDate?.take(4)?.let {
                Text(it, style = MaterialTheme.typography.bodySmall, color = MooTaupe)
            }
        }

        IconButton(onClick = onAdd, enabled = !alreadyAdded) {
            Icon(
                imageVector = if (alreadyAdded) Icons.Filled.CheckCircle else Icons.Filled.AddCircle,
                contentDescription = if (alreadyAdded) "Déjà ajouté" else "Ajouter",
                tint = if (alreadyAdded) MooGreen else MooOrangeDefault,
            )
        }
    }
}
