# MoohVie 🐮

Une app iOS pour trouver le film du soir, ensemble. Réponds à un quiz (ou laisse le hasard décider), puis swipez à deux jusqu'à trouver le film parfait.

## Fonctionnalités

- **Quiz** (rapide / équilibré / précis) pour affiner les critères : genres, durée, note minimale, période de sortie
- **Surprends-moi** : matching aléatoire sans quiz
- **Swipe à deux** sur les films suggérés, avec tirage aléatoire possible
- **Cinétable** : historique des films regardés, avec recherche et filtres
- Filtrage par plateformes de streaming et certification d'âge

## Stack technique

- SwiftUI
- [TMDB API](https://www.themoviedb.org/documentation/api) pour les données films et plateformes de streaming

## Configuration

1. Récupère une clé API sur [TMDB](https://www.themoviedb.org/settings/api)
2. Copie `Moohvie/Utils/secrets.swift.example` vers `Moohvie/Utils/secrets.swift`
3. Remplace `YOUR_TMDB_API_KEY` par ta clé

`secrets.swift` est ignoré par git (voir `.gitignore`), pas besoin de t'inquiéter de le commiter par erreur.

## Build

Ouvre `Moohvie.xcodeproj` dans Xcode (iOS 26.5+) et lance le target `Moohvie`.
