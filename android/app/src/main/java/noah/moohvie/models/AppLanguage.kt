package noah.moohvie.models

import java.util.Locale

enum class AppLanguage {
    SYSTEM,
    FRENCH,
    ENGLISH;

    val label: String
        get() = when (this) {
            SYSTEM -> "Système"
            FRENCH -> "Français"
            ENGLISH -> "English"
        }

    val locale: Locale
        get() = when (this) {
            SYSTEM -> Locale.getDefault()
            FRENCH -> Locale("fr")
            ENGLISH -> Locale("en")
        }

    /** Code de langue TMDB ("fr-FR", "en-US") utilisé pour récupérer titres/synopsis dans la bonne langue. */
    val tmdbLanguageCode: String
        get() = when (this) {
            SYSTEM -> if (Locale.getDefault().language == "fr") "fr-FR" else "en-US"
            FRENCH -> "fr-FR"
            ENGLISH -> "en-US"
        }
}
