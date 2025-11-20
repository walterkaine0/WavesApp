# WavesApp

A basic Android app that clones Spotify's core functionality, allowing users to browse and search albums using the Spotify Web API.

This project was developed as part of a GeeksforGeeks tutorial and uses the Client Credentials Flow for API authentication.

## Functionality

* **API Integration:** Retrieves album data from the Spotify Web API.
* **Authentication:** Implements the Spotify Client Credentials flow using the Volley library.
* **UI:** Displays popular, recommended, and trending albums in horizontal RecyclerView lists.
* **Image Loading:** Uses the Glide library to efficiently load images from URLs.
* **Search:** A basic search field for navigating to a specific search activity.
* **View Binding:** Uses Android view binding for safer user interface interactions.

## Technologies Used

* **Language:** Java
* **Network:** [Volley](developer.android.com)
* **Image Loading:** [Glide](bumptech.github.io)
* **UI Components:** `RecyclerView`, `ImageView`, `EditText`, `Intent`
* **API:** [Spotify Web API](developer.spotify.com)

## Getting Started

Follow these instructions to set up the project locally.

### Prerequisites

* Android Studio (latest version recommended)
* Spotify developer account
* A physical Android device or emulator with Android API level 24+

### Installation

1. **Clone the repository:**

```bash
git clone github.com
```

2. **Open in Android Studio:**
Open the cloned directory as an Android Studio project.

3. **Configure Spotify API credentials:**

* Go to the [Spotify Developer Dashboard](developer.spotify.com).
* Create a new app to get your `Client ID` and `Client Secret`.
* In your project, navigate to `com.example.wavesapp.MainActivity`. * Replace the placeholders in the `generateToken()` method with your actual data:

```java
// Inside MainActivity.java
String clientId = "Enter your own client id"; // Replace with your Client ID
String clientSecret = "Enter your own client secret"; // Replace with your Client Secret
```

4. **Sync Gradle:**
Allow Android Studio to sync the project to download dependencies (Volley and Glide).

### Running the app

* Connect your Android device or launch the AVD emulator.
* Click the **"Run 'app'** button in Android Studio.
