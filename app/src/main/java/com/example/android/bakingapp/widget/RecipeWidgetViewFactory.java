package com.example.android.bakingapp.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.activity.RecipeDetail;
import com.example.android.bakingapp.model.Ingredients;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.util.ArrayList;
import java.util.List;

public class RecipeWidgetViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private List<Ingredients> mIngredients = new ArrayList<>();
    private String name;

    public RecipeWidgetViewFactory( Context context){
        mContext = context;


    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String json = preferences.getString(RecipeDetail.GSON, "");
        name = preferences.getString(RecipeDetail.NAME, "");
        if (!json.equals("")){
            Gson gson = new Gson();
            mIngredients = gson.fromJson(json, new TypeToken<ArrayList<Ingredients>>(){}.getType());

        }

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public RemoteViews getViewAt(int position) {

        String whole = listIngredients(mIngredients);

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_ingredients);
        views.setTextViewText(R.id.widget_tv_name, name);
        views.setTextViewText(R.id.widget_ing_details, whole);


        return views;

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
