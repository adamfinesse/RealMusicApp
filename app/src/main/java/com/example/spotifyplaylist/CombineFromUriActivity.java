package com.example.spotifyplaylist;

import static com.example.spotifyplaylist.SplashActivity.getToken;
import static com.example.spotifyplaylist.SplashActivity.getUserid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class CombineFromUriActivity extends AppCompatActivity {
    private static final String CLIENT_ID = "e30929e731664b3f86b922d87115dc59";
    private static final String REDIRECT_URI = "http://localhost:8888/callback";
    private static final int REQUEST_CODE = 1337;
    private Stack uriStack = new Stack();
    //private String uri1;
    //private String uri2;
    //private String uri3;
    //private String uri4;
    //UserService a = new UserService();

    private String endpoint ="https://api.spotify.com/v1/users/"+getUserid()+"/playlists";
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
    public void addURItostack(View v) {
        if (!((EditText) findViewById(R.id.editTextTextPersonName)).getText().toString().equalsIgnoreCase("")){
            uriStack.push(((EditText) findViewById(R.id.editTextTextPersonName)).getText().toString());
            ((EditText) findViewById(R.id.editTextTextPersonName)).setText("");
        }
    }

    public void removeURIfromstack(View v) {
        if (!uriStack.isEmpty())
            uriStack.pop();
    }

    public void clearStack(View v) {
        while(!uriStack.isEmpty())
            uriStack.pop();
    }

    public void combineURI(View v){
        if(getToken()==null){
            Log.d("null token","the token is null cant combine");
            return;
        }
        else{
            //uriStack is a stack of all the strings entered into the method, not checked for validity just added as a string
            RequestQueue q = Volley.newRequestQueue(this);
            JSONObject postData = new JSONObject();
            Log.d("asdsa",getToken());
            try{
                postData.put("name","madeWithRequests");
                postData.put("public",true);
                postData.put("description","Made with Volley using spotify sdk");
                postData.put("Authorization",getToken());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, endpoint, postData, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            ((TextView)findViewById(R.id.textView10)).setText("Response: " + response.toString());
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO: Handle error

                        }
                    });

            // Access the RequestQueue through your singleton class.
            q.add(jsonObjectRequest);

        }
    //onStart();
    }
//    @Override
//    public Map<String, String> getHeaders() throws AuthFailureError {
//        Map<String, String>  params = new HashMap<String, String>();
//        params.put("User-Agent", "Nintendo Gameboy");
//        params.put("Accept-Language", "fr");
//
//        return params;
//    }
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
//                        //connected();
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
//        Log.d("loggedout","disconnected");
//    }
//    private void connected() {
//        // Play a playlist
//        //mSpotifyAppRemote.getUserApi().addToLibrary(((EditText)findViewById(R.id.editTextTextPersonName)).getText().toString());
//    }
//    public newPlaylist(){
//
//    }
}