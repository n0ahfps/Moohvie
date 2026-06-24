package noah.moohvie.ui.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

val MooCream = Color(0xFFFDF8EE)
val MooDark = Color(0xFF2B2521)
val MooOrangeDefault = Color(0xFFE8784E)
val MooCoral = Color(0xFFE85D4E)
val MooGreen = Color(0xFF5FA777)
val MooBeige = Color(0xFFEFE6D2)
val MooTaupe = Color(0xFF8A8070)

/**
 * iOS exposes `Color.mooOrange` as a computed property reading `ShopStore.shared.equippedAccentColor`.
 * This CompositionLocal mirrors that: [MoohvieTheme] provides the equipped theme's accent color here,
 * so every screen reading [LocalAccentColor] updates live when the user equips a different theme.
 */
val LocalAccentColor = compositionLocalOf { MooOrangeDefault }
