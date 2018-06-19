package com.example.android.bakingapp;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.bakingapp.model.Ingredients;
import com.example.android.bakingapp.model.Steps;

import java.util.List;

public class RecipeDetail extends AppCompatActivity implements LoaderManager.LoaderCallbacks {

    private RecyclerView mRecycleIngredients;
    private RecyclerView mRecycleSteps;
    private IngredientsAdapter mIngredientAdapter;
    private StepsAdapter mStepsAdapter;
    private static final int LOADER_ID_ING = 3;
    private static final int LOADER_ID_ST = 7;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_details);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        LoaderManager managerLoader = getLoaderManager();
        managerLoader.initLoader(LOADER_ID_ING, null, this );

        LoaderManager managerLoad = getLoaderManager();
        managerLoad.initLoader(LOADER_ID_ST, null, this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }


    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ID_ING){
            return new IngredientsLoader(this);
        }
        else if (id == LOADER_ID_ST){
            return new StepsLoader(this);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        int id = loader.getId();

        if (id == LOADER_ID_ING){
            List<Ingredients> ingredients = (List<Ingredients>) data;
            if (ingredients!=null && !ingredients.isEmpty()){
                mIngredientAdapter.bindIngredients(ingredients);
            } else {
                Toast.makeText(this, getString(R.string.sorry_no_ingredients), Toast.LENGTH_SHORT).show();
            }
        } else if (id == LOADER_ID_ST){
            List<Steps> steps = (List<Steps>) data;
            if (steps!=null && !steps.isEmpty()){
                mStepsAdapter.bindSteps(steps);
            } else {
                Toast.makeText(this, getString(R.string.sorry_no_steps), Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onLoaderReset(Loader loader) {
        int id = loader.getId();

        if (id == LOADER_ID_ING){
            mIngredientAdapter.bindIngredients(null);
        } else if (id == LOADER_ID_ST){
            mStepsAdapter.bindSteps(null);

        }
    }
}