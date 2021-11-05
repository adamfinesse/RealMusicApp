package com.example.spotifyplaylist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class PlayViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_view);
    }
    public void helpMenu2(View v){
        //Button a = (Button)v;
        Intent i = new Intent(this, HelpActivity2.class);
        startActivity(i);
        Log.d("helpmenu2","help menu 2 button clicked");
    }
}