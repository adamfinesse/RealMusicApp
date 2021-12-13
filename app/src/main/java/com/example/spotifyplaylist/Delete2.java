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

public class Delete2 extends DeleteActivity{
    private RequestQueue q;
    private String playlistID = returnPlaylistID();
    private ArrayList<String> playlistArray = getPlaylistArray();
    private String trackID;
    private String trackEndpoint = 	"https://api.spotify.com/v1/tracks/";
    private String removeFromPlaylistEndpoint;
    private ArrayList<String> tracksToDelete = new ArrayList<>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_from_entered_playlist);
        setTitle("Delete Track(s)");
        q = Volley.newRequestQueue(this);
    }
    public void getTracks(View v){
        if(((EditText) findViewById(R.id.editTextTextPersonName12)).getText().toString().contains("track")) {
            String viewString = ((EditText) findViewById(R.id.editTextTextPersonName12)).getText().toString();
            String[] splitString = viewString.split(":");
            trackID = splitString[splitString.length-1];
            tracksToDelete.add(trackID);
        }
    }
    public void deleteTracksFromPlaylist(View v){
        requestDeleteTracksFromPlaylist();
    }
    public void requestDeleteTracksFromPlaylist() {
        if (getToken() == null) {
            Log.d("null token", "the token is null cant combine");
            Toast.makeText(getApplicationContext(),"Null token!, restart app to get a new auth token.",Toast.LENGTH_LONG).show();
            return;
        }else if(playlistArray.isEmpty()){
            Log.d("list empty", "the list has no uris, add some and try again");
            Toast.makeText(getApplicationContext(),"the stack has no uris to delete, add some and try again",Toast.LENGTH_LONG).show();
            return;
        }
        else {
            //RequestQueue q = Volley.newRequestQueue(this);
            JSONArray postData = new JSONArray();
            for(int i = 0; i <playlistArray.size(); i++){
                if(playlistArray.contains(tracksToDelete.get(i))){
                    playlistArray.remove(playlistArray.indexOf(tracksToDelete.get(i)));
                }
            }
            try {
                JSONArray uriJsonArray = new JSONArray(playlistArray);
                for (int i = 0; i < playlistArray.size(); i++){
                    postData.getJSONObject(i).put("uris", uriJsonArray);
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
}
