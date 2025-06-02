# MyMovieList

**MyMovieList** is a simple Android app that displays a list of movies fetched from [TMDb](https://www.themoviedb.org/).
Users can view movie details and manage a personal watchlist by adding or removing movies.

> _A minimal movie browsing experience, built with modern Android tools._

## 🎬 Features

- 🔍 Fetch and display a list of movies from TMDb
- 📄 View detailed information for each movie
- ⭐ Add or remove movies from a personal watchlist

## 🧰 Tech Stack

This project is built using modern Android development tools and libraries:

- **Language**: Kotlin
- **UI**: Jetpack Compose + Material3
- **Architecture**: MVVM
- **Networking**: [Ktor Client](https://ktor.io/)
- **Serialization**: Kotlinx Serialization
- **State Management**: ViewModel + Kotlin Flow
- **Navigation**: Jetpack Navigation for Compose

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog or newer
- Android SDK 33+
- TMDb API Key (you'll need to provide your own)

### Run the App

1. Clone the repository:
    ```bash
    git clone https://github.com/Drawiin/my-movie-list.git
    ```

2. Open the project in Android Studio.

3. Add your TMDb API key to your `local.properties` or a configuration file, as instructed in the project’s `README` or `build.gradle`.

4. Build and run the app on an emulator or physical device.

## 📄 License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## 📝 Changelog

All notable changes are documented in [CHANGELOG.md](CHANGELOG.md).

## Tech Notes
Deep dive on some of the tech choices and patterns used in this project

### Architecture
The app uses overall clean architecture with three layers data, domain and presentation, the presentation uses
MVVM architecture implemented using a base class `MyMoviesViewModel` with some particularities:
- `State`: The single soruce of truth estate is exposed as a `StateFlow` to allow for easy observation in the UI, the state is always updated and start at a initial state for easier testing.
- `SideEffects`: The view model exposes a `SingleSharedFlow` for side effects, this allows the view model to emit events that should be handled by the UI, such as navigation or showing a snackbar.
    > Note: The `SingleSharedFlow` is used to ensure that the side effects are only emitted once and are not replayed on configuration changes and/or on resume events.
- `Actions`: The actions for simplicity are handled as simple callback functions on the viewmodel.
-
#### Interaction with the ViewModel on the UI
**Observing The State**
This is done using the `collectAsStateWithLifecycle` extension function, which allows the UI to observe the state and automatically handle lifecycle events.
```kotlin
val state by viewModel.state.collectAsStateWithLifecycle()
```

**Listening to Side Effects**
This is done using a especial composable function `CollectSideEffects` that allows the UI to listen to side effects and handle them accordingly.
```kotlin
 viewModel.sideEffects.SubscribeToSideEffects {
    when (it) {
        // Handle side effects here
    }
}
```

**Startup Actions**
Its common in apps to have some actions that need to be performed on startup, such as fetching data or checking permissions.
To offer more control over when these actions are performed, and facilitate testing, we have a special `OnStartSideEffect`
composable that allows the view model to perform actions on startup, it also ensures that the action is called only on the first composition
avoiding calling this function on configuration changes.

```kotlin
OnStartSideEffect {
    viewModel.loadInitalData()
}
```

#### Testing ViewModels
To facilitate testing of the ViewModels, a series of utility functions are provided to help with the setup of the tests.

## Project On Github
