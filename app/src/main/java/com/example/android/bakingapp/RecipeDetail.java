package com.example.android.bakingapp;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.bakingapp.model.Ingredients;
import com.example.android.bakingapp.model.Steps;

import java.util.ArrayList;
import java.util.List;


public class RecipeDetail extends AppCompatActivity  {

    private boolean twoPanel;
    private RecyclerView mRecycleIngredients;
    private RecyclerView mRecycleSteps;
    private IngredientsAdapter mIngredientAdapter;
    private StepsAdapter mStepsAdapter;

    private ImageView mWidget;

    public static final String EXTRA_NAME = "com.example.android.bakingapp.EXTRA_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_details);

        final Intent intent = getIntent();
        final String name = intent.getStringExtra("Name");
        getSupportActionBar().setTitle(name);

        final List<Ingredients> ingredients = intent.getParcelableArrayListExtra("In");
        final List<Steps> steps = intent.getParcelableArrayListExtra("St");

        mWidget = findViewById(R.id.iv_widget);
        mWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RecipeDetail.this, "Widget has been added", Toast.LENGTH_SHORT).show();
            }
        });

        mRecycleIngredients = findViewById(R.id.ingredients_recycle_view);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecycleIngredients.setLayoutManager(manager);
        mRecycleIngredients.setHasFixedSize(true);

        mIngredientAdapter = new IngredientsAdapter(this, ingredients);
        mRecycleIngredients.setAdapter(mIngredientAdapter);

        mRecycleSteps = findViewById(R.id.steps_recycle_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecycleSteps.setLayoutManager(linearLayoutManager);
        mRecycleSteps.setHasFixedSize(true);

        mStepsAdapter = new StepsAdapter(this, steps);
        mRecycleSteps.setAdapter(mStepsAdapter);

        if (findViewById(R.id.container)!=null){

            FragmentManager managerF = getSupportFragmentManager();
            ExoActivityFragment fragment = new ExoActivityFragment();
            managerF.beginTransaction().add(R.id.container, fragment).commit();
            twoPanel = true;

        } else {
            twoPanel = false;
        }
    }

}