# WeatherSnap

WeatherSnap is a production-style Android application built with Kotlin and Jetpack Compose. It allows users to search for real-time weather data, create weather reports with captured images, and save them locally.

## Features

- **City Search**: Real-time city suggestions using Open-Meteo Geocoding API.
- **Weather Details**: Fetches current temperature, condition, humidity, wind speed, and pressure.
- **Custom Camera**: A full-screen CameraX implementation for capturing report photos (No intents used).
- **Image Compression**: Automatically reduces image quality to 60% and compares file sizes to optimize storage.
- **Draft Recovery**: Uses `SavedStateHandle` to preserve report progress (notes, images, weather) across lifecycle changes like device rotation.
- **Local Storage**: Persistent reports stored in a Room Database.

## Tech Stack

- **UI**: Jetpack Compose (Material 3)
- **Architecture**: MVVM + Clean Architecture
- **DI**: Hilt
- **Navigation**: Compose Navigation
- **Network**: Retrofit + OkHttp + GSON
- **Database**: Room
- **Camera**: CameraX
- **Image Loading**: Coil
- **Concurrency**: Coroutines & Flow

## Setup Instructions

1. **Clone the project** into Android Studio.
2. **Sync Gradle**: Ensure you are using the latest stable version of Android Studio (Iguana or newer recommended).
3. **Permissions**: The app will request Camera permission on launch. Internet permission is included in the Manifest for API calls.
4. **Run**: Deploy to a physical device or emulator with Play Store services.

## Architecture Explanation

The project follows **Clean Architecture** principles:

- **Domain Layer**: Contains the core business logic, models (`City`, `WeatherSnapshot`), and Repository interfaces. It is independent of any framework.
- **Data Layer**: Implements the repositories. Handles API calls via Retrofit and local persistence via Room. It also includes Mappers to convert DTOs/Entities to Domain models.
- **UI Layer**: Composed of ViewModels and Compose Screens. ViewModels use `StateFlow` to expose UI state and `SavedStateHandle` for state restoration.

## Lifecycle & Draft Recovery

To ensure a seamless user experience, the `CreateReportViewModel` leverages `SavedStateHandle`. When a user captures an image or types notes, the data is immediately cached in the `SavedStateHandle`. This ensures that if the process is killed or the screen is rotated, the "draft" report is restored instantly without losing data or requiring a re-fetch of the weather snapshot.

## Image Compression

The `ImageCompressor` utility uses the `Bitmap.compress` API with `JPEG` format and `60%` quality. 
1. The original image is captured to a temporary file in the `cacheDir`.
2. The compressor reads this file, reduces quality, and writes a new compressed version.
3. The UI displays both the original and compressed sizes to the user, highlighting the storage savings.
4. Temporary files are stored in `cacheDir` to allow the system to clean them up when storage is low.

---
**Note**: This app uses the Open-Meteo API which does not require an API key for standard usage.
