package com.example.spotifyplaylist;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class CombineFromUriActivity extends AppCompatActivity {
    private static final String CLIENT_ID = "e30929e731664b3f86b922d87115dc59";
    private static final String REDIRECT_URI = "http://localhost:8888/callback";
    private static final int REQUEST_CODE = 1337;
    private Stack uriStack = new Stack();
    private RequestQueue q;
    private String playlistName = "madeWithRequests " + Integer.toString((int) (Math.random() * 10000));
    private String playlistID;


    private String playlistEndpoint = "https://api.spotify.com/v1/users/" + getUserid() + "/playlists";
    private String addToPlaylistEndpoint = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combine_from_uri);
        setTitle("Combine From URI");
        q = Volley.newRequestQueue(this);
    }

    public void helpMenu(View v) {
        Intent i = new Intent(this, UriHelpActivity.class);
        startActivity(i);
        Log.d("UriMenu", "Uri Help Menu CLicked");

    }

    public void addURItostack(View v) {
        if (!((EditText) findViewById(R.id.editTextTextPersonName)).getText().toString().equalsIgnoreCase("")) {
            uriStack.push(((EditText) findViewById(R.id.editTextTextPersonName)).getText().toString());
            ((EditText) findViewById(R.id.editTextTextPersonName)).setText("");
        }
    }

    public void removeURIfromstack(View v) {
        if (!uriStack.isEmpty())
            uriStack.pop();
    }

    public void clearStack(View v) {
        while (!uriStack.isEmpty())
            uriStack.pop();
    }

    public void combineURI(View v) {
        if (((Button) findViewById(R.id.button9)).getVisibility() == View.VISIBLE) {
            requestAddTracksToPlaylist();
        } else {
            requestCreatePlaylist();
        }
        //onStart();
    }

    public void toggleVisPlaylist(View v) {
        EditText playlistText = ((EditText) findViewById(R.id.editTextTextPersonName2));
        Button updatePlaylistButton = ((Button) findViewById(R.id.button9));
        if (playlistText.getVisibility() == View.INVISIBLE) {
            playlistText.setVisibility(View.VISIBLE);
            updatePlaylistButton.setVisibility(View.VISIBLE);

        } else {
            playlistText.setVisibility(View.INVISIBLE);
            updatePlaylistButton.setVisibility(View.INVISIBLE);
        }
    }

    public void updatePlaylistName(View v) {
        playlistName = ((EditText) findViewById(R.id.editTextTextPersonName2)).getText().toString();
        ((EditText) findViewById(R.id.editTextTextPersonName2)).setText("");
    }

    //    @Override
//    protected void onStop() {
//        super.onStop();
//        SpotifyAppRemote.disconnect(mSpotifyAppRemote);
//        Log.d("loggedout","disconnected");
//    }
    public void requestCreatePlaylist() {
        if (getToken() == null) {
            Log.d("null token", "the token is null cant combine");
            return;
        } else {
            //uriStack is a stack of all the strings entered into the method, not checked for validity just added as a string

            JSONObject postData = new JSONObject();
            Log.d("asdsa", getToken());
            try {
                postData.put("name", playlistName);
                postData.put("public", true);
                postData.put("collaborative", false);
                postData.put("description", "Made with Volley using spotify sdk");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            final String requestBody = postData.toString();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, playlistEndpoint, postData, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Response", response.toString());
                            try {
                                playlistID = response.get("id").toString();
                                Log.d("ResponseID", playlistID);
                                requestAddTracksToPlaylist();
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
            q.add(jsonObjectRequest);

        }
    }

    public void requestAddTracksToPlaylist() {
        if (getToken() == null) {
            Log.d("null token", "the token is null cant combine");
            return;
        } else {
            //RequestQueue q = Volley.newRequestQueue(this);
            JSONObject postData = new JSONObject();
            if (uriStack.peek().toString().contains("playlist")) {
                getUrisFromPlaylist();
                // make a string array with the uris from the playlist (max
            }
            if(findViewById(R.id.button9).getVisibility()==View.VISIBLE){
                playlistID = getPlaylistID(playlistName);
            }
                try {
                    String[] uriArray = new String[uriStack.size()];
                    JSONArray uriJsonArray = new JSONArray();
                    int c=0;
                    while(!uriStack.isEmpty()){//maybe move to before/move uristack.peek inside here for better logic
                        uriArray[c] = uriStack.peek().toString();
                        uriJsonArray.put(uriStack.peek().toString());
                        uriStack.pop();
                        c++;
                    }
                    postData.put("uris", uriJsonArray);
                    postData.put("position",null);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final String requestBody = postData.toString();
                addToPlaylistEndpoint = "https://api.spotify.com/v1/playlists/"+playlistID+"/tracks";
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.POST, addToPlaylistEndpoint, postData, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("Response", response.toString());
                                Toast.makeText(getApplicationContext(),"Success!!! Check your spotify!",Toast.LENGTH_LONG).show();
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
                q.add(jsonObjectRequest);
            }
        }
    public void getUrisFromPlaylist(){

    }
    public String getPlaylistID(String PlaylistName){
        return "";
    }
}