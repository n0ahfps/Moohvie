package noah.moohvie.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import noah.moohvie.models.CineTableFilterState
import noah.moohvie.models.CineTableSortOption
import noah.moohvie.models.GenreLookup
import noah.moohvie.ui.theme.MooBeige
import noah.moohvie.ui.theme.MooCoral
import noah.moohvie.ui.theme.MooDark
import noah.moohvie.ui.theme.MooGreen
import noah.moohvie.ui.theme.LocalAccentColor
import noah.moohvie.ui.theme.MooTaupe
import noah.moohvie.ui.theme.tr

@Composable
fun CineTableFilterScreen(
    filters: CineTableFilterState,
    onFiltersChange: (CineTableFilterState) -> Unit,
    onDone: () -> Unit,
) {
    val availableGenres = GenreLookup.names.entries.sortedBy { it.value }

    LazyColumn(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        item {
            Text(tr("Trier par"), style = MaterialTheme.typography.titleMedium, color = MooDark)
        }
        items(CineTableSortOption.entries) { option ->
            OptionRow(
                label = option.label,
                isSelected = filters.sortOption == option,
                onClick = { onFiltersChange(filters.copy(sortOption = option)) },
            )
        }

        item {
            Text(tr("Genre"), style = MaterialTheme.typography.titleMedium, color = MooDark)
        }
        item {
            OptionRow(
                label = "Tous les genres",
                isSelected = filters.selectedGenreID == null,
                onClick = { onFiltersChange(filters.copy(selectedGenreID = null)) },
            )
        }
        items(availableGenres) { (genreID, genreName) ->
            OptionRow(
                label = genreName,
                isSelected = filters.selectedGenreID == genreID,
                onClick = { onFiltersChange(filters.copy(selectedGenreID = genreID)) },
            )
        }

        item {
            Text(tr("Ma note minimum"), style = MaterialTheme.typography.titleMedium, color = MooDark)
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                (0..5).forEach { value ->
                    PillButton(
                        label = if (value == 0) "Toutes" else "$value+ ⭐",
                        isSelected = filters.minPersonalRating == value,
                        onClick = { onFiltersChange(filters.copy(minPersonalRating = value)) },
                    )
                }
            }
        }

        item {
            Text(tr("Note TMDB minimum"), style = MaterialTheme.typography.titleMedium, color = MooDark)
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(0.0, 5.0, 6.0, 7.0, 8.0).forEach { value ->
                    PillButton(
                        label = if (value == 0.0) "Toutes" else "${value.toInt()}+",
                        isSelected = filters.minTMDBRating == value,
                        onClick = { onFiltersChange(filters.copy(minTMDBRating = value)) },
                    )
                }
            }
        }

        item {
            TextButton(onClick = { onFiltersChange(CineTableFilterState()) }) {
                Text(tr("Réinitialiser les filtres"), color = MooCoral)
            }
        }

        item {
            Button(
                onClick = onDone,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = LocalAccentColor.current),
            ) {
                Text(tr("OK"), color = MooDark)
            }
        }
    }
}

@Composable
private fun OptionRow(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.5.dp, MooBeige, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
    ) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(tr(label), style = MaterialTheme.typography.bodyMedium, color = MooDark)
            Icon(
                imageVector = if (isSelected) Icons.Filled.Check else Icons.Filled.RadioButtonUnchecked,
                contentDescription = null,
                tint = if (isSelected) MooGreen else MooTaupe,
            )
        }
    }
}

@Composable
private fun PillButton(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier.border(1.5.dp, MooBeige, RoundedCornerShape(10.dp)),
        color = if (isSelected) LocalAccentColor.current else androidx.compose.ui.graphics.Color.White,
        shape = RoundedCornerShape(10.dp),
    ) {
        Text(
            tr(label),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            color = if (isSelected) MooDark else MooTaupe,
            style = MaterialTheme.typography.labelMedium,
        )
    }
}
