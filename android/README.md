# MoohVie Android (en cours de portage)

Portage natif Kotlin + Jetpack Compose de l'app iOS, suivi par l'issue #5.

## État actuel

- Scaffold Gradle/Compose fonctionnel (namespace `noah.moohvie`, minSdk 26)
- Modèles de données portés depuis `Moohvie/Models` et `Moohvie/Utils/genrelookup.swift`
- `TMDBService` (Retrofit) portant `Moohvie/Services/TMDBService.swift`
- Écrans Accueil et Quiz (Compose) comme preuve de concept de l'architecture MVVM

Le reste des écrans iOS (Swipe, Cinétable, Boutique, Profil, Trophées, Réglages) n'est pas encore porté — voir le commentaire de roadmap sur l'issue #5.

## Configuration

1. Récupère une clé API sur [TMDB](https://www.themoviedb.org/settings/api)
2. Copie `local.properties.example` vers `local.properties`
3. Remplace `YOUR_TMDB_API_KEY` par ta clé

`local.properties` est ignoré par git.

## Build

Ouvre le dossier `android/` dans Android Studio (Kotlin 1.9, AGP 8.5) et lance le module `app`. Android Studio génère automatiquement le Gradle wrapper au premier sync si besoin.
