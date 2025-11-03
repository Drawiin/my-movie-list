# UI State Management

This document explains how UI state management is implemented in the MyMovieList project using the MVVM (Model-View-ViewModel) pattern with unidirectional data flow.

## Overview

The presentation layer uses a base `MyMovieListViewModel` class to facilitate building UIs using unidirectional data flow, where the UI simply renders the `state` and reacts to `side effects`.

## Architecture Pattern

The project implements MVVM (Model-View-ViewModel) with the following characteristics:

- **Unidirectional Data Flow**: Data flows in one direction from ViewModel to UI
- **State Management**: UI state is exposed as `StateFlow` for easy observation
- **Side Effects**: One-time events are managed using `SingleSharedFlow`
- **Lifecycle Awareness**: ViewModels are lifecycle-aware and survive configuration changes

## Base ViewModel

All ViewModels in the project extend from `MyMovieListViewModel`, which provides a generic implementation for state and side effect management.

### Class Signature

```kotlin
open class MyMovieListViewModel<S, E>(private val initial: S) : ViewModel()
```

- **S**: The state type (e.g., `MovieListState`)
- **E**: The side effect type (e.g., `MyMoviesListSideEffect`)
- **initial**: The initial state value

### State Management

States are exposed as `StateFlow<S>` for easy observation in the UI:

```kotlin
private val _state = MutableStateFlow(initial)
val state: StateFlow<S> = _state.asStateFlow()
```

**Key Features**:
- State is immutable outside the ViewModel
- State changes are automatically observed by the UI
- State persists across configuration changes

**State Updates**:
The base ViewModel provides two methods for updating state:

```kotlin
// Update state using a reducer function
protected fun updateState(stateReducer: S.() -> S)

// Get current state and update atomically
protected fun getAndUpdateState(stateReducer: S.() -> S): S
```

### Side Effects Management

Side effects are managed using `SingleSharedFlow` to ensure they are emitted only once and not replayed on configuration changes:

```kotlin
private val _sideEffects = SingleSharedFlow<E>()
val sideEffects: SharedFlow<E> = _sideEffects
```

**Key Features**:
- One-time events (navigation, showing messages, etc.)
- Not replayed to new subscribers
- Replay cache is reset after each emission

**Dispatching Side Effects**:
```kotlin
protected fun dispatchSideEffect(sideEffect: E)
```

### SingleSharedFlow

`SingleSharedFlow` is a custom implementation that wraps `MutableSharedFlow` with replay cache reset functionality:

**Purpose**:
- Ensures side effects are only delivered once
- Prevents replay on configuration changes
- Useful for one-time events like navigation

**How It Works**:
- Uses `MutableSharedFlow` with `replay = 1`
- Automatically resets replay cache after each emission
- New subscribers don't receive old values

## Usage in ViewModels

### Example: MovieListViewModel

```kotlin
@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val getMoviesUseCase: GetMovieListUseCase,
    private val initial: MovieListState
) : MyMovieListViewModel<MovieListState, MyMoviesListSideEffect>(initial = initial) {
    
    // State definition
    data class MovieListState(
        val movies: List<MoviePreviewModel> = emptyList(),
        val currentPage: Int = 1,
        val totalPages: Int = 1,
        val isLoading: Boolean = false,
        val errorMessage: String? = null
    )
    
    // Side effect definition
    sealed interface MyMoviesListSideEffect {
        data class GoToMovieDetails(val movieId: Int) : MyMoviesListSideEffect
        data object GoToWatchList : MyMoviesListSideEffect
    }
    
    fun fetchMovies() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            getMoviesUseCase(state.value.currentPage)
                .onSuccess { result ->
                    updateState {
                        copy(
                            movies = result.movies,
                            currentPage = result.page,
                            totalPages = result.totalPages,
                        )
                    }
                }
                .onFailure { 
                    updateState { copy(errorMessage = it.message.toString()) } 
                }
            updateState { copy(isLoading = false) }
        }
    }
    
    fun onMovieClicked(movieId: Int) {
        dispatchSideEffect(MyMoviesListSideEffect.GoToMovieDetails(movieId))
    }
}
```

