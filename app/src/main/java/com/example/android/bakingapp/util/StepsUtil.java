package com.example.android.bakingapp.util;

import android.content.Context;
import android.content.res.AssetManager;

import com.example.android.bakingapp.model.Steps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class StepsUtil {

    public StepsUtil(){}

    private static final String ID = "id";
    private static final String STEPS = "steps";
    private static final String SHORT = "shortDescription";
    private static final String DESCRIPTION = "description";
    private static final String VIDEO = "videoURL";
    private static final String THUMBNAIL = "thumbnailURL";;

    public static List<Steps> getAllSteps(Context context){

        String json = null;

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

        try{

            JSONArray array = new JSONArray(json);

            for (int i=0; i<array.length(); i++){
                JSONObject object = array.getJSONObject(i);
                List<Steps> listSteps = new ArrayList<>();

                JSONArray steps = object.getJSONArray(STEPS);
                for (int k =0; k < steps.length(); k++){
                    JSONObject stepsObject = steps.getJSONObject(k);

                    idSteps = stepsObject.getInt(ID);
                    shortDescription = stepsObject.getString(SHORT);
                    description = stepsObject.optString(DESCRIPTION);
                    video = stepsObject.getString(VIDEO);
                    thumbnail = stepsObject.getString(THUMBNAIL);

                    Steps st = new Steps(idSteps, shortDescription, description, video, thumbnail);
                    listSteps.add(st);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getAllSteps(context);

    }
}
