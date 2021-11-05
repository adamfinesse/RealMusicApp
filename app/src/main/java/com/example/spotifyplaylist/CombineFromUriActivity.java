package com.example.spotifyplaylist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class CombineFromUriActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combine_from_uri);
    }
    public void helpMenu(View v){
        Intent i = new Intent(this,UriHelpActivity.class);
        startActivity(i);
        Log.d("UriMenu","Uri Help Menu CLicked");

    }
}