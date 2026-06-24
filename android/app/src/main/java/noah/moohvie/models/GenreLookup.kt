package noah.moohvie.models

object GenreLookup {
    private val names: Map<Int, String> = mapOf(
        28 to "Action",
        12 to "Aventure",
        16 to "Animation",
        35 to "Comédie",
        80 to "Crime",
        99 to "Documentaire",
        18 to "Drame",
        10751 to "Familial",
        14 to "Fantastique",
        27 to "Horreur",
        10402 to "Musique",
        9648 to "Mystère",
        10749 to "Romance",
        878 to "Science-Fiction",
        53 to "Thriller",
        10752 to "Guerre",
        37 to "Western",
    )

    fun name(forId: Int): String = names[forId] ?: "Autre"
}
