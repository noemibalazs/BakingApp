package com.example.android.bakingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;


public class RecipeDetail extends AppCompatActivity  {

    private boolean twoPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_details);

        Intent intent = getIntent();
        String name = intent.getStringExtra("Name");
        getSupportActionBar().setTitle(name);

        if (findViewById(R.id.container)!=null){

            FragmentManager manager = getSupportFragmentManager();
            ExoActivityFragment fragment = new ExoActivityFragment();
            manager.beginTransaction().add(R.id.container, fragment).commit();
            twoPanel = true;

        } else {
            twoPanel = false;
        }
    }

}