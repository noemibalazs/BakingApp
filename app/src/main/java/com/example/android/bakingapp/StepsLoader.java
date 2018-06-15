package com.example.android.bakingapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.example.android.bakingapp.model.Steps;
import com.example.android.bakingapp.util.StepsUtil;

import java.util.List;

public class StepsLoader extends AsyncTaskLoader<List<Steps>>{

    public StepsLoader(Context context){
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Steps> loadInBackground() {

        List<Steps> steps = StepsUtil.getAllSteps(getContext());
        return steps;
    }
}
