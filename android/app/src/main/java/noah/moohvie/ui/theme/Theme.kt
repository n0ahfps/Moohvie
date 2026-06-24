package noah.moohvie.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import noah.moohvie.services.AppSettings
import noah.moohvie.services.ShopStore

@Composable
fun MoohvieTheme(content: @Composable () -> Unit) {
    val context = LocalContext.current
    val shopStore = remember { ShopStore.getInstance(context) }
    val appSettings = remember { AppSettings.getInstance(context) }
    val accentColor = shopStore.equippedAccentColor
    val language = appSettings.appLanguage

    val colorScheme = lightColorScheme(
        primary = accentColor,
        secondary = MooCoral,
        tertiary = MooGreen,
        background = MooCream,
        surface = MooBeige,
        onPrimary = MooCream,
        onBackground = MooDark,
        onSurface = MooDark,
    )

    CompositionLocalProvider(
        LocalAccentColor provides accentColor,
        LocalAppLanguage provides language,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content,
        )
    }
}
