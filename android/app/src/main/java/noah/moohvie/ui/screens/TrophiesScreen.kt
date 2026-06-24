@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package noah.moohvie.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import noah.moohvie.models.TrophyProgress
import noah.moohvie.services.CineTableStore
import noah.moohvie.services.TrophyEngine
import noah.moohvie.ui.theme.MooBeige
import noah.moohvie.ui.theme.MooDark
import noah.moohvie.ui.theme.MooTaupe
import noah.moohvie.ui.theme.tr

@Composable
fun TrophiesScreen(onBack: () -> Unit, onOpenTrophy: (TrophyProgress) -> Unit) {
    val context = LocalContext.current
    val store = remember { CineTableStore.getInstance(context) }
    val trophies = TrophyEngine.trophies(store.watchedMovies)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(tr("Trophées")) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = tr("Retour"), tint = MooDark)
                    }
                },
            )
        },
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize().padding(padding),
        ) {
            items(trophies) { trophy ->
                TrophyCard(trophy, onClick = { onOpenTrophy(trophy) })
            }
        }
    }
}

@Composable
private fun TrophyCard(trophy: TrophyProgress, onClick: () -> Unit) {
    val tierColor = trophy.tier?.color ?: MooBeige

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(androidx.compose.ui.graphics.Color.White, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            imageVector = trophyIcon(trophy.icon),
            contentDescription = tr(trophy.title),
            tint = tierColor,
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(tierColor.copy(alpha = 0.15f))
                .padding(14.dp),
        )

        Text(
            tr(trophy.title),
            style = MaterialTheme.typography.labelMedium,
            color = MooDark,
            maxLines = 2,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        )

        Text(
            trophy.tier?.label?.let { tr(it) } ?: tr("Non débloqué"),
            style = MaterialTheme.typography.labelSmall,
            color = if (trophy.tier != null) tierColor else MooTaupe,
        )

        Text(
            trophy.nextThreshold?.let { "${trophy.count}/$it" } ?: tr("Maîtrisé"),
            style = MaterialTheme.typography.labelSmall,
            color = MooTaupe,
        )
    }
}
