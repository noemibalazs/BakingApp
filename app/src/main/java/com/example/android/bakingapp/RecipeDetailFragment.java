package com.example.android.bakingapp;


import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.model.Ingredients;
import com.example.android.bakingapp.model.Steps;

import java.util.List;
import java.util.Objects;


public class RecipeDetailFragment extends Fragment {

    private RecyclerView mRecycleIngredients;
    private RecyclerView mRecycleSteps;
    private IngredientsAdapter mIngredientAdapter;
    private StepsAdapter mStepsAdapter;


    public RecipeDetailFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.recipe_fragment, container, false);

        Bundle bundle = getArguments();

        if (bundle!=null) {

            final List<Ingredients> ingredients = bundle.getParcelableArrayList("In");
            final List<Steps> steps = bundle.getParcelableArrayList("St");

            mRecycleIngredients = root.findViewById(R.id.ingredients_recycle_view);
            LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            mRecycleIngredients.setLayoutManager(manager);
            mRecycleIngredients.setHasFixedSize(true);

            mIngredientAdapter = new IngredientsAdapter(getContext(), ingredients);
            mRecycleIngredients.setAdapter(mIngredientAdapter);

            mRecycleSteps = root.findViewById(R.id.steps_recycle_view);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            mRecycleSteps.setLayoutManager(linearLayoutManager);
            mRecycleSteps.setHasFixedSize(true);

            mStepsAdapter = new StepsAdapter(getContext(), steps);
            mRecycleSteps.setAdapter(mStepsAdapter);

        }
            return root;

    }
}
