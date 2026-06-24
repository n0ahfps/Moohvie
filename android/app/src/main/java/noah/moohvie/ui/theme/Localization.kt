package noah.moohvie.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import noah.moohvie.models.AppLanguage
import noah.moohvie.services.Translations

/** Provided by [MoohvieTheme], updates live when the user changes the language in Réglages. */
val LocalAppLanguage = compositionLocalOf { AppLanguage.SYSTEM }

/**
 * Translates [text] for display, mirroring the iOS app's `LocalizedStringKey` + Localizable.xcstrings
 * catalog: the French string itself is the lookup key. Falls back to [text] unchanged when there's no
 * entry (or the resolved language is French), so it's safe to wrap any literal — static or data-driven
 * (quiz questions, shop item names, genre names, etc.) — without needing a translation for every string
 * up front.
 */
@Composable
fun tr(text: String): String = Translations.translate(text, LocalAppLanguage.current)
