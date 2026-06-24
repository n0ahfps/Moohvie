package noah.moohvie.models

import androidx.compose.ui.graphics.Color

enum class ShopItemCategory {
    THEME,
    TITLE,
    BADGE;

    val label: String
        get() = when (this) {
            THEME -> "Thèmes"
            TITLE -> "Titres"
            BADGE -> "Badges"
        }
}

data class ShopItem(
    val id: String,
    val category: ShopItemCategory,
    val name: String,
    val cost: Int,
    /** Couleur d'accent pour les items de type THEME. */
    val accentColor: Color? = null,
    /** Emoji utilisé pour les items de type BADGE. */
    val badgeIcon: String? = null,
)
