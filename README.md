# WeatherApp

A simple Android weather application that uses the MVVM (Model-View-ViewModel) architecture to fetch and display weather data from a remote API.


## Features

- Fetches current weather data from a remote API
- Displays temperature, weather conditions, and more
- MVVM architecture for clean separation of concerns
- Uses Retrofit for API calls
- LiveData for observing data changes

## Tech Stack

- **Language:** Kotlin
- **Architecture:** MVVM (Model-View-ViewModel)
- **Networking:** Retrofit
- **Async:** Coroutines
- **Data Binding:** LiveData, ViewModel
- **UI:** XML, RecyclerView
- **Image Loading:** Glide 


## Architecture

- **Model:** Represents the data layer of the app, including data classes for weather information and Room entities.
- **View:** The UI layer, including Activities, Fragments, and XML layouts.
- **ViewModel:** Connects the Model and View layers. Contains LiveData for the UI to observe and updates it based on changes in the data layer.


## Tags

- `android`
- `kotlin`
- `mvvm`
- `weather-app`
- `retrofit`
- `livedata`
- `viewmodel`
- `coroutines`

