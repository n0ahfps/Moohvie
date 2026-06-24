package noah.moohvie.models

import androidx.compose.ui.graphics.Color

enum class TrophyTier {
    BRONZE,
    ARGENT,
    OR,
    PLATINE,
    EMERAUDE,
    DIAMANT,
    CINEPHILE;

    val label: String
        get() = when (this) {
            BRONZE -> "Bronze"
            ARGENT -> "Argent"
            OR -> "Or"
            PLATINE -> "Platine"
            EMERAUDE -> "Émeraude"
            DIAMANT -> "Diamant"
            CINEPHILE -> "Cinéphile"
        }

    val color: Color
        get() = when (this) {
            BRONZE -> Color(0xFFB87333)
            ARGENT -> Color(0xFFADADAD)
            OR -> Color(0xFFFFD700)
            PLATINE -> Color(0xFFB3D9E6)
            EMERAUDE -> Color(0xFF009966)
            DIAMANT -> Color(0xFF4DBFF2)
            CINEPHILE -> Color(0xFF9933CC)
        }
}
