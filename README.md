WavesApp (Spotify Clone)
A basic Android application that clones core functionalities of Spotify, allowing users to browse and search for albums using the Spotify Web API.
This project was developed as part of a tutorial on GeeksforGeeks and utilizes the Client Credentials Flow for API authentication.
Features
API Integration: Fetches album data from the Spotify Web API.
Authentication: Implements the Spotify Client Credentials flow using Volley.
UI: Displays trending, popular, and recommended albums in horizontal RecyclerView lists.
Image Loading: Uses the Glide library for efficient image loading from URLs.
Search Functionality: Basic search input to navigate to a dedicated search activity.
View Binding: Uses Android View Binding for safer UI interactions.
Technologies Used
Language: Java
Networking: Volley
Image Loading: Glide
UI Components: RecyclerView, ImageView, EditText, Intent
API: Spotify Web API
Getting Started
Follow these instructions to set up the project locally.
Prerequisites
Android Studio (latest version recommended)
A Spotify Developer Account
A physical Android device or emulator with Android API level 24+
Installation
Clone the repository:
bash
git clone 

bash
github.com

bash


Open in Android Studio:
Open the cloned directory as an Android Studio project.
Configure Spotify API Credentials:
Go to the Spotify Developer Dashboard.
Create a new app to get your Client ID and Client Secret.
In your project, navigate to com.example.wavesapp.MainActivity.
Replace the placeholder strings in the generateToken() method with your actual credentials:
java
// Inside MainActivity.java
String clientId = "Enter your own client id"; // Replace with your Client ID
String clientSecret = "Enter your own client secret"; // Replace with your Client Secret

Sync Gradle:
Allow Android Studio to sync the project to download the dependencies (Volley and Glide).
Running the App
Connect your Android device or start an AVD emulator.
Click the Run 'app' button in Android Studio.
