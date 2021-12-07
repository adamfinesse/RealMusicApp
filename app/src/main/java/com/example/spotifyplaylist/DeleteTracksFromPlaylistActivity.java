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

public class DeleteTracksFromPlaylistActivity extends AppCompatActivity {
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_tracks_from_playlist);
        q = Volley.newRequestQueue(this);
    }
}
