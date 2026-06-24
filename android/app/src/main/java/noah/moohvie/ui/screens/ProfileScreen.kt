@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package noah.moohvie.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import noah.moohvie.models.ShopCatalog
import noah.moohvie.models.ShopItem
import noah.moohvie.models.ShopItemCategory
import noah.moohvie.services.ProfileStore
import noah.moohvie.services.ShopStore
import noah.moohvie.ui.theme.MooBeige
import noah.moohvie.ui.theme.MooDark
import noah.moohvie.ui.theme.MooGreen
import noah.moohvie.ui.theme.LocalAccentColor
import noah.moohvie.ui.theme.MooTaupe
import noah.moohvie.ui.theme.tr

@Composable
fun ProfileScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val profile = remember { ProfileStore.getInstance(context) }
    val shop = remember { ShopStore.getInstance(context) }
    var isEditingUsername by remember { mutableStateOf(false) }
    var draftUsername by remember { mutableStateOf(profile.username) }

    val photoPicker = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let { profile.setProfileImage(it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(tr("Profil")) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = tr("Retour"), tint = MooDark)
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            AvatarPicker(
                imagePath = profile.profileImagePath,
                onClick = { photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) },
            )

            if (isEditingUsername) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(value = draftUsername, onValueChange = { draftUsername = it })
                    IconButton(onClick = {
                        if (draftUsername.isNotBlank()) profile.username = draftUsername
                        isEditingUsername = false
                    }) {
                        Icon(Icons.Filled.Check, contentDescription = tr("Valider"), tint = MooGreen)
                    }
                }
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        draftUsername = profile.username
                        isEditingUsername = true
                    },
                ) {
                    Text(profile.username, style = MaterialTheme.typography.titleMedium, color = MooDark)
                    Icon(Icons.Filled.Edit, contentDescription = tr("Modifier"), tint = MooTaupe, modifier = Modifier.size(16.dp))
                }
            }

            shop.equippedTitleName?.let {
                Text(tr(it), style = MaterialTheme.typography.bodyMedium, color = LocalAccentColor.current)
            }
            shop.equippedBadgeIcon?.let {
                Text(it, style = MaterialTheme.typography.headlineSmall)
            }

            SectionPicker(category = ShopItemCategory.TITLE, title = tr("Titre équipé"), shop = shop)
            SectionPicker(category = ShopItemCategory.BADGE, title = tr("Badge équipé"), shop = shop)
        }
    }
}

@Composable
private fun AvatarPicker(imagePath: String?, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .clip(CircleShape)
            .border(1.5.dp, MooBeige, CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        if (imagePath != null) {
            AsyncImage(model = imagePath, contentDescription = tr("Avatar"), modifier = Modifier.size(100.dp).clip(CircleShape))
        } else {
            Text("🐮", style = MaterialTheme.typography.displaySmall)
        }
    }
}

@Composable
private fun SectionPicker(category: ShopItemCategory, title: String, shop: ShopStore) {
    val ownedItems = ShopCatalog.items.filter { it.category == category && shop.isOwned(it) }

    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(title, style = MaterialTheme.typography.titleSmall, color = MooDark)
        ownedItems.forEach { item ->
            ShopOptionRow(item = item, isEquipped = isEquipped(shop, item), onClick = { shop.equip(item) })
        }
    }
}

@Composable
private fun ShopOptionRow(item: ShopItem, isEquipped: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .border(1.5.dp, MooBeige, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
    ) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(tr(item.name), style = MaterialTheme.typography.bodyMedium, color = MooDark)
            Icon(
                imageVector = if (isEquipped) Icons.Filled.Check else Icons.Filled.RadioButtonUnchecked,
                contentDescription = null,
                tint = if (isEquipped) MooGreen else MooTaupe,
            )
        }
    }
}

private fun isEquipped(shop: ShopStore, item: ShopItem): Boolean = when (item.category) {
    ShopItemCategory.THEME -> shop.equippedThemeID == item.id
    ShopItemCategory.TITLE -> shop.equippedTitleID == item.id
    ShopItemCategory.BADGE -> shop.equippedBadgeID == item.id
}
