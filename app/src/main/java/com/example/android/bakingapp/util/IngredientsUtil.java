package com.example.android.bakingapp.util;

import android.content.Context;
import android.content.res.AssetManager;

import com.example.android.bakingapp.model.Ingredients;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class IngredientsUtil {

    public IngredientsUtil(){}

    private static final String INGREDIENTS = "ingredients";
    private static final String QUANTITY = "quantity";
    private static final String MEASURE = "measure";
    private static final String INGRED = "ingredient";

    public static List<Ingredients> getAllIngredients(Context context){

        List<Ingredients> ingredients = new ArrayList<>();
        String json = null;

        String quantity = "";
        String measure = "";
        String ingred = "";

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

            for (int i=0; i< array.length();i++){

                JSONObject object = array.getJSONObject(i);

                JSONArray ingredientsArray = object.getJSONArray(INGREDIENTS);
                for (int j =0; j<ingredientsArray.length();j++){
                    JSONObject detail = ingredientsArray.getJSONObject(j);

                    quantity = detail.getString(QUANTITY);
                    measure = detail.getString(MEASURE);
                    ingred = detail.getString(INGRED);

                    Ingredients ing = new Ingredients(quantity, measure, ingred);
                    ingredients.add(ing);
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return ingredients;

    }
}
