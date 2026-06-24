@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package noah.moohvie.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Verified
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import noah.moohvie.models.TrophyMilestone
import noah.moohvie.models.TrophyProgress
import noah.moohvie.services.CineTableStore
import noah.moohvie.services.TrophyEngine
import noah.moohvie.ui.theme.MooBeige
import noah.moohvie.ui.theme.MooDark
import noah.moohvie.ui.theme.MooTaupe
import noah.moohvie.ui.theme.tr

private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

@Composable
fun TrophyDetailScreen(trophyId: String, onBack: () -> Unit) {
    val context = LocalContext.current
    val store = remember { CineTableStore.getInstance(context) }
    val trophy = TrophyEngine.trophies(store.watchedMovies).firstOrNull { it.id == trophyId } ?: return

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(tr(trophy.title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = tr("Retour"), tint = MooDark)
                    }
                },
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item { TrophyHeader(trophy) }
            items(trophy.milestones) { milestone -> MilestoneRow(milestone, trophy.count) }
            item {
                Text(
                    tr(trophy.description),
                    style = MaterialTheme.typography.bodySmall,
                    color = MooTaupe,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                )
            }
        }
    }
}

@Composable
private fun TrophyHeader(trophy: TrophyProgress) {
    val tierColor = trophy.tier?.color ?: MooBeige
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Icon(
            imageVector = trophyIcon(trophy.icon),
            contentDescription = tr(trophy.title),
            tint = tierColor,
            modifier = Modifier
                .size(84.dp)
                .clip(CircleShape)
                .background(tierColor.copy(alpha = 0.15f))
                .padding(20.dp),
        )
        Text(tr(trophy.title), style = MaterialTheme.typography.titleLarge, color = MooDark)
        Text(
            "${trophy.count} ${tr(if (trophy.count == 1) "film" else "films")} ${tr(if (trophy.count == 1) "comptabilisé" else "comptabilisés")}",
            style = MaterialTheme.typography.bodyMedium,
            color = MooTaupe,
        )
    }
}

@Composable
private fun MilestoneRow(milestone: TrophyMilestone, currentCount: Int) {
    val color = if (milestone.isUnlocked) milestone.tier.color else MooBeige

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(androidx.compose.ui.graphics.Color.White, RoundedCornerShape(14.dp))
            .border(1.5.dp, if (milestone.isUnlocked) color.copy(alpha = 0.4f) else MooBeige, RoundedCornerShape(14.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Icon(
            imageVector = if (milestone.isUnlocked) Icons.Filled.Verified else Icons.Filled.Lock,
            contentDescription = null,
            tint = color,
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(tr(milestone.tier.label), style = MaterialTheme.typography.bodyMedium, color = MooDark)
            if (milestone.unlockedDate != null) {
                Text(
                    "${tr("Débloqué le")} ${dateFormat.format(Date(milestone.unlockedDate))}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MooTaupe,
                )
            } else {
                val remaining = milestone.threshold - currentCount
                Text(
                    "${tr("Encore")} $remaining ${tr(if (remaining == 1) "film" else "films")} ${tr("pour ce palier")} (${milestone.threshold} ${tr("requis")})",
                    style = MaterialTheme.typography.labelSmall,
                    color = MooTaupe,
                )
            }
        }

        Text(
            "${milestone.threshold}",
            style = MaterialTheme.typography.bodyMedium,
            color = if (milestone.isUnlocked) color else MooTaupe,
        )
    }
}
