package noah.moohvie.services

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import noah.moohvie.models.ShopCatalog
import noah.moohvie.models.ShopItem
import noah.moohvie.models.ShopItemCategory

class ShopStore private constructor(context: Context) {
    private val prefs = context.getSharedPreferences("moohvie_settings", Context.MODE_PRIVATE)
    private val pointsStore = MoohPointsStore.getInstance(context)

    private val ownedItemIDsState = mutableStateOf(
        (prefs.getStringSet("shopOwnedItemIDs", emptySet()) ?: emptySet())
            .toSet()
            .plus(setOf(ShopCatalog.defaultThemeID, "title-novice", "badge-popcorn"))
    )
    val ownedItemIDs: Set<String> get() = ownedItemIDsState.value

    private val equippedThemeIDState = mutableStateOf(
        prefs.getString("shopEquippedThemeID", ShopCatalog.defaultThemeID) ?: ShopCatalog.defaultThemeID
    )
    var equippedThemeID: String
        get() = equippedThemeIDState.value
        private set(value) {
            equippedThemeIDState.value = value
            prefs.edit().putString("shopEquippedThemeID", value).apply()
        }

    private val equippedTitleIDState = mutableStateOf(prefs.getString("shopEquippedTitleID", null))
    var equippedTitleID: String?
        get() = equippedTitleIDState.value
        private set(value) {
            equippedTitleIDState.value = value
            prefs.edit().putString("shopEquippedTitleID", value).apply()
        }

    private val equippedBadgeIDState = mutableStateOf(prefs.getString("shopEquippedBadgeID", null))
    var equippedBadgeID: String?
        get() = equippedBadgeIDState.value
        private set(value) {
            equippedBadgeIDState.value = value
            prefs.edit().putString("shopEquippedBadgeID", value).apply()
        }

    fun isOwned(item: ShopItem): Boolean = ownedItemIDs.contains(item.id)

    fun purchase(item: ShopItem): Boolean {
        if (isOwned(item)) return true
        if (!pointsStore.spend(item.cost)) return false
        ownedItemIDsState.value = ownedItemIDsState.value + item.id
        prefs.edit().putStringSet("shopOwnedItemIDs", ownedItemIDsState.value).apply()
        return true
    }

    fun equip(item: ShopItem) {
        if (!isOwned(item)) return
        when (item.category) {
            ShopItemCategory.THEME -> equippedThemeID = item.id
            ShopItemCategory.TITLE -> equippedTitleID = item.id
            ShopItemCategory.BADGE -> equippedBadgeID = item.id
        }
    }

    val ownedThemes: List<ShopItem>
        get() = ShopCatalog.items.filter { it.category == ShopItemCategory.THEME && isOwned(it) }

    val equippedAccentColor: Color
        get() = ShopCatalog.item(withId = equippedThemeID)?.accentColor
            ?: ShopCatalog.item(withId = ShopCatalog.defaultThemeID)?.accentColor
            ?: Color(0xFFFFA500)

    val equippedTitleName: String?
        get() = equippedTitleID?.let { ShopCatalog.item(withId = it) }?.name

    val equippedBadgeIcon: String?
        get() = equippedBadgeID?.let { ShopCatalog.item(withId = it) }?.badgeIcon

    companion object {
        @Volatile private var instance: ShopStore? = null

        fun getInstance(context: Context): ShopStore =
            instance ?: synchronized(this) {
                instance ?: ShopStore(context.applicationContext).also { instance = it }
            }
    }
}
