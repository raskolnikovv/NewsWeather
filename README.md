# NewsWeather
Android application that combines real-time news headlines with local weather information based on the user's current location.

https://github.com/user-attachments/assets/3133d47a-1721-4603-927e-75d0f18ff30f

## Tech Stack
- **UI:** Jetpack Compose with Material 3.
- **Architecture:** MVVM + Clean Architecture principles.
- **DI:** Hilt (Dagger).
- **Network:** Retrofit + GSON.
- **Location:** Google Play Services Location (FusedLocationProvider).
- **Asynchrony:** Kotlin Coroutines & Flow.

## Main Features
- **Dynamic News:** Fetches latest headlines from News API.
- **Location-Aware Weather:** Automatically detects user's city via Reverse Geocoding and fetches weather from Open-Meteo API.
- **Favorites:** Persistence for marking news articles as favorites (DataStore/Room).
- **Responsive UI:** Pull-to-refresh and shimmer loading states.

## How it works
1. On startup, the app requests location permissions.
2. Once granted, it retrieves the current coordinates and converts them to a city name.
3. The UI updates simultaneously with news articles and the specific weather for that region.
