@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package noah.moohvie.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import noah.moohvie.models.AppLanguage
import noah.moohvie.models.FrenchCertification
import noah.moohvie.models.StreamingProviderOption
import noah.moohvie.services.AppSettings
import noah.moohvie.ui.theme.MooBeige
import noah.moohvie.ui.theme.MooDark
import noah.moohvie.ui.theme.MooGreen
import noah.moohvie.ui.theme.LocalAccentColor
import noah.moohvie.ui.theme.MooTaupe
import noah.moohvie.ui.theme.tr

@Composable
fun SettingsScreen(onClose: () -> Unit) {
    val context = LocalContext.current
    val settings = remember { AppSettings.getInstance(context) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    TextButton(onClick = onClose) {
                        Text(tr("Fermer"), color = LocalAccentColor.current)
                    }
                },
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(28.dp),
        ) {
            item {
                Text(tr("Paramètres"), style = MaterialTheme.typography.headlineMedium, color = MooDark)
            }

            item {
                SettingsSection(
                    title = tr("Mes plateformes de streaming"),
                    description = tr("Sélectionne tes abonnements pour ne voir que des films que tu peux regarder direct. Laisse tout décoché pour ne pas filtrer."),
                )
            }
            items(StreamingProviderOption.all) { provider ->
                val isSelected = settings.selectedProviderIDs.contains(provider.id)
                OptionRow(
                    label = provider.name,
                    isSelected = isSelected,
                    onClick = {
                        settings.selectedProviderIDs = if (isSelected) {
                            settings.selectedProviderIDs - provider.id
                        } else {
                            settings.selectedProviderIDs + provider.id
                        }
                    },
                )
            }

            item {
                SettingsSection(
                    title = tr("Contenu maximum"),
                    description = tr("Exclut les films classés au-dessus de ce seuil (violence, sexe, etc.)"),
                )
            }
            items(FrenchCertification.entries) { cert ->
                OptionRow(
                    label = cert.rawValue,
                    isSelected = settings.maxCertification == cert,
                    onClick = { settings.maxCertification = cert },
                )
            }

            item {
                SettingsSection(title = tr("Langue"))
            }
            items(AppLanguage.entries) { language ->
                OptionRow(
                    label = language.label,
                    isSelected = settings.appLanguage == language,
                    onClick = { settings.appLanguage = language },
                )
            }

            item {
                SettingsSection(title = tr("Cinétable"))
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.5.dp, MooBeige, RoundedCornerShape(12.dp))
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(tr("Revoir les films déjà vus"), style = MaterialTheme.typography.bodyMedium, color = MooDark)
                    Switch(
                        checked = settings.allowRewatching,
                        onCheckedChange = { settings.allowRewatching = it },
                        colors = SwitchDefaults.colors(checkedTrackColor = MooGreen),
                    )
                }
            }
            item {
                Text(
                    tr("Si désactivé, les films de ton Cinétable ne seront plus proposés."),
                    style = MaterialTheme.typography.labelSmall,
                    color = MooTaupe,
                )
            }
        }
    }
}

@Composable
private fun SettingsSection(title: String, description: String? = null) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(title, style = MaterialTheme.typography.titleMedium, color = MooDark)
        description?.let {
            Text(it, style = MaterialTheme.typography.labelSmall, color = MooTaupe)
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
        color = Color.White,
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
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
