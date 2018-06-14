package com.example.android.bakingapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.android.bakingapp.model.Ingredients;
import com.example.android.bakingapp.util.IngredientsUtil;

import java.util.List;

public class IngredientsLoader extends AsyncTaskLoader<List<Ingredients>> {

    public IngredientsLoader(Context context){
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Ingredients> loadInBackground() {

        List<Ingredients> ingredients = IngredientsUtil.getAllIngredients(getContext());
        return ingredients;
    }
}
