package com.example.android.bakingapp.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.adapter.IngredientsAdapter;
import com.example.android.bakingapp.adapter.StepsAdapter;
import com.example.android.bakingapp.model.Ingredients;
import com.example.android.bakingapp.model.Steps;
import com.example.android.bakingapp.widget.WidgetUpdateService;

import java.util.List;

public class RecipeDetailFragment extends Fragment {

    private boolean twoPanel;
    private RecyclerView mRecycleIngredients;
    private RecyclerView mRecycleSteps;
    private IngredientsAdapter mIngredientAdapter;
    private StepsAdapter mStepsAdapter;

    private String name;

    public RecipeDetailFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.recipe_fragment, container, false);

        if (root.findViewById(R.id.container)!=null){

            FragmentManager managerF = getActivity().getSupportFragmentManager();
            ExoActivityFragment fragment = new ExoActivityFragment();
            managerF.beginTransaction().add(R.id.container, fragment).commit();
            twoPanel = true;

        } else {
            twoPanel = false;
        }

        Bundle bundle = getArguments();

        if (bundle!=null) {

            List<Ingredients> ingredients = bundle.getParcelableArrayList("In");
            List<Steps> steps = bundle.getParcelableArrayList("St");
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

            mStepsAdapter = new StepsAdapter(getContext(), steps);
            mRecycleSteps.setAdapter(mStepsAdapter);

        }

        return root;
     }

}


