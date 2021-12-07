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
import java.util.ArrayList;
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
    private String addToPlaylistEndpoint;
    private String removeFromPlaylistEndpoint;
    private String getUserPlaylistsEndpoint = "https://api.spotify.com/v1/users/"+ getUserid()+"/playlists";
    private String getPlaylistEndPoint;
    //private ArrayList<String> playlistArray = new ArrayList<>();

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

    }

    public void addURItostack(View v) {
        if (((EditText) findViewById(R.id.editTextTextPersonName)).getText().toString().contains("playlist")) {
            String viewString = ((EditText) findViewById(R.id.editTextTextPersonName)).getText().toString();
            String[] splitString=viewString.split(":");
            Log.d("aaa",splitString[splitString.length-1]);
            getUrisFromPlaylist(splitString[splitString.length-1]);
            ((EditText) findViewById(R.id.editTextTextPersonName)).setText("");
            // make a string array with the uris from the playlist (max
        }
        if (!((EditText) findViewById(R.id.editTextTextPersonName)).getText().toString().equalsIgnoreCase("")) {
            uriStack.push(((EditText) findViewById(R.id.editTextTextPersonName)).getText().toString());
            ((EditText) findViewById(R.id.editTextTextPersonName)).setText("");

        }
    }

    public void removeLastURIfromstack(View v) {
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
        playlistID = getPlaylistID(playlistName);
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
        }
        else if(uriStack.isEmpty()){
            Log.d("stack empty", "the stack has no uris, add some and try again");
            Toast.makeText(getApplicationContext(),"the stack has no uris to add, add some and try again",Toast.LENGTH_LONG).show();
            return;
        }else {
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
                        //spotify:track:2MIBAmYwiuGoKUlpq9B9sZ https://open.spotify.com/track/2MIBAmYwiuGoKUlpq9B9sZ?si=76493e3262934de1
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
            Toast.makeText(getApplicationContext(),"Null token!, restart app to get a new auth token.",Toast.LENGTH_LONG).show();
            return;
        }else if(uriStack.isEmpty()){
            Log.d("stack empty", "the stack has no uris, add some and try again");
            Toast.makeText(getApplicationContext(),"the stack has no uris to add, add some and try again",Toast.LENGTH_LONG).show();
            return;
        }
        else {
            //RequestQueue q = Volley.newRequestQueue(this);
            JSONObject postData = new JSONObject();

//            if(findViewById(R.id.button9).getVisibility()==View.VISIBLE){
//                playlistID = getPlaylistID(playlistName);
//                //Log.
//            }
                try {
                    JSONArray uriJsonArray = new JSONArray();
                    while(!uriStack.isEmpty()){//maybe move to before/move uristack.peek inside here for better logic
                        uriJsonArray.put(uriStack.peek().toString());
                        uriStack.pop();
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
                                longLog(response.toString());
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
    public void requestDeleteTracksFromPlaylist(){
        if (getToken() == null) {
            Log.d("null token", "the token is null cant combine");
            Toast.makeText(getApplicationContext(),"Null token!, restart app to get a new auth token.",Toast.LENGTH_LONG).show();
            return;
        }else if(uriStack.isEmpty()){
            Log.d("stack empty", "the stack has no uris, add some and try again");
            Toast.makeText(getApplicationContext(),"the stack has no uris to delete, add some and try again",Toast.LENGTH_LONG).show();
            return;
        }
        else {
            //RequestQueue q = Volley.newRequestQueue(this);
            JSONObject deleteData = new JSONObject();

//            if(findViewById(R.id.button9).getVisibility()==View.VISIBLE){
//                playlistID = getPlaylistID(playlistName);
//                //Log.
//            }
            try {
                JSONArray uriTopJsonArray = new JSONArray();
                JSONArray uriOtherJsonArray = new JSONArray();
                while(!uriStack.isEmpty()){//maybe move to before/move uristack.peek inside here for better logic
                    if(uriStack.peek().toString().equals(deleteData.toString())){
                        uriStack.pop();
                    }
                    uriOtherJsonArray.put(uriStack.peek().toString());
                    uriStack.pop();
                }
                deleteData.put("uris", uriTopJsonArray);
                deleteData.put("position",null);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            final String requestBody = deleteData.toString();
            removeFromPlaylistEndpoint = "https://api.spotify.com/v1/playlists/"+playlistID+"/tracks";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.DELETE, removeFromPlaylistEndpoint, deleteData, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            longLog(response.toString());
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
    public void getUrisFromPlaylist(String playlistIDC){
        if (getToken() == null) {
            Log.d("null token", "the token is null cant combine");
            Toast.makeText(getApplicationContext(),"Null token!, restart app to get a new auth token.",Toast.LENGTH_LONG).show();
        } else {
            getPlaylistEndPoint= "https://api.spotify.com/v1/playlists/"+playlistIDC+"/tracks"; //?fields=limit,offset&offset=0&limit=100
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
                                    uriStack.add("spotify:track:"+listOfTracks.getJSONObject(i).getJSONObject("track").getString("id"));
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
    //use longLog to print out a full response as Log.d cuts the response short when printing.
    public static void longLog(String str) {
        if (str.length() > 4000) {
            Log.d("longlogResponse", str.substring(0, 4000));
            longLog(str.substring(4000));
        } else
            Log.d("longlogResponse", str);
    }
}