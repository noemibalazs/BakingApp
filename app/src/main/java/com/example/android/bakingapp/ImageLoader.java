package com.example.android.bakingapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.util.RecipeUtil;

import java.util.List;

public class ImageLoader extends AsyncTaskLoader<List<Recipe>> {

    public ImageLoader(Context context){
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Recipe> loadInBackground() {

        List<Recipe> recipes = RecipeUtil.getAllDetail(getContext());
        return recipes;
    }

}
