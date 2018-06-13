package com.example.android.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Recipe implements Parcelable{

    private int mIdRecipe;
    private String mName;
    private String mImage;
    private List<Ingredients> mIngredients;
    private List<Steps> mSteps;

    public Recipe(int idRecipe, String name, List<Ingredients> ingredients, List<Steps> steps, String image){
        mIdRecipe = idRecipe;
        mName = name;
        mIngredients = ingredients;
        mSteps = steps;
        mImage = image;
    }


    public int getIdRecipe(){
        return  mIdRecipe;
    }

    public String getNameRecipe (){
        return  mName;
    }

    public String getImageRecipe (){
        return  mImage;
    }

    public List<Ingredients> getListIngredients(){
        return  mIngredients;
    }

    public List<Steps> getListSteps (){
        return mSteps;
    }

    private Recipe(Parcel in) {
        mIdRecipe = in.readInt();
        mName = in.readString();
        mImage = in.readString();
        mIngredients = in.createTypedArrayList(Ingredients.CREATOR);
        mSteps = in.createTypedArrayList(Steps.CREATOR);
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mIdRecipe);
        dest.writeString(mName);
        dest.writeString(mImage);
        dest.writeTypedList(mIngredients);
        dest.writeTypedList(mSteps);
    }
}
