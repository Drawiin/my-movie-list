# Module System

This document describes the modular architecture of the MyMovieList project and how different modules are organized and structured.

## Overview

Although full modularization is not completely implemented yet, the project package structure is designed to allow for easy separation of features into modules. Each module follows Clean Architecture principles, separating concerns into three main layers: **Data**, **Domain**, and **Presentation**.

## Architecture Layers

Each module in the project follows Clean Architecture principles with three distinct layers:

### Data Layer
- **Responsibility**: Data sources, including network and local storage
- **Components**: 
  - Data sources (remote and local)
  - Data Transfer Objects (DTOs)
  - Entity classes
  - Repository implementations
- **Purpose**: Abstracts data access and provides a unified interface for data operations

### Domain Layer
- **Responsibility**: Business logic and use cases
- **Components**:
  - Domain models
  - Use cases (business logic)
  - Repository interfaces
- **Purpose**: Defines the core functionality of the app, independent of any frameworks

### Presentation Layer
- **Responsibility**: UI implementation using Jetpack Compose
- **Components**:
  - Composable screens
  - ViewModels
  - UI state models
- **Purpose**: Manages UI-related data in a lifecycle-conscious way

## Module Types

The project is organized into several types of modules:

### 1. `app` Module
**Location**: Root module (main application)

**Purpose**: 
- Main application entry point
- Overall app configuration
- Navigation setup and wiring
- Dependency Injection (Hilt) configuration
- Application-level components

**Key Responsibilities**:
- Initializes the application
- Sets up the navigation graph
- Configures dependency injection modules
- Wires together all feature modules

### 2. `core` Module
**Location**: `app/src/main/java/com/drawiin/mymovielist/core/`

**Purpose**: 
Contains shared utilities, base classes, and common components used across the entire application.

**Components**:
- **Architecture**: Base ViewModel classes (`MyMovieListViewModel`), side effect management
- **Network**: Network client (`MyMovieListNetworkClient`), network configuration
- **UI Kit**: Reusable UI components (e.g., `MoviePosterImage`, `MoviePreviewCard`, `BackdropImage`)
- **Navigation**: App routes and navigation utilities
- **Localization**: Date formatting and other localization utilities
- **Test Utilities**: Custom testing utilities for ViewModels and UseCases

**Example Structure**:
```
core/
├── arch/
│   ├── MyMovieListViewModel.kt
│   ├── SingleSharedFlow.kt
│   └── OnStartEffect.kt
├── network/
│   ├── client/
│   └── di/
├── uikit/
│   ├── components/
│   └── foundation/
└── test/
```

### 3. `common` Module
**Location**: `app/src/main/java/com/drawiin/mymovielist/common/`

**Purpose**: 
Contains common business logic and utilities that can be reused across different features.

**Components**:
- **Movie Watchlist**: Common functionality for managing the watchlist
  - Data layer: Local database entities, DAOs, repositories
  - Domain layer: Use cases for adding, removing, checking, and retrieving watchlist items

**Example Structure**:
```
common/
└── movie/
    ├── data/
    │   ├── datasource/local/
    │   ├── entity/
    │   └── repository/
    ├── domain/
    │   ├── model/
    │   └── usecase/
    └── di/
```

**Key Features**:
- Movie entity storage
- Watchlist management (add, remove, check, get)
- Shared across features like Movie Details and Watchlist

### 4. `features` Module
**Location**: `app/src/main/java/com/drawiin/mymovielist/feature/`

**Purpose**: 
Contains feature-specific modules, each representing a distinct part of the app. Each feature module can be developed and tested independently.

**Current Features**:

#### `list` Feature
- **Purpose**: Displays an infinite list of popular movies
- **Structure**:
  - Data: Remote data source for fetching movie list from TMDb API
  - Domain: Use cases for retrieving movie lists
  - Presentation: Movie list screen with infinite scroll

#### `moviedetails` Feature
- **Purpose**: Shows detailed information about a specific movie
- **Structure**:
  - Data: Remote data source for fetching movie details
  - Domain: Use cases for retrieving movie details
  - Presentation: Movie details screen

#### `watchlist` Feature
- **Purpose**: Displays the user's personal watchlist
- **Structure**:
  - Presentation: Watchlist screen
  - Uses `common` module for watchlist operations

**Feature Module Structure**:
```
feature/
└── [feature-name]/
    ├── data/
    │   ├── datasource/
    │   ├── dto/
    │   └── repository/
    ├── domain/
    │   ├── model/
    │   └── usecase/
    ├── presentation/
    │   ├── [Feature]Screen.kt
    │   └── [Feature]ViewModel.kt
    └── di/
        └── [Feature]Module.kt
```

## Dependency Injection

Each module has its own Hilt module for dependency injection:

- **Network Module**: `core/network/di/NetworkModule.kt`
- **Movie Module**: `common/movie/di/MovieModule.kt`
- **Feature Modules**: Each feature has its own DI module (e.g., `feature/list/di/MovieListModule.kt`)

All modules are installed in the appropriate Hilt components (usually `SingletonComponent` or `ViewModelComponent`).

## Benefits of This Structure

1. **Separation of Concerns**: Each layer has a clear responsibility
2. **Testability**: Each module can be tested independently
3. **Reusability**: Core and common modules can be shared across features
4. **Maintainability**: Changes to one feature don't affect others
5. **Scalability**: Easy to add new features following the same pattern
6. **Future Modularization**: Package structure is ready for easy conversion to Gradle modules

## Future Modularization

The current package structure is designed to facilitate future full modularization. When ready, each package can be easily converted into a separate Gradle module with proper dependencies:

- `app` → application module
- `core` → `:core` library module
- `common` → `:common:movie` library module
- `feature` → `:feature:list`, `:feature:moviedetails`, `:feature:watchlist` feature modules

This will enable:
- Faster build times through parallel compilation
- Better dependency management
- Clearer module boundaries
- Independent feature development

## Related Documentation

- [UI State Management](./ui-state-management.md) - Learn how ViewModels are structured within modules

