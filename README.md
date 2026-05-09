A university project created as part of a coursework.
The mobile app is designed for couriers and dispatchers.
Main features:
1. Authorization (courier / dispatcher)
2. Map with geolocation and route planning to a specified destination
3. Selection of transportation methods
4. Chat with the client
5. Courier profile
6. Shared chats for communication between the client, courier, and dispatcher
7. Dispatcher scheduling

Almost all of the dispatcher's functionality is not functional and is presented as a concept. Almost all of the functionality presented is implemented for the courier. The principle of the app's operation with couriers is borrowed from the Samokat courier company. Orders should automatically be assigned to a free courier who is assigned to the area from which the order originated. The app accepts coordinates for MapKit and the order ID for establishing a communication channel with the client.
The program was developed not for commercial use but for practice.

com.example.marsi
|-- data
│ |-- CourierRepository.kt       // Interface and InMemory implementation
|-- model
| |-- Courier.kt                 // Courier model (+ CourieStatus enum)
|-- MainActivity.kt              // Main screen with map
|-- MainViewModel.kt             // ViewModel for the main screen
|-- MainViewModelFactory.kt      // Factory for MainViewModel
|-- LoginActivity.kt             // Login screen
|-- LoginViewModel.kt            // ViewModel for login
|-- LoginViewModelFactory.kt     // Factory for LoginViewModel
|-- EmptyDispatcherActivity.kt   // Dispatcher stub
|-- ProfileActivity.kt           // Courier profile screen
|-- BuildConfig                   // MAPKIT_API_KEY
