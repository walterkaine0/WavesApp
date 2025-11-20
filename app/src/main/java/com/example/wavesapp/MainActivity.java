package com.example.wavesapp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RecyclerView albumsRV, popularAlbumsRV, trendingAlbumsRV;
    private EditText searchEdt;
    private boolean isTokenGenerated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();   // Link UI elements with code
        setupSearchView();   // Set listener for the search action
        generateToken();     // Get Spotify API token
    }

    private void initializeViews() {
        albumsRV = findViewById(R.id.idRVAlbums);
        popularAlbumsRV = findViewById(R.id.idRVPopularAlbums);
        trendingAlbumsRV = findViewById(R.id.idRVTrendingAlbums);
        searchEdt = findViewById(R.id.idEdtSearch);

        RecyclerView[] recyclerViews = {albumsRV, popularAlbumsRV, trendingAlbumsRV};
        for (RecyclerView rv : recyclerViews) {
            rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            rv.setHasFixedSize(true);
        }
    }

    private void setupSearchView() {
        searchEdt.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchTracks(searchEdt.getText().toString().trim());
                return true;
            }
            return false;
        });
    }

    private void searchTracks(String searchQuery) {
        if (!searchQuery.isEmpty()) {
            // Navigate to SearchActivity with query
            Intent intent = new Intent(this, SearchActivity.class);
            intent.putExtra("searchQuery", searchQuery);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Please enter a search term", Toast.LENGTH_SHORT).show();
        }
    }

    private void generateToken() {
        String url = "https://accounts.spotify.com/api/token";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String token = jsonObject.getString("access_token");

                        // Save token in SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                        sharedPreferences.edit().putString("token", "Bearer " + token).apply();

                        isTokenGenerated = true;
                        loadAllAlbumData(); // Load albums after token is ready
                    } catch (JSONException e) {
                        showError("Failed to parse token response: " + e.getMessage());
                    }
                },
                error -> showError("Failed to get token: " + error.getMessage())
        ) {
            @Override
            public Map<String, String> getHeaders() {
                String clientId = "f2b4e5cf79964a64bfc935473e259a82";
                String clientSecret = "26cd19468bd1409e928f290f9e82fc22";
                String credentials = clientId + ":" + clientSecret;
                String auth = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Basic " + auth);
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("grant_type", "client_credentials");
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

    private void loadAllAlbumData() {
        if (!isTokenGenerated) {
            showError("Token not generated yet");
            return;
        }

        loadAlbums(albumsRV, List.of(
                "3VammNdDFNfiSE1GdEbHbv", "1TiWFnZwyZ152viq7v9C31", "4n1tg05JN5EY0k7FRRcAir",
                "1VuwREfzRmsxw2Zu9NLYpo", "7A356O065CDhBZCA5tO6u5", "50pWd5nfE7IOO5oowQEPyO",
                "2o24x27LSoV6twxFtFl5Nb", "0cT1SQDE7wSh1eUJkGFXse"
        ));


        loadAlbums(popularAlbumsRV, List.of(
                "1TiWFnZwyZ152viq7v9C31", "50pWd5nfE7IOO5oowQEPyO", "0fSfkmx0tdPqFYkJuNX74a",
                "7sfzt6opjPYVupZDX0bMUB", "41SZeD5hrlYWJi9UNdhhGJ", "6RjLSkvZ3rH3wsMP8d7Zii",
                "3CAAG4KgURYpCxtd3H83tz"
        ));

        loadAlbums(trendingAlbumsRV, List.of(
                "4d4VDp3u8MYzgQkZXjuoSl", "2uoF8DSa55phMHJsF402nm", "6CNLfcQDhEtcu2Wr1ALPP0",
                "0QblNKhZ0y576RUoZ1MRoB", "6MQtWELG7aRX7CkAzQ6nLM", "5asi6xhIro5qMzaEojet0l",
                "1TP95xOGiWqdVOu4hGbuug"
        ));
    }

    private void loadAlbums(RecyclerView recyclerView, List<String> albumIds) {
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        if (token.isEmpty()) {
            showError("Authentication token is missing");
            return;
        }

        String url = "https://api.spotify.com/v1/albums?ids=" + String.join(",", albumIds);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        parseAlbumResponse(response, recyclerView);
                    } catch (Exception e) {
                        showError("Failed to parse album data: " + e.getMessage());
                    }
                },
                error -> showError("Failed to load albums: " + error.getMessage())
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", token);
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

    private void parseAlbumResponse(JSONObject response, RecyclerView recyclerView) {
        try {
            JSONArray albumArray = response.getJSONArray("albums");
            List<AlbumModel> albumList = new ArrayList<>();

            for (int i = 0; i < albumArray.length(); i++) {
                JSONObject albumObj = albumArray.getJSONObject(i);
                JSONArray artists = albumObj.getJSONArray("artists");
                JSONArray images = albumObj.getJSONArray("images");

                String artistName = (artists.length() > 0)
                        ? artists.getJSONObject(0).optString("name", "Unknown Artist")
                        : "Unknown Artist";

                String imageUrl = (images.length() > 1)
                        ? images.getJSONObject(1).optString("url", "")
                        : "";

                albumList.add(new AlbumModel(
                        albumObj.optString("album_type", "album"),
                        artistName,
                        albumObj.getJSONObject("external_ids").optString("upc", ""),
                        albumObj.getJSONObject("external_urls").optString("spotify", ""),
                        albumObj.optString("href", ""),
                        albumObj.optString("id", ""),
                        imageUrl,
                        albumObj.optString("label", ""),
                        albumObj.optString("name", "Unknown Album"),
                        albumObj.optInt("popularity", 0),
                        albumObj.optString("release_date", ""),
                        albumObj.optInt("total_tracks", 0),
                        albumObj.optString("type", "album")
                ));
            }

            recyclerView.setAdapter(new AlbumAdapter(albumList, this));

        } catch (JSONException e) {
            showError("JSON parsing error: " + e.getMessage());
        } catch (Exception e) {
            showError("Error parsing album data: " + e.getMessage());
        }
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}