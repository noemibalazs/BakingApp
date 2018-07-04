package com.example.android.bakingapp.fragment;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.activity.ExoActivity;
import com.example.android.bakingapp.activity.RecipeDetail;
import com.example.android.bakingapp.activity.TransitActivity;
import com.example.android.bakingapp.adapter.IngredientsAdapter;
import com.example.android.bakingapp.adapter.StepsAdapter;
import com.example.android.bakingapp.inter.MyInterface;
import com.example.android.bakingapp.model.Ingredients;
import com.example.android.bakingapp.model.Steps;


import java.util.ArrayList;
import java.util.List;

public class RecipeDetailFragment extends Fragment implements MyInterface{

    private RecyclerView mRecycleIngredients;
    private RecyclerView mRecycleSteps;
    private IngredientsAdapter mIngredientAdapter;
    private StepsAdapter mStepsAdapter;

    private List<Steps> mSteps;
    private Boolean isTablet;

    public RecipeDetailFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.recipe_fragment, container, false);

        Bundle bundle = getArguments();

        isTablet = ((RecipeDetail)getActivity()).tabletMode();

        if (bundle!=null) {

            List<Ingredients> ingredients = bundle.getParcelableArrayList("In");
            mSteps = bundle.getParcelableArrayList("St");
            String name = bundle.getString("Name");

            getActivity().setTitle(name);

            mRecycleIngredients = root.findViewById(R.id.ingredients_recycle_view);
            LinearLayoutManager inManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            mRecycleIngredients.setLayoutManager(inManager);
            mRecycleIngredients.setHasFixedSize(true);

            mIngredientAdapter = new IngredientsAdapter(getContext(), ingredients);
            mRecycleIngredients.setAdapter(mIngredientAdapter);

            mRecycleSteps = root.findViewById(R.id.steps_recycle_view);
            LinearLayoutManager stManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            mRecycleSteps.setLayoutManager(stManager);
            mRecycleSteps.setHasFixedSize(true);

            mStepsAdapter = new StepsAdapter(getContext(), mSteps, this);
            mRecycleSteps.setAdapter(mStepsAdapter);

        }

        return root;
     }

    @Override
    public void onHandler(int position) {
        Steps step = mSteps.get(position);
        String description = step.getRecipeDescription();
        String thumbnail = step.getThumbnailUrl();
        String video = step.getVideoUrl();
        int positionSteps = step.getIdSteps();

        if (!isTablet){
            Intent intent = new Intent(getContext(), ExoActivity.class);
            intent.putParcelableArrayListExtra("List", (ArrayList<? extends Parcelable>) mSteps);
            intent.putExtra("Description", description);
            intent.putExtra("Video", video);
            intent.putExtra("Thumbnail", thumbnail);
            intent.putExtra("Id", positionSteps);
            startActivity(intent);

        } else {
            Intent service = new Intent(getContext(), TransitActivity.class);
            service.putExtra("Description", description);
            service.putExtra("Video", video);
            service.putExtra("Thumbnail", thumbnail);
            startActivity(service);
        }

    }
}


