package com.example.android.bakingapp.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.fragment.ExoFragment;

public class TransitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transit_activity);

        Intent intent = getIntent();
        String description = intent.getStringExtra("Description");
        String video = intent.getStringExtra("Video");
        String thumbnail = intent.getStringExtra("Thumbnail");

        Bundle bundle = new Bundle();
        bundle.putString("Description", description);
        bundle.putString("Video", video);
        bundle.putString("Thumbnail", thumbnail);

        if (savedInstanceState == null){

            ExoFragment fragment = new ExoFragment();
            fragment.setArguments(bundle);
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().add(R.id.lets_finish, fragment).commit();

        }

    }
}
