# WeatherSnap

WeatherSnap is an Android application that allows users to capture weather-stamped photos and save them as reports.

## Setup & Run
1.  **Clone the Repository**: Open the project in Android Studio (Iguana or newer).
2.  **Gradle Sync**: Wait for the project to sync dependencies (Hilt, CameraX, Room, Retrofit).
3.  **Permissions**: Grant Camera and Location permissions when prompted on the first run.
4.  **Run**: Select the `app` configuration and run on a physical device (recommended for CameraX) or an emulator with camera support.

## Architecture
- **Clean Architecture**: Divided into `data`, `domain`, and `ui` packages to ensure separation of concerns.
- **MVVM**: UI state is managed in ViewModels using `StateFlow`.
- **Hilt**: Dependency injection for repositories, database, and API services.

## Approach & Tradeoffs

### Camera Implementation
- **Approach**: Built a `CustomCameraScreen` using CameraX and `AndroidView` for a seamless in-app experience.
- **Tradeoff**: Used `PreviewView` inside `AndroidView` instead of a pure Compose implementation (which is still experimental) to ensure stability and better hardware compatibility.

### State Persistence
- **Approach**: Leveraged `SavedStateHandle` in `CreateReportViewModel`.
- **Tradeoff**: Adds slight complexity to the ViewModel initialization but ensures that captured photos and notes survive process death/reconfiguration without requiring an immediate database write.

### Lifecycle Management
- **Approach**: Switched to `androidx.compose.ui.platform.LocalLifecycleOwner` for the CameraX binding.
- **Tradeoff**: While `androidx.lifecycle.compose` offers a newer version, the platform-provided one is more consistently available across different navigation hosts, preventing the `CompositionLocal not present` crash.

### Image Processing
- **Approach**: Implemented manual JPEG compression (60% quality) to save storage.
- **Tradeoff**: Chose a fixed compression ratio for simplicity. While dynamic quality adjustment could optimize further, 60% provides a good balance between legibility of weather data and file size.
