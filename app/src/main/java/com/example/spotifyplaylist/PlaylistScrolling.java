package com.example.spotifyplaylist;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.spotifyplaylist.databinding.ActivityScrollingPlaylistBinding;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class PlaylistScrolling extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.spotifyplaylist.databinding.ActivityScrollingPlaylistBinding binding = ActivityScrollingPlaylistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Toolbar toolbar = binding.toolbar; ?
        //setSupportActionBar(toolbar);
        //CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        //toolBarLayout.setTitle(getTitle());

        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
    }
}