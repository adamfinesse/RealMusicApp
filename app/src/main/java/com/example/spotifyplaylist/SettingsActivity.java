package com.example.spotifyplaylist;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;



public class SettingsActivity extends AppCompatActivity  {
    SwitchCompat nightModeSwitch;
    SharedPreferences sharedPreferences = null;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("settingsMenu","settings menu create started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().hide();

        // testing code---------------
        nightModeSwitch = findViewById(R.id.nightModeSwitch);

        sharedPreferences = getSharedPreferences("night",0);
        Boolean booleanValue = sharedPreferences.getBoolean("night_mode",true);
        if (booleanValue){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            nightModeSwitch.setChecked(true);

        }

        nightModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                nightModeSwitch.setChecked(true);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("night_mode",true);
                editor.apply();

            }else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                nightModeSwitch.setChecked(false);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("night_mode",false);
                editor.apply();

            }
        });
        Log.d("settingsMenu","settings menu created");

        // end of test code------------------

    }


}
