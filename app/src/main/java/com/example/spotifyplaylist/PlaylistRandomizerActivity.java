package com.example.spotifyplaylist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.spotifyplaylist.CombineFromUriActivity.longLog;
import static com.example.spotifyplaylist.SplashActivity.getToken;
import static com.example.spotifyplaylist.SplashActivity.getUserid;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlaylistRandomizerActivity extends AppCompatActivity {
    private RequestQueue q;
    private String playlistID;
    private String playlistEndpoint = "https://api.spotify.com/v1/playlists/";
    private String getPlaylistEndPoint;
    private ArrayList<String> playlistArray = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_randomizer);
    }
    public void randomizePlaylist(View v){
        getUrisFromPlaylist(playlistID);
    }
    public void getPlaylistId(View v){
        if (((EditText) findViewById(R.id.editTextTextPersonName3)).getText().toString().contains("playlist")) {
            String viewString = ((EditText) findViewById(R.id.editTextTextPersonName3)).getText().toString();
            String[] splitString=viewString.split(":");
            playlistEndpoint = playlistEndpoint+splitString[splitString.length-1];
            playlistID=splitString[splitString.length-1];
            ((EditText) findViewById(R.id.editTextTextPersonName3)).setText("");
            // make a string array with the uris from the playlist (max 100)
        }
    }
    public void getUrisFromPlaylist(String playlistIDC){
        if (getToken() == null) {
            Log.d("null token", "the token is null cant combine");
            Toast.makeText(getApplicationContext(),"Null token!, restart app to get a new auth token.",Toast.LENGTH_LONG).show();
        } else {
            getPlaylistEndPoint= "https://api.spotify.com/v1/playlists/"+playlistID+"/tracks"; //?fields=limit,offset&offset=0&limit=100
            JSONObject postData = new JSONObject();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, getPlaylistEndPoint, postData, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            longLog(response.toString());
                            try {
                                JSONArray listOfTracks= response.getJSONArray("items"); //get json array of tracks
                                Log.d("playlistLength",listOfTracks.length()+"");// the max length in 1 request is 100
                                for(int i=0;i<listOfTracks.length();i++){
                                    playlistArray.add("spotify:track:"+listOfTracks.getJSONObject(i).getJSONObject("track").getString("id"));
                                }
                                requestRandomizePlaylist();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO: Handle error

                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", "Bearer " + getToken());
                    params.put("Content-Type", "application/json");

                    return params;
                }
            };

            // Access the RequestQueue through your singleton class.

            q.add(jsonObjectRequest);

        }

    }
    public void requestRandomizePlaylist() {
        if (getToken() == null) {
            Log.d("null token", "the token is null cant combine");
            Toast.makeText(getApplicationContext(),"Null token!, restart app to get a new auth token.",Toast.LENGTH_LONG).show();
        } else {

        }
    }
}