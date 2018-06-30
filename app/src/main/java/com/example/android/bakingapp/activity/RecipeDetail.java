package com.example.android.bakingapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.fragment.RecipeDetailFragment;
import com.example.android.bakingapp.model.Ingredients;
import com.example.android.bakingapp.model.Steps;
import com.example.android.bakingapp.widget.WidgetUpdateService;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetail extends AppCompatActivity {

    private ImageView mWidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_details);

        mWidget = findViewById(R.id.iv_widget);

        Intent intent = getIntent();
        List<Ingredients> ingredients = intent.getParcelableArrayListExtra("In");
        List<Steps> steps = intent.getParcelableArrayListExtra("St");
        String name = intent.getStringExtra("Name");

        Bundle bundle = new Bundle();
        bundle.putString("Name", name);
        bundle.putParcelableArrayList("In", (ArrayList<? extends Parcelable>) ingredients);
        bundle.putParcelableArrayList("St", (ArrayList<? extends Parcelable>) steps);

        RecipeDetailFragment fragment = new RecipeDetailFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.master_list_fragment, fragment).commit();


    }


    public void click(View view) {
        Toast.makeText(this, "Widget has been added", Toast.LENGTH_SHORT).show();
        WidgetUpdateService.updateWidget(this);
    }
}