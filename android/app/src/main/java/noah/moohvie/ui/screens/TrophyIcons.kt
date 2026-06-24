package noah.moohvie.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.EmojiEmotions
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TheaterComedy
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.ui.graphics.vector.ImageVector

/** Traduit l'identifiant SF Symbol (porté tel quel depuis l'iOS) vers une icône Material. */
fun trophyIcon(name: String): ImageVector = when (name) {
    "film.fill", "film" -> Icons.Filled.Movie
    "star.fill" -> Icons.Filled.Star
    "clock.fill" -> Icons.Filled.Schedule
    "bolt.fill" -> Icons.Filled.Bolt
    "map.fill" -> Icons.Filled.Map
    "paintpalette.fill" -> Icons.Filled.Palette
    "face.smiling.fill" -> Icons.Filled.EmojiEmotions
    "magnifyingglass" -> Icons.Filled.Search
    "doc.text.fill" -> Icons.Filled.Description
    "theatermasks.fill" -> Icons.Filled.TheaterComedy
    "house.fill" -> Icons.Filled.Home
    "wand.and.stars" -> Icons.Filled.AutoFixHigh
    "eye.fill" -> Icons.Filled.Visibility
    "music.note" -> Icons.Filled.MusicNote
    "questionmark.circle.fill" -> Icons.AutoMirrored.Filled.HelpOutline
    "heart.fill" -> Icons.Filled.Favorite
    "atom" -> Icons.Filled.Science
    "bolt.heart.fill" -> Icons.Filled.Favorite
    "shield.fill" -> Icons.Filled.Shield
    "sun.dust.fill" -> Icons.Filled.WbSunny
    else -> Icons.Filled.Movie
}
