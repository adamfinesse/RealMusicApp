package com.example.spotifyplaylist;

import static com.spotify.sdk.android.auth.AccountsQueryParameters.CLIENT_ID;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

public class SuccessActivity extends AppCompatActivity {
    private static final String CLIENT_ID = "e76813a837234daca9626a030fb5cc9c";
    private static final String REDIRECT_URI = "http://localhost:8888/callback";
    private static final int REQUEST_CODE = 1337;
    private Intent a;
    private SpotifyAppRemote mSpotifyAppRemote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        setTitle("Home");
    }
    public void helpMenu(View v){
        //Button a = (Button)v;
        Intent i = new Intent(this, HelpActivity.class);
        startActivity(i);
        Log.d("helpmenu","help menu button clicked");
    }
    public void combineFromUriMenu(View v){
        Intent i = new Intent(this,CombineFromUriActivity.class);
        //i.putExtra()
        startActivity(i);
        Log.d("UriMenu","Combine From Uri Menu CLicked");
    }
    public void playView(View v){
        //Button a = (Button)v;
        Intent i = new Intent(this, PlayViewActivity.class);
        startActivity(i);
        Log.d("playView","view playlists button clicked");
    }
    public void loginSpotify(View v){
        //onStart();
//        final AuthorizationRequest request = new AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI)
//                .setScopes(new String[]{"user-read-private", "playlist-read", "playlist-read-private", "streaming","app-remote-control"})
//                //.setShowDialog(true)
//                .build();
//        AuthorizationClient.openLoginActivity(SuccessActivity.this, REQUEST_CODE, request); // This should try to authenticate with the 3 params, and by default if spotify isnt on the device it goes to web sign in.
        //a=AuthorizationClient.createLoginActivityIntent(SuccessActivity.this,request);
        //i.putExtra("response",AuthorizationClient.getResponse(Activity.RESULT_OK,i));
        //onActivityResult(REQUEST_CODE, Activity.RESULT_OK,a);


}
//    @Override
//    protected void onStart() {
//        super.onStart();
//        ConnectionParams connectionParams =
//                new ConnectionParams.Builder(CLIENT_ID)
//                        .setRedirectUri(REDIRECT_URI)
//                        .showAuthView(true)
//                        .build();
//
//        SpotifyAppRemote.connect(this, connectionParams,
//                new Connector.ConnectionListener() {
//
//                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
//                        mSpotifyAppRemote = spotifyAppRemote;
//                        Log.d("MainActivity", "Connected! Yay!");
//
//                        // Now you can start interacting with App Remote
//                        connected();
//
//                    }
//
//                    public void onFailure(Throwable throwable) {
//                        Log.e("MyActivity", throwable.getMessage(), throwable);
//
//                        // Something went wrong when attempting to connect! Handle errors here
//                    }
//                });
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
//    }
//
//    private void connected() {
//        // Play a playlist
//        //Library
//        mSpotifyAppRemote.getUserApi().getLibraryState("spotify:playlist:0X595F8fFxowklYH3fJSZ7?si=7e90dc56be564ad4");
//        mSpotifyAppRemote.getPlayerApi();
//    }
}
