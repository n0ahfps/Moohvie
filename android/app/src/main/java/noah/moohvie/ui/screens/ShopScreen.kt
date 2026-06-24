@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package noah.moohvie.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import noah.moohvie.models.ShopCatalog
import noah.moohvie.models.ShopItem
import noah.moohvie.models.ShopItemCategory
import noah.moohvie.services.MoohPointsStore
import noah.moohvie.services.ShopStore
import noah.moohvie.ui.theme.MooBeige
import noah.moohvie.ui.theme.MooDark
import noah.moohvie.ui.theme.MooGreen
import noah.moohvie.ui.theme.MooOrangeDefault
import noah.moohvie.ui.theme.MooTaupe

@Composable
fun ShopScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val shop = remember { ShopStore.getInstance(context) }
    val pointsStore = remember { MoohPointsStore.getInstance(context) }
    var selectedCategory by remember { mutableStateOf(ShopItemCategory.THEME) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Boutique") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour", tint = MooDark)
                    }
                },
            )
        },
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                Text("🔶 ${pointsStore.totalPoints} points", color = MooOrangeDefault)
            }

            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                ShopItemCategory.entries.forEachIndexed { index, category ->
                    SegmentedButton(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = ShopItemCategory.entries.size),
                    ) {
                        Text(category.label)
                    }
                }
            }

            val itemsInCategory = ShopCatalog.items.filter { it.category == selectedCategory }

            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(itemsInCategory) { item ->
                    ShopItemRow(
                        item = item,
                        isOwned = shop.isOwned(item),
                        isEquipped = isItemEquipped(shop, item),
                        totalPoints = pointsStore.totalPoints,
                        onPurchase = { shop.purchase(item) },
                        onEquip = { shop.equip(item) },
                    )
                }
            }
        }
    }
}

private fun isItemEquipped(shop: ShopStore, item: ShopItem): Boolean = when (item.category) {
    ShopItemCategory.THEME -> shop.equippedThemeID == item.id
    ShopItemCategory.TITLE -> shop.equippedTitleID == item.id
    ShopItemCategory.BADGE -> shop.equippedBadgeID == item.id
}

@Composable
private fun ShopItemRow(
    item: ShopItem,
    isOwned: Boolean,
    isEquipped: Boolean,
    totalPoints: Int,
    onPurchase: () -> Unit,
    onEquip: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(androidx.compose.ui.graphics.Color.White, RoundedCornerShape(14.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        ShopItemIcon(item)

        Column(modifier = Modifier.weight(1f)) {
            Text(item.name, style = MaterialTheme.typography.bodyMedium, color = MooDark)
            if (!isOwned) {
                Text("🔶 ${item.cost}", style = MaterialTheme.typography.labelSmall, color = MooTaupe)
            }
        }

        when {
            isEquipped -> Text("Équipé", style = MaterialTheme.typography.labelMedium, color = MooGreen)
            isOwned -> Button(
                onClick = onEquip,
                colors = ButtonDefaults.buttonColors(containerColor = MooOrangeDefault),
            ) {
                Text("Équiper", color = androidx.compose.ui.graphics.Color.White)
            }
            else -> Button(
                onClick = onPurchase,
                enabled = totalPoints >= item.cost,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MooOrangeDefault.copy(alpha = 0.3f),
                    disabledContainerColor = MooBeige,
                ),
            ) {
                Text("Acheter", color = MooDark)
            }
        }
    }
}

@Composable
private fun ShopItemIcon(item: ShopItem) {
    when (item.category) {
        ShopItemCategory.THEME -> Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(item.accentColor ?: MooOrangeDefault),
        )
        ShopItemCategory.TITLE -> Icon(
            Icons.Filled.TextFields,
            contentDescription = null,
            tint = MooOrangeDefault,
            modifier = Modifier.size(32.dp),
        )
        ShopItemCategory.BADGE -> Box(modifier = Modifier.size(32.dp), contentAlignment = Alignment.Center) {
            Text(item.badgeIcon ?: "🏅", style = MaterialTheme.typography.titleLarge)
        }
    }
}
