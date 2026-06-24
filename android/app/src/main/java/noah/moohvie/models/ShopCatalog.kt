package noah.moohvie.models

import androidx.compose.ui.graphics.Color

object ShopCatalog {
    /** Le thème "Classique" est toujours possédé et équipé par défaut, gratuitement. */
    const val defaultThemeID = "theme-classic"

    private val mooOrangeBase = Color(red = 244f / 255, green = 162f / 255, blue = 38f / 255)

    val items: List<ShopItem> = listOf(
        ShopItem(defaultThemeID, ShopItemCategory.THEME, "Classique", 0, accentColor = mooOrangeBase),
        ShopItem("theme-ocean", ShopItemCategory.THEME, "Océan", 50, accentColor = Color(0.20f, 0.55f, 0.85f)),
        ShopItem("theme-forest", ShopItemCategory.THEME, "Forêt", 50, accentColor = Color(0.20f, 0.55f, 0.30f)),
        ShopItem("theme-ruby", ShopItemCategory.THEME, "Rubis", 75, accentColor = Color(0.75f, 0.15f, 0.25f)),
        ShopItem("theme-mystic", ShopItemCategory.THEME, "Mystère", 100, accentColor = Color(0.45f, 0.25f, 0.70f)),
        ShopItem("theme-gold", ShopItemCategory.THEME, "Doré", 150, accentColor = Color(0.80f, 0.65f, 0.10f)),

        ShopItem("title-novice", ShopItemCategory.TITLE, "Spectateur novice", 0),
        ShopItem("title-curious", ShopItemCategory.TITLE, "Curieux du septième art", 30),
        ShopItem("title-et", ShopItemCategory.TITLE, "Maison, téléphone", 40),
        ShopItem("title-explorer", ShopItemCategory.TITLE, "Explorateur de genres", 60),
        ShopItem("title-jedi", ShopItemCategory.TITLE, "Que la force soit avec toi", 70),
        ShopItem("title-westworld", ShopItemCategory.TITLE, "Il était une fois au cinéma", 80),
        ShopItem("title-pulp", ShopItemCategory.TITLE, "Pulp Spectateur", 90),
        ShopItem("title-critic", ShopItemCategory.TITLE, "Critique redouté", 100),
        ShopItem("title-odyssey", ShopItemCategory.TITLE, "2001 : l'Odyssée du Popcorn", 110),
        ShopItem("title-ring", ShopItemCategory.TITLE, "Un Cinétable pour les gouverner tous", 130),
        ShopItem("title-kane", ShopItemCategory.TITLE, "Citizen Cinéphile", 150),
        ShopItem("title-godfather", ShopItemCategory.TITLE, "Le Parrain du Cinétable", 175),
        ShopItem("title-legend", ShopItemCategory.TITLE, "Légende du Cinétable", 200),

        ShopItem("badge-popcorn", ShopItemCategory.BADGE, "Popcorn d'or", 0, badgeIcon = "🍿"),
        ShopItem("badge-clapper", ShopItemCategory.BADGE, "Clap de cinéma", 30, badgeIcon = "🎬"),
        ShopItem("badge-shark", ShopItemCategory.BADGE, "Les Dents de la mer", 40, badgeIcon = "🦈"),
        ShopItem("badge-ghost", ShopItemCategory.BADGE, "S.O.S. Fantômes", 50, badgeIcon = "👻"),
        ShopItem("badge-dino", ShopItemCategory.BADGE, "Jurassic Cinétable", 60, badgeIcon = "🦖"),
        ShopItem("badge-star", ShopItemCategory.BADGE, "Étoile filante", 70, badgeIcon = "🌟"),
        ShopItem("badge-ring", ShopItemCategory.BADGE, "L'Anneau Unique", 80, badgeIcon = "💍"),
        ShopItem("badge-ufo", ShopItemCategory.BADGE, "Rencontre du troisième type", 90, badgeIcon = "🛸"),
        ShopItem("badge-ship", ShopItemCategory.BADGE, "Le Roi du monde", 100, badgeIcon = "🚢"),
        ShopItem("badge-spider", ShopItemCategory.BADGE, "Avec de grands pouvoirs...", 110, badgeIcon = "🕷️"),
        ShopItem("badge-bat", ShopItemCategory.BADGE, "Le Chevalier Noir", 120, badgeIcon = "🦇"),
        ShopItem("badge-dragon", ShopItemCategory.BADGE, "Mère des Dragons", 140, badgeIcon = "🐉"),
        ShopItem("badge-crown", ShopItemCategory.BADGE, "Couronne du Cinétable", 175, badgeIcon = "👑"),
    )

    fun item(withId: String): ShopItem? = items.firstOrNull { it.id == withId }
}
