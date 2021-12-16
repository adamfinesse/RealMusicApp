package com.example.spotifyplaylist;

import static com.example.spotifyplaylist.CombineFromUriActivity.longLog;
import static com.example.spotifyplaylist.SplashActivity.getToken;
import static com.example.spotifyplaylist.SplashActivity.getUserid;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.*;

public class DeleteActivity extends AppCompatActivity{
    private RequestQueue q;
    public String playlistID;
    private String playlistEndpoint = "https://api.spotify.com/v1/playlists/";
    private String getPlaylistEndPoint;
    private ArrayList<String> playlistArray = new ArrayList<>();
    private String trackEndpoint = 	"https://api.spotify.com/v1/tracks/";
    private ArrayList<String> tracksToDelete = new ArrayList<>();
    private final String getUserPlaylistsEndpoint = "https://api.spotify.com/v1/users/"+ getUserid()+"/playlists";
    private String playlistName = "madeWithRequests " + Integer.toString((int) (Math.random() * 10000));
    private String removeFromPlaylistEndpoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_tracks_from_playlist);
        getSupportActionBar().hide();
        setTitle("Delete Track(s)");
        q = Volley.newRequestQueue(this);
    }

    public void addURItoList(View v) {
        if (((EditText) findViewById(R.id.editTextTextPersonName12)).getText().toString().contains("playlist")) {
            String viewString = ((EditText) findViewById(R.id.editTextTextPersonName12)).getText().toString();
            String[] splitString=viewString.split(":");
            Log.d("aaa",splitString[splitString.length-1]);
            ((EditText) findViewById(R.id.editTextTextPersonName12)).setText("");
            // make a string array with the uris from the playlist (max
        }
        if (!((EditText) findViewById(R.id.editTextTextPersonName12)).getText().toString().equalsIgnoreCase("")) {
            tracksToDelete.add(((EditText) findViewById(R.id.editTextTextPersonName12)).getText().toString());
            ((EditText) findViewById(R.id.editTextTextPersonName12)).setText("");
        }
    }
    public String getPlaylistID(String PlaylistName){
        if (getToken() == null) {
            Log.d("null token", "the token is null cant combine");
            Toast.makeText(getApplicationContext(),"Null token!, restart app to get a new auth token.",Toast.LENGTH_LONG).show();
        } else {
            JSONObject postData = new JSONObject();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, getUserPlaylistsEndpoint, postData, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("getPlaylistResponse", response.toString());
                            try {
                                JSONArray listOfPlaylists= response.getJSONArray("items"); //get json array of playlists
                                for(int i=0;i<listOfPlaylists.length();i++){
                                    try {
                                        JSONObject playlist = listOfPlaylists.getJSONObject(i); //turn each index of the json array into a json object to interact with
                                        if (playlist.get("name").toString().equalsIgnoreCase(playlistName)) {
                                            playlistID=playlist.get("id").toString();
                                            break;
                                        }
                                    } catch(JSONException e){
                                        e.printStackTrace();
                                    }

                                }
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
        return playlistID;
    }
    public void deleteTracksFromPlaylist(View v){
        requestDeleteTracksFromPlaylist();
    }
    public void requestDeleteTracksFromPlaylist() {
        if (getToken() == null) {
            Log.d("null token", "the token is null");
            Toast.makeText(getApplicationContext(),"Null token!, restart app to get a new auth token.",Toast.LENGTH_LONG).show();
            return;
        }else if(tracksToDelete.isEmpty()){
            Log.d("list empty", "the list has no uris, add some and try again");
            Toast.makeText(getApplicationContext(),"the list has no uris to delete, add some and try again",Toast.LENGTH_LONG).show();
            return;
        }
        else {
            //RequestQueue q = Volley.newRequestQueue(this);
            JSONArray postData = new JSONArray();
            try {
                for (int i = 0; i<tracksToDelete.size(); i++){
                    JSONObject j = new JSONObject();
                    j.put("uri", tracksToDelete.get(i));
                    postData.put(j);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            final String requestBody = postData.toString();
            removeFromPlaylistEndpoint = "https://api.spotify.com/v1/playlists/"+playlistID+"/tracks";
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                    (Request.Method.DELETE, removeFromPlaylistEndpoint, postData, new Response.Listener<JSONArray>() {

                        @Override
                        public void onResponse(JSONArray response) {
                            longLog(response.toString());
                            Toast.makeText(getApplicationContext(),"Success!!! Check your spotify!",Toast.LENGTH_LONG).show();
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            longLog("ERROR");

                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", "Bearer " + getToken());
                    params.put("Content-Type", "application/json");

                    return params;
                }

                @Override
                public byte[] getBody() {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                                requestBody, "utf-8");
                        return null;
                    }
                }

            };

            // Access the RequestQueue through your singleton class.
            q.add(jsonArrayRequest);
        }
    }

    public void getPlaylistId(View v) {
        if (((EditText) findViewById(R.id.editT)).getText().toString().contains("playlist")) {
            String viewString = ((EditText) findViewById(R.id.editT)).getText().toString();
            String[] splitString = viewString.split(":");
            playlistEndpoint = playlistEndpoint + splitString[splitString.length - 1];
            playlistID = splitString[splitString.length - 1];
            ((EditText) findViewById(R.id.editT)).setText("");
            // make a string array with the uris from the playlist (max 100)
        }
    }

    public void deleteHelpMenu(View v) {
        Intent i = new Intent(this, DeleteHelpActivity.class);
        startActivity(i);
    }

}