## Usage in Composables

### Observing State

States are observed using `collectAsStateWithLifecycle`:

```kotlin
@Composable
fun MovieListScreen(viewModel: MovieListViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    
    when {
        state.isLoading -> LoadingScreen()
        state.errorMessage != null -> ErrorScreen(state.errorMessage)
        else -> MovieListContent(state.movies)
    }
}
```

### Handling Side Effects

Side effects are collected and handled in a `LaunchedEffect`:

```kotlin
@Composable
fun MovieListScreen(
    viewModel: MovieListViewModel = hiltViewModel(),
    onNavigateToDetails: (Int) -> Unit,
    onNavigateToWatchList: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    
    // Handle side effects
    LaunchedEffect(Unit) {
        viewModel.sideEffects.collect { sideEffect ->
            when (sideEffect) {
                is MyMoviesListSideEffect.GoToMovieDetails -> 
                    onNavigateToDetails(sideEffect.movieId)
                is MyMoviesListSideEffect.GoToWatchList -> 
                    onNavigateToWatchList()
            }
        }
    }
    
    // UI content
}
```

### OnStartSideEffect

For startup actions, use the `OnStartSideEffect` composable:

```kotlin
@Composable
fun MovieListScreen(viewModel: MovieListViewModel = hiltViewModel()) {
    OnStartSideEffect {
        viewModel.fetchMovies()
    }
    
    // Rest of the UI
}
```

**Purpose**:
- Ensures actions are only called on the first composition
- Prevents duplicate calls on recomposition
- Useful for initial data loading

## Testing

The project provides custom test utilities to facilitate ViewModel testing:

### ViewModelTest Utility

```kotlin
viewModelTest(watchListViewModel) {
    coEvery { getWatchListUseCase.invoke() } returns Result.success(movies)

    viewModelUnderTest.getWatchList()
    advanceUntilIdle()

    states.first() assertIsEqualTo WatchListState.Loading
    states.last() assertIsEqualTo WatchListState.Success(movies)
}
```

**Features**:
- Automatically collects states and side effects
- Provides access to collected states and side effects
- Includes `advanceUntilIdle()` for coroutine testing
- Uses custom infix assertion functions for readable tests

### Test Structure

```kotlin
@ExperimentalCoroutinesApi
inline fun <S, E, VM : MyMovieListViewModel<S, E>> viewModelTest(
    viewModel: VM,
    crossinline test: Scope<S, E, VM>.() -> Unit,
) = runTest {
    val stateList = mutableStateListOf<S>()
    val sideEffectList = mutableStateListOf<E>()

    // Collect states and side effects
    val collectionJobs = arrayOf(
        launch(UnconfinedTestDispatcher()) {
            viewModel.state.collect { stateList.add(it) }
        },
        launch {
            viewModel.sideEffects.collect { sideEffectList.add(it) }
        }
    )

    Scope(this, stateList, sideEffectList, viewModel).test()

    collectionJobs.forEach { job -> job.cancel() }
}
```

## Best Practices

1. **State Immutability**: Always use data classes for state and update using `copy()`
2. **Side Effects for Events**: Use side effects for one-time events (navigation, messages)
3. **State for UI Data**: Use state for all UI data that needs to be displayed
4. **Type Safety**: Use sealed interfaces/classes for side effects to ensure exhaustive handling
5. **Initial State**: Always provide a meaningful initial state
6. **Lifecycle Awareness**: Use `collectAsStateWithLifecycle` in Composables
7. **Testing**: Write tests for state transitions and side effect emissions

## Benefits

1. **Unidirectional Flow**: Clear data flow makes the code easier to understand and debug
2. **Type Safety**: Generic ViewModel ensures type safety for states and side effects
3. **Testability**: Easy to test state changes and side effects
4. **Lifecycle Awareness**: State persists across configuration changes
5. **No Duplicate Events**: Side effects are only delivered once
6. **Consistency**: All ViewModels follow the same pattern

## Related Documentation

- [Module System](./module-system.md) - Learn how ViewModels fit into the module structure

