# TodoExpressive

A simple and expressive Todo list application for Android, built as part of an internship assignment. This project demonstrates the use of modern Android development practices and libraries.

## Features

*   Add, view, and manage your daily tasks.
*   Clean and intuitive user interface built with Jetpack Compose.
*   Persistent data storage to ensure your tasks are saved.

## Tech Stack & Libraries

This project is built with Kotlin and leverages the following key technologies from the Android Jetpack suite:

*   **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) for building the user interface declaratively.
*   **Design:** [Material 3](https://m3.material.io/) for modern and customizable UI components.
*   **Architecture:** Follows recommended Android app architecture principles.
    *   **ViewModel:** For managing UI-related data in a lifecycle-conscious way.
    *   **Navigation Compose:** For navigating between different screens within the app.
*   **Data Persistence:** [Room](https://developer.android.com/training/data-storage/room) for local, persistent storage of todo items.
*   **Core Libraries:** Utilizes AndroidX libraries like `Activity`, `Lifecycle`, and `core-ktx`.

## Project Structure

The project is a single-module Android application (`:app`) that contains all the necessary source code, resources, and configurations.

## Getting Started

To get a local copy up and running, follow these simple steps.

### Prerequisites

*   [Android Studio](https://developer.android.com/studio) (latest stable version recommended)
*   An Android device or emulator running API level 24 or higher.

### Build and Run

1.  **Clone the repository:**
    ```sh
    git clone <your-repository-url>
    ```
2.  **Open in Android Studio:**
    *   Open Android Studio.
    *   Select "Open" and navigate to the cloned project directory.
3.  **Sync Gradle:**
    *   Let Android Studio automatically sync the project with the Gradle files. This will download all the necessary dependencies.
4.  **Run the app:**
    *   Connect an Android device or start an emulator.
    *   Select the `app` run configuration from the dropdown menu.
    *   Click the "Run" button.

## Screenshots
