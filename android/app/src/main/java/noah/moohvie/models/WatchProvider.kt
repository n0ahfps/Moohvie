package noah.moohvie.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WatchProvider(
    @SerialName("provider_id") val id: Int,
    @SerialName("provider_name") val name: String,
    @SerialName("logo_path") val logoPath: String? = null,
) {
    val logoUrl: String?
        get() = logoPath?.let { "https://image.tmdb.org/t/p/w92$it" }
}

@Serializable
data class WatchProvidersResponse(
    val results: Map<String, CountryProviders> = emptyMap(),
)

@Serializable
data class CountryProviders(
    val flatrate: List<WatchProvider>? = null,
    val rent: List<WatchProvider>? = null,
    val buy: List<WatchProvider>? = null,
)

data class StreamingProviderOption(
    val id: Int,
    val name: String,
) {
    companion object {
        val all: List<StreamingProviderOption> = listOf(
            StreamingProviderOption(8, "Netflix"),
            StreamingProviderOption(119, "Prime Video"),
            StreamingProviderOption(337, "Disney+"),
            StreamingProviderOption(350, "Apple TV+"),
            StreamingProviderOption(381, "Canal+"),
            StreamingProviderOption(56, "OCS"),
            StreamingProviderOption(531, "Paramount+"),
            StreamingProviderOption(1899, "Max"),
            StreamingProviderOption(283, "Crunchyroll"),
            StreamingProviderOption(188, "YouTube Premium"),
            StreamingProviderOption(358, "Salto"),
            StreamingProviderOption(333, "ARTE"),
            StreamingProviderOption(234, "Mubi"),
            StreamingProviderOption(552, "Filmo"),
            StreamingProviderOption(1968, "Ciné+"),
        )
    }
}
