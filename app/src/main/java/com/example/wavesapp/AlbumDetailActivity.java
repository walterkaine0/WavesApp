package com.example.wavesapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AlbumDetailActivity extends AppCompatActivity {

    private String albumID = "";
    private String albumImgUrl = null;
    private String albumName = null;
    private String artist = null;
    private String albumUrl = null;

    private TextView albumNameTV;
    private TextView artistTV;
    private ImageView albumIV;
    private ImageView playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_album_detail);

        albumID = getIntent().getStringExtra("id");
        albumIV = findViewById(R.id.idIVAlbum);
        albumImgUrl = getIntent().getStringExtra("img");
        albumName = getIntent().getStringExtra("name");
        artist = getIntent().getStringExtra("artist");
        albumUrl = getIntent().getStringExtra("albumUrl");

        Log.e("TAG", "album id is : " + albumID);

        albumNameTV = findViewById(R.id.idTVAlbumName);
        playButton = findViewById(R.id.playButton);
        artistTV = findViewById(R.id.idTVArtistName);


        albumNameTV.setText(albumName);
        artistTV.setText(artist);


        playButton.setOnClickListener(v -> {

            Uri uri = Uri.parse(albumUrl); // missing 'http://' will cause crash
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });


        Glide.with(this).load(albumImgUrl).into(albumIV);

        getAlbumTracks(albumID);
    }


    private String getToken() {
        return getSharedPreferences("MySharedPref", MODE_PRIVATE).getString("token", "Not Found");
    }


    private void getAlbumTracks(String albumID) {

        String url = "https://api.spotify.com/v1/albums/" + albumID + "/tracks";

        ArrayList<TrackModel> trackModels = new ArrayList<>();
        TrackAdapter trackAdapter = new TrackAdapter(trackModels, this);
        RecyclerView trackRV = findViewById(R.id.rvAlbumDetails);
        trackRV.setAdapter(trackAdapter);

        JsonObjectRequest trackObj = new JsonObjectRequest(
                Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray itemsArray = response.getJSONArray("items");
                        for (int i = 0; i < itemsArray.length(); i++) {
                            JSONObject itemObj = itemsArray.getJSONObject(i);
                            String trackName = itemObj.getString("name");
                            String id = itemObj.getString("id");
                            String trackArtist = itemObj.getJSONArray("artists")
                                    .getJSONObject(0)
                                    .getString("name");
                            trackModels.add(new TrackModel(trackName, trackArtist, id));
                        }
                        trackAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(
                        AlbumDetailActivity.this,
                        "Fail to get Tracks" + error, Toast.LENGTH_SHORT
                ).show()
        ) {

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", getToken());
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(trackObj);
    }
}