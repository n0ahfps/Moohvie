# MoohVie Android

Portage natif Kotlin + Jetpack Compose de l'app iOS, suivi par l'issue #5. Tous les écrans (Accueil, Quiz, Swipe, Surprends-moi, Cinétable, Boutique, Profil, Trophées, Réglages) sont portés.

## Configuration

1. Récupère une clé API sur [TMDB](https://www.themoviedb.org/settings/api)
2. Copie `local.properties.example` vers `local.properties`
3. Remplace `YOUR_TMDB_API_KEY` par ta clé

`local.properties` est ignoré par git.

## Build

Ouvre le dossier `android/` dans Android Studio (Kotlin 2.0, AGP 8.5) et lance le module `app`.

En CLI : `./gradlew :app:assembleDebug` génère un `.apk` debug dans `app/build/outputs/apk/debug/`.

**JDK requis : 17** (pas plus récent). AGP utilise `jlink` sur `core-for-system-modules.jar` du SDK Android, qui échoue avec certains JDK 17 alternatifs (ex. GraalVM CE 17 plante avec une erreur `'for' is not a Java identifier`) et avec les JDK 18+. Un OpenJDK 17 standard (`brew install openjdk@17`) fonctionne. Si plusieurs JDK sont installés, force celui à utiliser sans toucher à ta config globale :

```
./gradlew :app:assembleDebug -Dorg.gradle.java.home=/opt/homebrew/Cellar/openjdk@17/<version>
```
