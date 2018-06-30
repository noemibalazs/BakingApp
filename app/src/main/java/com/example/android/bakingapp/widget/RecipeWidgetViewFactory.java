package com.example.android.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Ingredients;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.util.RecipeUtil;


import java.util.ArrayList;
import java.util.List;

public class RecipeWidgetViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private List<Ingredients> mIngredients = new ArrayList<>();
    private List<Recipe> recipes = new ArrayList<>();

    public RecipeWidgetViewFactory( Context context){
        mContext = context;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mIngredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        recipes = RecipeUtil.getAllDetail(mContext);
        if (recipes!=null){

        Recipe recipe = recipes.get(position);
        String name = recipe.getNameRecipe();
        mIngredients = recipe.getListIngredients();
        String list = listIngredients(mIngredients);

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.listview_ingredients);
        views.setTextViewText(R.id.widget_tv_name, name);
        views.setTextViewText(R.id.widget_ing_details, list);

        Intent fillInIntent = new Intent();
        views.setOnClickFillInIntent(R.id.widget_ing_details, fillInIntent);

        return views;
        }
        return null;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


    public String listIngredients (List<Ingredients> ingredients){
        StringBuilder builder = new StringBuilder();
        for (int i=0; i < ingredients.size(); i++){
            Ingredients ing = ingredients.get(i);
            String q = ing.getQuantity();
            String m = ing.getMeasure();
            String iT = ing.getIngredient();
            builder.append("\n");
            builder.append(" ").append(q).append(" ").append(m).append(" ").append(iT);
        }
        return  builder.toString();
    }

}
