package com.example.android.bakingapp.model;


import android.os.Parcel;
import android.os.Parcelable;

public class Ingredients implements Parcelable {

    private String mQuantity;
    private String mMeasure;
    private String mIngredient;

    public Ingredients(String quantity, String measure, String ingredient){
        mQuantity = quantity;
        mMeasure = measure;
        mIngredient = ingredient;
    }

    public String getQuantity(){
        return  mQuantity;
    }

    public String getMeasure(){
        return mMeasure;
    }

    public String getIngredient(){
        return mIngredient;
    }


    private Ingredients(Parcel in) {
        mQuantity = in.readString();
        mMeasure = in.readString();
        mIngredient = in.readString();
    }

    public static final Creator<Ingredients> CREATOR = new Creator<Ingredients>() {
        @Override
        public Ingredients createFromParcel(Parcel in) {
            return new Ingredients(in);
        }

        @Override
        public Ingredients[] newArray(int size) {
            return new Ingredients[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mQuantity);
        dest.writeString(mMeasure);
        dest.writeString(mIngredient);
    }
}
