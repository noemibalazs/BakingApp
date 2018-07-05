package com.example.android.bakingapp.fragment;

import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.activity.ExoActivity;
import com.example.android.bakingapp.activity.RecipeDetail;
import com.example.android.bakingapp.adapter.IngredientsAdapter;
import com.example.android.bakingapp.adapter.StepsAdapter;
import com.example.android.bakingapp.inter.MyInterface;
import com.example.android.bakingapp.model.Ingredients;
import com.example.android.bakingapp.model.Steps;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RecipeDetailFragment extends Fragment implements MyInterface{

    private RecyclerView mRecycleIngredients;
    private RecyclerView mRecycleSteps;
    private IngredientsAdapter mIngredientAdapter;
    private StepsAdapter mStepsAdapter;

    public static final String NAME = "recipe_name";
    public static final String INGREDIENTS = "recipe_list_ingredients";
    public static final String STEPS = "recipe_list_steps";

    private List<Steps> mSteps;
    private List<Ingredients> mIngredients;
    private String mName;

    private Boolean isTablet ;

    private String description;
    private String video;
    private String thumbnail;
    private int stepsId;

    public RecipeDetailFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.recipe_fragment, container, false);

        isTablet = ((RecipeDetail)getActivity()).tabletMode();

        Bundle bundle = getArguments();

        if (bundle!=null) {

            mIngredients = bundle.getParcelableArrayList("In");
            mSteps = bundle.getParcelableArrayList("St");
            mName = bundle.getString("Name");

            getActivity().setTitle(mName);

            mRecycleIngredients = root.findViewById(R.id.ingredients_recycle_view);
            LinearLayoutManager inManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            mRecycleIngredients.setLayoutManager(inManager);
            mRecycleIngredients.setHasFixedSize(true);

            mIngredientAdapter = new IngredientsAdapter(getContext(), mIngredients);
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
    public void onSaveInstanceState(@NonNull Bundle bundle) {
        bundle.putString(NAME, mName);
        bundle.putParcelableArrayList(INGREDIENTS, (ArrayList<? extends Parcelable>) mIngredients);
        bundle.putParcelableArrayList(STEPS, (ArrayList<? extends Parcelable>) mSteps);
        super.onSaveInstanceState(bundle);

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState!=null){
            mIngredients = savedInstanceState.getParcelableArrayList(INGREDIENTS);
            mSteps = savedInstanceState.getParcelableArrayList(STEPS);
            mName = savedInstanceState.getString(NAME);
        }
    }

    @Override
    public void onHandler(int position) {

        Steps st = mSteps.get(position);
        description = st.getRecipeDescription();
        video = st.getVideoUrl();
        thumbnail = st.getThumbnailUrl();
        stepsId = st.getIdSteps();

        if (!isTablet){

        Intent service = new Intent(getContext(), ExoActivity.class);
        service.putExtra("Description", description);
        service.putExtra("Video", video);
        service.putExtra("Thumbnail", thumbnail);
        service.putExtra("Id", stepsId);
        service.putParcelableArrayListExtra("List", (ArrayList<? extends Parcelable>) mSteps);
        startActivity(service);

        } else {

            Bundle bundle = new Bundle();
            bundle.putString("Video", video);
            bundle.putString("Thumbnail", thumbnail);
            bundle.putString("Description", description);

            ExoFragment fragment = new ExoFragment();
            fragment.setArguments(bundle);
            FragmentManager manager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
            manager.beginTransaction().add(R.id.container, fragment).commit();
        }
    }
}


