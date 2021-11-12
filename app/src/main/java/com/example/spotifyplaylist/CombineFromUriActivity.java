package com.example.spotifyplaylist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

public class CombineFromUriActivity extends AppCompatActivity {
    private static final String CLIENT_ID = "e30929e731664b3f86b922d87115dc59";
    private static final String REDIRECT_URI = "http://localhost:8888/callback";
    private static final int REQUEST_CODE = 1337;
    String token;
    private SpotifyAppRemote mSpotifyAppRemote;
    private Intent a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combine_from_uri);
        setTitle("Combine From URI");
    }
    public void helpMenu(View v){
        Intent i = new Intent(this,UriHelpActivity.class);
        startActivity(i);
        Log.d("UriMenu","Uri Help Menu CLicked");

    }
    public void combineURI(View v){
    onStart();
    }
    @Override
    protected void onStart() {
        super.onStart();
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(CLIENT_ID)
                        .setRedirectUri(REDIRECT_URI)
                        .showAuthView(true)
                        .build();

        SpotifyAppRemote.connect(this, connectionParams,
                new Connector.ConnectionListener() {

                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        Log.d("MainActivity", "Connected! Yay!");

                        // Now you can start interacting with App Remote
                        //connected();

                    }

                    public void onFailure(Throwable throwable) {
                        Log.e("MyActivity", throwable.getMessage(), throwable);

                        // Something went wrong when attempting to connect! Handle errors here
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
        Log.d("loggedout","disconnected");
    }
    private void connected() {
        // Play a playlist
        //mSpotifyAppRemote.getUserApi().addToLibrary(((EditText)findViewById(R.id.editTextTextPersonName)).getText().toString());
    }
}