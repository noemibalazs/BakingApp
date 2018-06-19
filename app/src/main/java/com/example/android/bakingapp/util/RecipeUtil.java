package com.example.android.bakingapp.util;


import android.content.Context;
import android.content.res.AssetManager;

import com.example.android.bakingapp.model.Ingredients;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Steps;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RecipeUtil {

    public RecipeUtil(){}

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String INGREDIENTS = "ingredients";
    private static final String QUANTITY = "quantity";
    private static final String MEASURE = "measure";
    private static final String INGRED = "ingredient";
    private static final String STEPS = "steps";
    private static final String SHORT = "shortDescription";
    private static final String DESCRIPTION = "description";
    private static final String VIDEO = "videoURL";
    private static final String THUMBNAIL = "thumbnailURL";
    private static final String IMAGE = "image";


    public static List<Recipe> getAllDetail(Context context){

        List<Recipe> recipes = new ArrayList<>();
        String json = null;

        int idRecipe;
        String name = "";
        String image = "";
        String quantity = "";
        String measure = "";
        String ingred = "";
        int idSteps;
        String shortDescription = "";
        String description = "";
        String video = "";
        String thumbnail = "";


        try {
            AssetManager manager = context.getAssets();
            InputStream inputStream = manager.open("baking.json");
            int size = inputStream.available();
            byte [] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        try {

            JSONArray array = new JSONArray(json);

            for (int i=0; i< array.length(); i++){

                List<Ingredients> ingredientsList = new ArrayList<>();
                List<Steps> stepsList = new ArrayList<>();

                JSONObject object = array.getJSONObject(i);

                idRecipe = object.getInt(ID);
                name = object.getString(NAME);
                image = object.optString(IMAGE);

                JSONArray ingredients = object.getJSONArray(INGREDIENTS);

                for (int j =0; j<ingredients.length(); j++){

                    JSONObject ingredObject = ingredients.getJSONObject(j);

                    quantity = ingredObject.getString(QUANTITY);
                    measure = ingredObject.getString(MEASURE);
                    ingred = ingredObject.getString(INGRED);

                    Ingredients hozz = new Ingredients(quantity, measure, ingred);
                    ingredientsList.add(hozz);
                }

                JSONArray steps = object.getJSONArray(STEPS);
                for (int k =0; k < steps.length(); k++){
                    JSONObject stepsObject = steps.getJSONObject(k);

                    idSteps = stepsObject.getInt(ID);
                    shortDescription = stepsObject.getString(SHORT);
                    description = stepsObject.optString(DESCRIPTION);
                    video = stepsObject.getString(VIDEO);
                    thumbnail = stepsObject.getString(THUMBNAIL);

                    Steps st = new Steps(idSteps, shortDescription, description, video, thumbnail);
                    stepsList.add(st);
                }


                Recipe rec = new Recipe(idRecipe, name,  ingredientsList, stepsList, image);
                recipes.add(rec);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return recipes;
    }

}
