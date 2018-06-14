package com.example.android.bakingapp;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.android.bakingapp.model.Ingredients;
import com.example.android.bakingapp.model.Steps;

import java.util.List;


public class RecipeDetail extends AppCompatActivity implements LoaderManager.LoaderCallbacks {

    private RecyclerView mRecycle;
    private IngredientsAdapter mIngredAdapter;
    private static final int LOADER_ID = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_detail);

        Intent intent = getIntent();
        String name = intent.getStringExtra("Name");
        getSupportActionBar().setTitle(name);

        int id = intent.getIntExtra("Id", 0);

        List<Ingredients> ingredients =  intent.getParcelableArrayListExtra("In");
        List<Steps> steps = intent.getParcelableArrayListExtra("St");

        mRecycle = findViewById(R.id.ingredients_recycle_view);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecycle.setLayoutManager(manager);
        mRecycle.setHasFixedSize(true);

        mIngredAdapter = new IngredientsAdapter(this, ingredients);
        mRecycle.setAdapter(mIngredAdapter);

        LoaderManager managerLoader = getLoaderManager();
        managerLoader.initLoader(LOADER_ID, null, this );
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ID){
        return new IngredientsLoader(this);}
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        int id = loader.getId();
        if (id == LOADER_ID){
            List<Ingredients> ingredients = (List<Ingredients>) data;
           if (ingredients!=null && !ingredients.isEmpty()){
               mIngredAdapter.bindIngredients(ingredients);
           } else {
               Toast.makeText(this, getString(R.string.sorry_no_ingredients), Toast.LENGTH_SHORT).show();
           }
        }

    }

    @Override
    public void onLoaderReset(Loader loader) {
        int id = loader.getId();
        if (id == LOADER_ID){
        mIngredAdapter.bindIngredients(null);}

    }
}
