package com.example.android.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Ingredients;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.util.RecipeUtil;


import java.util.List;

public class RecipeWidgetViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private List<Ingredients> mIngredients;
    private List<Recipe> recipes;

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
        Ingredients ingredients = mIngredients.get(position);
        String q = ingredients.getQuantity();
        String m = ingredients.getMeasure();
        String in = ingredients.getIngredient();

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.listview_ingredients);
        views.setTextViewText(R.id.widget_tv_name, name);
        views.setTextViewText(R.id.widget_tv_q, q);
        views.setTextViewText(R.id.widget_tv_m, m);
        views.setTextViewText(R.id.widget_tv_i, in);

        Intent intent = new Intent();
        views.setOnClickFillInIntent(R.layout.listview_ingredients, intent);

        return  views;
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

}
