package noah.moohvie.services

import noah.moohvie.models.AppLanguage
import java.util.Locale

/**
 * French → English lookup used by `tr()`, mirroring the iOS app's Localizable.xcstrings catalog:
 * the French string baked into the UI/data is the translation key. Anything not listed here (proper
 * nouns, streaming platform names, emoji, format strings) is simply displayed as-is in both languages.
 */
object Translations {
    private val frToEn: Map<String, String> = mapOf(
        // Home
        "MoohVie" to "MoohVie",
        "Trouvez le film du soir, ensemble" to "Find tonight's movie, together",
        "Rapide" to "Quick",
        "Équilibré" to "Balanced",
        "Précis" to "Precise",
        "5 questions essentielles" to "5 essential questions",
        "12 questions pour mieux cibler" to "12 questions for a better match",
        "20 questions pour un choix parfait" to "20 questions for the perfect pick",
        "🎲 Surprends-moi" to "🎲 Surprise me",
        "Aucune question, juste le hasard" to "No questions, just chance",
        "Mon Cinétable" to "My Movie Table",
        "Mes trophées" to "My trophies",
        "Boutique" to "Shop",
        "Réglages" to "Settings",
        "Cinétable" to "Movie Table",
        "Profil" to "Profile",
        "Trophées" to "Trophies",

        // Quiz / Swipe
        "Quiz terminé ! Genres retenus : " to "Quiz done! Genres kept: ",
        "Swipez ensemble jusqu'au bon film" to "Swipe together until you find the one",
        "Tirage aléatoire" to "Random pick",
        "Un autre film" to "Another movie",
        "On regarde celui-là !" to "Let's watch this one!",
        "😅 Plus de films à proposer" to "😅 No more movies to suggest",
        "Vous avez passé tous les films disponibles. Réessayez avec d'autres critères !" to
            "You've gone through every available movie. Try again with different criteria!",
        "Recommencer" to "Start over",
        "🎬 C'est parti !" to "🎬 Let's go!",
        "Disponible sur" to "Available on",
        "Ajouté à ton Cinétable !" to "Added to your Movie Table!",
        "On l'a regardé, ajouter au Cinétable" to "We watched it, add to Movie Table",
        "Aucun film trouvé avec ces critères 😕" to "No movie found with these criteria 😕",
        "Impossible de charger les films. Vérifie ta connexion." to "Couldn't load movies. Check your connection.",

        // Surprise / generic back
        "Retour" to "Back",

        // Cinétable
        "Ajouter un film" to "Add a movie",
        " Ajouter un film" to " Add a movie",
        "Ton Cinétable est vide" to "Your Movie Table is empty",
        "Les films que vous regardez ensemble apparaîtront ici" to
            "The movies you watch together will show up here",
        "Aucun film ne correspond à ces filtres" to "No movie matches these filters",
        " Filtres" to " Filters",
        "Retirer ce film du Cinétable ?" to "Remove this movie from your Movie Table?",
        "Retirer" to "Remove",
        "Annuler" to "Cancel",
        " Mon Cinétable" to " My Movie Table",
        " Mes trophées" to " My trophies",

        // Movie search
        "Titre du film..." to "Movie title...",
        "Aucun film trouvé" to "No movie found",
        "Déjà ajouté" to "Already added",
        "Ajouter" to "Add",
        "Fermer" to "Close",

        // Cinétable filters
        "Trier par" to "Sort by",
        "Plus récent d'abord" to "Most recent first",
        "Plus ancien d'abord" to "Oldest first",
        "Ma note (haute → basse)" to "My rating (high → low)",
        "Note TMDB (haute → basse)" to "TMDB rating (high → low)",
        "Genre" to "Genre",
        "Tous les genres" to "All genres",
        "Ma note minimum" to "My minimum rating",
        "Toutes" to "All",
        "Note TMDB minimum" to "Minimum TMDB rating",
        "Réinitialiser les filtres" to "Reset filters",
        "OK" to "OK",
        "Grille" to "Grid",
        "Liste" to "List",

        // Shop
        "Acheter" to "Buy",
        "Équiper" to "Equip",
        "Équipé" to "Equipped",
        "Thèmes" to "Themes",
        "Titres" to "Titles",
        "Badges" to "Badges",
        "Classique" to "Classic",
        "Océan" to "Ocean",
        "Forêt" to "Forest",
        "Rubis" to "Ruby",
        "Mystère" to "Mystery",
        "Doré" to "Golden",
        "Spectateur novice" to "Novice viewer",
        "Curieux du septième art" to "Curious about cinema",
        "Maison, téléphone" to "Phone home",
        "Explorateur de genres" to "Genre explorer",
        "Que la force soit avec toi" to "May the force be with you",
        "Il était une fois au cinéma" to "Once upon a time at the movies",
        "Pulp Spectateur" to "Pulp Viewer",
        "Critique redouté" to "Feared critic",
        "2001 : l'Odyssée du Popcorn" to "2001: A Popcorn Odyssey",
        "Un Cinétable pour les gouverner tous" to "One Movie Table to rule them all",
        "Citizen Cinéphile" to "Citizen Moviegoer",
        "Le Parrain du Cinétable" to "The Movie Table Godfather",
        "Légende du Cinétable" to "Movie Table Legend",
        "Popcorn d'or" to "Golden Popcorn",
        "Clap de cinéma" to "Movie Clapper",
        "Les Dents de la mer" to "Jaws",
        "S.O.S. Fantômes" to "Ghostbusters",
        "Jurassic Cinétable" to "Jurassic Movie Table",
        "Étoile filante" to "Shooting Star",
        "L'Anneau Unique" to "The One Ring",
        "Rencontre du troisième type" to "Close Encounter",
        "Le Roi du monde" to "King of the World",
        "Avec de grands pouvoirs..." to "With great power...",
        "Le Chevalier Noir" to "The Dark Knight",
        "Mère des Dragons" to "Mother of Dragons",
        "Couronne du Cinétable" to "Movie Table Crown",

        // Profile
        "Titre équipé" to "Equipped title",
        "Badge équipé" to "Equipped badge",
        "Modifier" to "Edit",
        "Valider" to "Confirm",
        "Avatar" to "Avatar",

        // Trophies
        "Cinéphile" to "Cinephile",
        "Critique exigeant" to "Demanding critic",
        "Explorateur des époques" to "Era explorer",
        "Regarde des films, tout simplement ! Chaque film ajouté au Cinétable compte." to
            "Just watch movies! Every movie added to your Movie Table counts.",
        "Regarde des films très bien notés sur TMDB" to "Watch highly rated movies on TMDB",
        "Regarde des films sortis dans des décennies différentes, du plus ancien au plus récent." to
            "Watch movies released across different decades, from oldest to newest.",
        "Regarde des films du genre" to "Watch movies in the genre",
        "Non débloqué" to "Not unlocked",
        "Maîtrisé" to "Mastered",
        "comptabilisé" to "counted",
        "comptabilisés" to "counted",
        "film comptabilisé" to "movie counted",
        "films comptabilisés" to "movies counted",
        "Débloqué le" to "Unlocked on",
        "Encore" to "Still",
        "film" to "movie",
        "films" to "movies",
        "pour ce palier" to "for this tier",
        "requis" to "required",
        "Bronze" to "Bronze",
        "Argent" to "Silver",
        "Or" to "Gold",
        "Platine" to "Platinum",
        "Émeraude" to "Emerald",
        "Diamant" to "Diamond",

        // Settings
        "Paramètres" to "Settings",
        "Mes plateformes de streaming" to "My streaming platforms",
        "Sélectionne tes abonnements pour ne voir que des films que tu peux regarder direct. Laisse tout décoché pour ne pas filtrer." to
            "Pick your subscriptions to only see movies you can watch right away. Leave everything unchecked to not filter.",
        "Contenu maximum" to "Maximum content rating",
        "Exclut les films classés au-dessus de ce seuil (violence, sexe, etc.)" to
            "Excludes movies rated above this threshold (violence, sex, etc.)",
        "Tous publics" to "All audiences",
        "Langue" to "Language",
        "Système" to "System",
        "Revoir les films déjà vus" to "Allow rewatching seen movies",
        "Si désactivé, les films de ton Cinétable ne seront plus proposés." to
            "If disabled, movies already in your Movie Table won't be suggested again.",

        // Genres
        "Action" to "Action",
        "Aventure" to "Adventure",
        "Animation" to "Animation",
        "Comédie" to "Comedy",
        "Crime" to "Crime",
        "Documentaire" to "Documentary",
        "Drame" to "Drama",
        "Familial" to "Family",
        "Fantastique" to "Fantasy",
        "Horreur" to "Horror",
        "Musique" to "Music",
        "Romance" to "Romance",
        "Science-Fiction" to "Science Fiction",
        "Thriller" to "Thriller",
        "Guerre" to "War",
        "Western" to "Western",
        "Autre" to "Other",
        "Non classé" to "Unclassified",

        // Quiz questions & answers
        "As-tu envie de rigoler ?" to "Feel like laughing?",
        "Oui, j'ai besoin de rire" to "Yes, I need a laugh",
        "Non, plutôt sérieux" to "No, something serious",
        "Peu importe" to "Doesn't matter",
        "Plutôt animation ou prise de vue réelle ?" to "Animation or live action?",
        "Prise de vue réelle" to "Live action",
        "Petite envie romantique ?" to "Feeling romantic?",
        "Oui, un peu d'amour" to "Yes, a bit of romance",
        "Non, pas ce soir" to "No, not tonight",
        "T'as envie de sensations fortes ?" to "Craving a thrill?",
        "Oui, action/aventure" to "Yes, action/adventure",
        "Non, plutôt calme" to "No, something calm",
        "Tu veux frissonner un peu ?" to "Want to get a little scared?",
        "Oui, horreur/thriller" to "Yes, horror/thriller",
        "Non merci" to "No thanks",
        "Un peu de science-fiction ou fantastique ?" to "A bit of sci-fi or fantasy?",
        "Oui, j'aime l'imaginaire" to "Yes, I love the imaginary",
        "Non, reste réaliste" to "No, keep it realistic",
        "Envie d'une enquête ou d'un mystère à résoudre ?" to "In the mood for a mystery to solve?",
        "Oui, j'aime chercher des indices" to "Yes, I like looking for clues",
        "Non, pas la tête à ça" to "No, not in the mood",
        "Un film basé sur une histoire vraie ou historique ?" to "A movie based on a true or historical story?",
        "Oui, j'aime apprendre" to "Yes, I like to learn",
        "Non, fiction pure" to "No, pure fiction",
        "Tu veux pleurer un peu (drame émotionnel) ?" to "Want a good cry (emotional drama)?",
        "Oui, j'ai besoin d'émotion" to "Yes, I need some emotion",
        "Non, je veux décompresser" to "No, I want to unwind",
        "Un film familial, sympa pour tout le monde ?" to "A family movie, nice for everyone?",
        "Oui, quelque chose de tout public" to "Yes, something for all audiences",
        "Non, plutôt adulte" to "No, something more adult",
        "Crime, mafia, ou enquête policière ?" to "Crime, mafia, or police investigation?",
        "Oui, ça m'intéresse" to "Yes, I'm interested",
        "Non" to "No",
        "Plutôt un film qui dure longtemps, ou court et efficace ?" to "A long movie, or short and snappy?",
        "Long, j'ai le temps ce soir" to "Long, I've got time tonight",
        "Court, je suis fatigué(e)" to "Short, I'm tired",
        "Un film récent ou un classique ?" to "A recent movie or a classic?",
        "Récent, je veux du frais" to "Recent, I want something fresh",
        "Classique, j'aime les anciens" to "Classic, I like the old ones",
        "Tu préfères un film léger ou qui fait réfléchir ?" to "Light movie or something thought-provoking?",
        "Léger, je veux me détendre" to "Light, I want to relax",
        "Qui fait réfléchir, j'ai envie de fond" to "Thought-provoking, I want some substance",
        "Envie d'un film avec de la musique ou de la danse ?" to "In the mood for music or dance in a movie?",
        "Oui, comédie musicale" to "Yes, a musical",
        "Tu es plutôt d'humeur à un Western ?" to "In the mood for a Western?",
        "Oui, pourquoi pas" to "Yes, why not",
        "Tu veux un film qui fait peur ou qui stresse vraiment ?" to "Want a movie that's really scary or stressful?",
        "Oui, à fond" to "Yes, all the way",
        "Non, juste un peu de suspense" to "No, just a bit of suspense",
        "Un film bien noté par la critique, ou peu importe la note ?" to "A critically acclaimed movie, or doesn't matter?",
        "Bien noté, je veux de la qualité" to "Well rated, I want quality",
        "Peu importe la note, l'ambiance compte plus" to "Rating doesn't matter, vibe matters more",
        "Plutôt un univers réaliste ou complètement déjanté ?" to "A realistic setting, or completely wild?",
        "Réaliste" to "Realistic",
        "Déjanté, j'aime l'absurde" to "Wild, I love the absurd",
        "Dernière question : tu veux un film qui se termine bien à coup sûr ?" to "Last question: do you want a movie that's guaranteed to end well?",
        "Oui, fin heureuse obligatoire" to "Yes, happy ending required",
        "Non, surprends-moi" to "No, surprise me",
    )

    fun translate(text: String, language: AppLanguage): String {
        val isEnglish = when (language) {
            AppLanguage.ENGLISH -> true
            AppLanguage.FRENCH -> false
            AppLanguage.SYSTEM -> Locale.getDefault().language != "fr"
        }
        if (!isEnglish) return text
        return frToEn[text] ?: text
    }
}
