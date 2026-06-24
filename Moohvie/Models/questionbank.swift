import Foundation

struct QuestionBank {
    static let questions: [Question] = [
        Question(
            text: "As-tu envie de rigoler ?",
            options: [
                AnswerOption(label: "Oui, j'ai besoin de rire", genresToAdd: [35]),
                AnswerOption(label: "Non, plutôt sérieux"),
                AnswerOption(label: "Peu importe")
            ],
            priority: .essential
        ),
        Question(
            text: "Plutôt animation ou prise de vue réelle ?",
            options: [
                AnswerOption(label: "Animation", genresToAdd: [16]),
                AnswerOption(label: "Prise de vue réelle", genresToExclude: [16]),
                AnswerOption(label: "Peu importe")
            ],
            priority: .essential
        ),
        Question(
            text: "Petite envie romantique ?",
            options: [
                AnswerOption(label: "Oui, un peu d'amour", genresToAdd: [10749]),
                AnswerOption(label: "Non, pas ce soir"),
                AnswerOption(label: "Peu importe")
            ],
            priority: .essential
        ),
        Question(
            text: "T'as envie de sensations fortes ?",
            options: [
                AnswerOption(label: "Oui, action/aventure", genresToAdd: [28, 12]),
                AnswerOption(label: "Non, plutôt calme"),
                AnswerOption(label: "Peu importe")
            ],
            priority: .essential
        ),
        Question(
            text: "Tu veux frissonner un peu ?",
            options: [
                AnswerOption(label: "Oui, horreur/thriller", genresToAdd: [27, 53]),
                AnswerOption(label: "Non merci"),
                AnswerOption(label: "Peu importe")
            ],
            priority: .essential
        ),
        Question(
            text: "Un peu de science-fiction ou fantastique ?",
            options: [
                AnswerOption(label: "Oui, j'aime l'imaginaire", genresToAdd: [878, 14]),
                AnswerOption(label: "Non, reste réaliste"),
                AnswerOption(label: "Peu importe")
            ],
            priority: .important
        ),
        Question(
            text: "Envie d'une enquête ou d'un mystère à résoudre ?",
            options: [
                AnswerOption(label: "Oui, j'aime chercher des indices", genresToAdd: [9648]),
                AnswerOption(label: "Non, pas la tête à ça"),
                AnswerOption(label: "Peu importe")
            ],
            priority: .important
        ),
        Question(
            text: "Un film basé sur une histoire vraie ou historique ?",
            options: [
                AnswerOption(label: "Oui, j'aime apprendre", genresToAdd: [36]),
                AnswerOption(label: "Non, fiction pure"),
                AnswerOption(label: "Peu importe")
            ],
            priority: .detailed
        ),
        Question(
            text: "Tu veux pleurer un peu (drame émotionnel) ?",
            options: [
                AnswerOption(label: "Oui, j'ai besoin d'émotion", genresToAdd: [18]),
                AnswerOption(label: "Non, je veux décompresser"),
                AnswerOption(label: "Peu importe")
            ],
            priority: .important
        ),
        Question(
            text: "Un film familial, sympa pour tout le monde ?",
            options: [
                AnswerOption(label: "Oui, quelque chose de tout public", genresToAdd: [10751]),
                AnswerOption(label: "Non, plutôt adulte"),
                AnswerOption(label: "Peu importe")
            ],
            priority: .important
        ),
        Question(
            text: "Crime, mafia, ou enquête policière ?",
            options: [
                AnswerOption(label: "Oui, ça m'intéresse", genresToAdd: [80]),
                AnswerOption(label: "Non"),
                AnswerOption(label: "Peu importe")
            ],
            priority: .detailed
        ),
        Question(
            text: "Plutôt un film qui dure longtemps, ou court et efficace ?",
            options: [
                AnswerOption(label: "Long, j'ai le temps ce soir"),
                AnswerOption(label: "Court, je suis fatigué(e)", maxRuntime: 100),
                AnswerOption(label: "Peu importe")
            ],
            priority: .important
        ),
        Question(
            text: "Un film récent ou un classique ?",
            options: [
                AnswerOption(label: "Récent, je veux du frais", minReleaseYear: 2015),
                AnswerOption(label: "Classique, j'aime les anciens", maxReleaseYear: 2000),
                AnswerOption(label: "Peu importe")
            ],
            priority: .important
        ),
        Question(
            text: "Tu préfères un film léger ou qui fait réfléchir ?",
            options: [
                AnswerOption(label: "Léger, je veux me détendre"),
                AnswerOption(label: "Qui fait réfléchir, j'ai envie de fond"),
                AnswerOption(label: "Peu importe")
            ],
            priority: .detailed
        ),
        Question(
            text: "Envie d'un film avec de la musique ou de la danse ?",
            options: [
                AnswerOption(label: "Oui, comédie musicale", genresToAdd: [10402]),
                AnswerOption(label: "Non"),
                AnswerOption(label: "Peu importe")
            ],
            priority: .detailed
        ),
        Question(
            text: "Tu es plutôt d'humeur à un Western ?",
            options: [
                AnswerOption(label: "Oui, pourquoi pas", genresToAdd: [37]),
                AnswerOption(label: "Non, pas ce soir"),
                AnswerOption(label: "Peu importe")
            ],
            priority: .detailed
        ),
        Question(
            text: "Tu veux un film qui fait peur ou qui stresse vraiment ?",
            options: [
                AnswerOption(label: "Oui, à fond", genresToAdd: [27]),
                AnswerOption(label: "Non, juste un peu de suspense", genresToAdd: [53]),
                AnswerOption(label: "Peu importe")
            ],
            priority: .detailed
        ),
        Question(
            text: "Un film bien noté par la critique, ou peu importe la note ?",
            options: [
                AnswerOption(label: "Bien noté, je veux de la qualité", minVoteAverage: 7.0),
                AnswerOption(label: "Peu importe la note, l'ambiance compte plus"),
                AnswerOption(label: "Peu importe")
            ],
            priority: .important
        ),
        Question(
            text: "Plutôt un univers réaliste ou complètement déjanté ?",
            options: [
                AnswerOption(label: "Réaliste"),
                AnswerOption(label: "Déjanté, j'aime l'absurde", genresToAdd: [35]),
                AnswerOption(label: "Peu importe")
            ],
            priority: .detailed
        ),
        Question(
            text: "Dernière question : tu veux un film qui se termine bien à coup sûr ?",
            options: [
                AnswerOption(label: "Oui, fin heureuse obligatoire"),
                AnswerOption(label: "Non, surprends-moi"),
                AnswerOption(label: "Peu importe")
            ],
            priority: .detailed
        )
    ]
}
