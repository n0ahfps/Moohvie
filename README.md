# MoohVie 🐮

Une app pour trouver le film du soir, ensemble. Réponds à un quiz (ou laisse le hasard décider), puis swipez à deux jusqu'à trouver le film parfait. Disponible sur iOS (SwiftUI) et Android (Kotlin/Compose).

## Fonctionnalités

- **Quiz** (rapide / équilibré / précis) pour affiner les critères : genres, durée, note minimale, période de sortie
- **Surprends-moi** : matching aléatoire sans quiz
- **Swipe à deux** sur les films suggérés, avec tirage aléatoire possible
- **Cinétable** : historique des films regardés, avec recherche et filtres
- Filtrage par plateformes de streaming et certification d'âge
- **Mooh Points** : système de points récompensant les pioches de films rares
- **Trophées** : suivi de la progression de visionnage par genre, note et époque
- **Boutique** : thèmes, titres et badges à débloquer avec les Mooh Points, et écran de profil
- **Langue** : bascule entre français et anglais

## Stack technique

- **iOS** : SwiftUI
- **Android** : Kotlin, Jetpack Compose
- [TMDB API](https://www.themoviedb.org/documentation/api) pour les données films et plateformes de streaming

## iOS

### Configuration

1. Récupère une clé API sur [TMDB](https://www.themoviedb.org/settings/api)
2. Copie `Moohvie/Utils/secrets.swift.example` vers `Moohvie/Utils/secrets.swift`
3. Remplace `YOUR_TMDB_API_KEY` par ta clé

`secrets.swift` est ignoré par git (voir `.gitignore`), pas besoin de t'inquiéter de le commiter par erreur.

### Build

Ouvre `Moohvie.xcodeproj` dans Xcode (iOS 26.5+) et lance le target `Moohvie`.

## Android

### Configuration

1. Récupère une clé API sur [TMDB](https://www.themoviedb.org/settings/api)
2. Copie `android/local.properties.example` vers `android/local.properties`
3. Remplace `YOUR_TMDB_API_KEY` par ta clé

`local.properties` est ignoré par git, pas besoin de t'inquiéter de le commiter par erreur.

### Build

Ouvre le dossier `android/` dans Android Studio (JDK 17), synchronise Gradle, puis lance le module `app` sur un émulateur ou un appareil.
