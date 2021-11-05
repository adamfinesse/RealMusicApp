package com.example.spotifyplaylist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class SuccessActivity extends AppCompatActivity {

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
}