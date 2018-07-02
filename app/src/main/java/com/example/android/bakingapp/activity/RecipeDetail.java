package com.example.android.bakingapp.activity;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.fragment.ExoActivityFragment;
import com.example.android.bakingapp.fragment.RecipeDetailFragment;
import com.example.android.bakingapp.model.Ingredients;
import com.example.android.bakingapp.model.Steps;
import com.example.android.bakingapp.widget.RecipeWidget;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetail extends AppCompatActivity {

    private ImageView mWidget;
    private boolean twoPanel;
    private List<Ingredients> ingredients;
    private String name;
    public static final String GSON = "json";
    public static final String NAME = "name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_details);

        mWidget = findViewById(R.id.iv_widget);

        Intent intent = getIntent();
        ingredients = intent.getParcelableArrayListExtra("In");
        List<Steps> steps = intent.getParcelableArrayListExtra("St");
        name = intent.getStringExtra("Name");

        if (savedInstanceState == null){

            Bundle bundle = new Bundle();
            bundle.putString("Name", name);
            bundle.putParcelableArrayList("In", (ArrayList<? extends Parcelable>) ingredients);
            bundle.putParcelableArrayList("St", (ArrayList<? extends Parcelable>) steps);

            RecipeDetailFragment fragment = new RecipeDetailFragment();
            fragment.setArguments(bundle);
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().add(R.id.replace, fragment).commit();
        }

        if (findViewById(R.id.container)!=null){
            ExoActivityFragment exoFragment = new ExoActivityFragment();
            FragmentManager managerFragment = getSupportFragmentManager();
            managerFragment.beginTransaction().add(R.id.container, exoFragment).commit();
            twoPanel = true;
        } else {
            twoPanel = false;
        }
    }


    public void click(View view) {
        Toast.makeText(this, "Widget has been added", Toast.LENGTH_SHORT).show();
        sendBroadcast();
        sharedPreference();
    }

    private void sendBroadcast(){
        Intent intent = new Intent(this, RecipeWidget.class);
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE\"");
        sendBroadcast(intent);
    }

    private void sharedPreference(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(ingredients);
        editor.putString(GSON, json);
        editor.putString(NAME, name);
        editor.apply();

    }
}