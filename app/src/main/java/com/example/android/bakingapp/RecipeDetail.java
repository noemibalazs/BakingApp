package com.example.android.bakingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import com.example.android.bakingapp.model.Ingredients;
import com.example.android.bakingapp.model.Steps;

import java.util.List;

public class RecipeDetail extends AppCompatActivity  {

    private RecyclerView mRecycleIngredients;
    private RecyclerView mRecycleSteps;
    private IngredientsAdapter mIngredientAdapter;
    private StepsAdapter mStepsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_details);


        Intent intent = getIntent();
        String name = intent.getStringExtra("Name");
        getSupportActionBar().setTitle(name);

        final int id = intent.getIntExtra("Id", 0);

        final List<Ingredients> ingredients =  intent.getParcelableArrayListExtra("In");
        final List<Steps> steps = intent.getParcelableArrayListExtra("St");

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

    }


}