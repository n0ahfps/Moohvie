package noah.moohvie.models

object QuestionBank {
    val questions: List<Question> = listOf(
        Question(
            text = "As-tu envie de rigoler ?",
            options = listOf(
                AnswerOption(label = "Oui, j'ai besoin de rire", genresToAdd = listOf(35)),
                AnswerOption(label = "Non, plutôt sérieux", genresToExclude = listOf(35)),
                AnswerOption(label = "Peu importe"),
            ),
            priority = QuestionPriority.ESSENTIAL,
        ),
        Question(
            text = "Plutôt animation ou prise de vue réelle ?",
            options = listOf(
                AnswerOption(label = "Animation", genresToAdd = listOf(16)),
                AnswerOption(label = "Prise de vue réelle", genresToExclude = listOf(16)),
                AnswerOption(label = "Peu importe"),
            ),
            priority = QuestionPriority.ESSENTIAL,
        ),
        Question(
            text = "Petite envie romantique ?",
            options = listOf(
                AnswerOption(label = "Oui, un peu d'amour", genresToAdd = listOf(10749)),
                AnswerOption(label = "Non, pas ce soir"),
                AnswerOption(label = "Peu importe"),
            ),
            priority = QuestionPriority.ESSENTIAL,
        ),
        Question(
            text = "T'as envie de sensations fortes ?",
            options = listOf(
                AnswerOption(label = "Oui, action/aventure", genresToAdd = listOf(28, 12)),
                AnswerOption(label = "Non, plutôt calme", genresToExclude = listOf(28, 12)),
                AnswerOption(label = "Peu importe"),
            ),
            priority = QuestionPriority.ESSENTIAL,
        ),
        Question(
            text = "Tu veux frissonner un peu ?",
            options = listOf(
                AnswerOption(label = "Oui, horreur/thriller", genresToAdd = listOf(27, 53)),
                AnswerOption(label = "Non merci", genresToExclude = listOf(27, 53)),
                AnswerOption(label = "Peu importe"),
            ),
            priority = QuestionPriority.ESSENTIAL,
        ),
        Question(
            text = "Un peu de science-fiction ou fantastique ?",
            options = listOf(
                AnswerOption(label = "Oui, j'aime l'imaginaire", genresToAdd = listOf(878, 14)),
                AnswerOption(label = "Non, reste réaliste", genresToExclude = listOf(878, 14)),
                AnswerOption(label = "Peu importe"),
            ),
            priority = QuestionPriority.IMPORTANT,
        ),
        Question(
            text = "Envie d'une enquête ou d'un mystère à résoudre ?",
            options = listOf(
                AnswerOption(label = "Oui, j'aime chercher des indices", genresToAdd = listOf(9648)),
                AnswerOption(label = "Non, pas la tête à ça"),
                AnswerOption(label = "Peu importe"),
            ),
            priority = QuestionPriority.IMPORTANT,
        ),
        Question(
            text = "Un film basé sur une histoire vraie ou historique ?",
            options = listOf(
                AnswerOption(label = "Oui, j'aime apprendre", genresToAdd = listOf(36)),
                AnswerOption(label = "Non, fiction pure"),
                AnswerOption(label = "Peu importe"),
            ),
            priority = QuestionPriority.DETAILED,
        ),
        Question(
            text = "Tu veux pleurer un peu (drame émotionnel) ?",
            options = listOf(
                AnswerOption(label = "Oui, j'ai besoin d'émotion", genresToAdd = listOf(18)),
                AnswerOption(label = "Non, je veux décompresser"),
                AnswerOption(label = "Peu importe"),
            ),
            priority = QuestionPriority.IMPORTANT,
        ),
        Question(
            text = "Un film familial, sympa pour tout le monde ?",
            options = listOf(
                AnswerOption(label = "Oui, quelque chose de tout public", genresToAdd = listOf(10751)),
                AnswerOption(label = "Non, plutôt adulte"),
                AnswerOption(label = "Peu importe"),
            ),
            priority = QuestionPriority.IMPORTANT,
        ),
        Question(
            text = "Crime, mafia, ou enquête policière ?",
            options = listOf(
                AnswerOption(label = "Oui, ça m'intéresse", genresToAdd = listOf(80)),
                AnswerOption(label = "Non"),
                AnswerOption(label = "Peu importe"),
            ),
            priority = QuestionPriority.DETAILED,
        ),
        Question(
            text = "Plutôt un film qui dure longtemps, ou court et efficace ?",
            options = listOf(
                AnswerOption(label = "Long, j'ai le temps ce soir"),
                AnswerOption(label = "Court, je suis fatigué(e)", maxRuntime = 100),
                AnswerOption(label = "Peu importe"),
            ),
            priority = QuestionPriority.IMPORTANT,
        ),
        Question(
            text = "Un film récent ou un classique ?",
            options = listOf(
                AnswerOption(label = "Récent, je veux du frais", minReleaseYear = 2015),
                AnswerOption(label = "Classique, j'aime les anciens", maxReleaseYear = 2000),
                AnswerOption(label = "Peu importe"),
            ),
            priority = QuestionPriority.IMPORTANT,
        ),
        Question(
            text = "Tu préfères un film léger ou qui fait réfléchir ?",
            options = listOf(
                AnswerOption(label = "Léger, je veux me détendre"),
                AnswerOption(label = "Qui fait réfléchir, j'ai envie de fond"),
                AnswerOption(label = "Peu importe"),
            ),
            priority = QuestionPriority.DETAILED,
        ),
        Question(
            text = "Envie d'un film avec de la musique ou de la danse ?",
            options = listOf(
                AnswerOption(label = "Oui, comédie musicale", genresToAdd = listOf(10402)),
                AnswerOption(label = "Non"),
                AnswerOption(label = "Peu importe"),
            ),
            priority = QuestionPriority.DETAILED,
        ),
        Question(
            text = "Tu es plutôt d'humeur à un Western ?",
            options = listOf(
                AnswerOption(label = "Oui, pourquoi pas", genresToAdd = listOf(37)),
                AnswerOption(label = "Non, pas ce soir"),
                AnswerOption(label = "Peu importe"),
            ),
            priority = QuestionPriority.DETAILED,
        ),
        Question(
            text = "Tu veux un film qui fait peur ou qui stresse vraiment ?",
            options = listOf(
                AnswerOption(label = "Oui, à fond", genresToAdd = listOf(27)),
                AnswerOption(label = "Non, juste un peu de suspense", genresToAdd = listOf(53)),
                AnswerOption(label = "Peu importe"),
            ),
            priority = QuestionPriority.DETAILED,
        ),
        Question(
            text = "Un film bien noté par la critique, ou peu importe la note ?",
            options = listOf(
                AnswerOption(label = "Bien noté, je veux de la qualité", minVoteAverage = 7.0),
                AnswerOption(label = "Peu importe la note, l'ambiance compte plus"),
                AnswerOption(label = "Peu importe"),
            ),
            priority = QuestionPriority.IMPORTANT,
        ),
        Question(
            text = "Plutôt un univers réaliste ou complètement déjanté ?",
            options = listOf(
                AnswerOption(label = "Réaliste"),
                AnswerOption(label = "Déjanté, j'aime l'absurde", genresToAdd = listOf(35)),
                AnswerOption(label = "Peu importe"),
            ),
            priority = QuestionPriority.DETAILED,
        ),
        Question(
            text = "Dernière question : tu veux un film qui se termine bien à coup sûr ?",
            options = listOf(
                AnswerOption(label = "Oui, fin heureuse obligatoire"),
                AnswerOption(label = "Non, surprends-moi"),
                AnswerOption(label = "Peu importe"),
            ),
            priority = QuestionPriority.DETAILED,
        ),
    )
}
