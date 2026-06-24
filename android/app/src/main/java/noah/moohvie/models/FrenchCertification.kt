package noah.moohvie.models

enum class FrenchCertification(val rawValue: String, val maxAllowed: String) {
    ALL("Tous publics", "U"),
    TWELVE("-12", "12"),
    SIXTEEN("-16", "16"),
    EIGHTEEN("-18", "18");

    companion object {
        fun fromRawValue(rawValue: String): FrenchCertification =
            entries.firstOrNull { it.rawValue == rawValue } ?: ALL
    }
}
