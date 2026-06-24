package noah.moohvie.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val MoohvieColorScheme = lightColorScheme(
    primary = MooOrangeDefault,
    secondary = MooCoral,
    tertiary = MooGreen,
    background = MooCream,
    surface = MooBeige,
    onPrimary = MooCream,
    onBackground = MooDark,
    onSurface = MooDark,
)

@Composable
fun MoohvieTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MoohvieColorScheme,
        content = content,
    )
}
