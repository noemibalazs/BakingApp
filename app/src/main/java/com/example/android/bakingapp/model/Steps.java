package com.example.android.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Steps implements Parcelable {

    private int mIdSteps;
    private String mShortDescription;
    private String mDescription;
    private String mVideoUrl;
    private String mThumbnailUrl;

    public Steps(int id, String shortDescription, String description, String videoUrl, String thumbnailUrl){
        mIdSteps = id;
        mShortDescription = shortDescription;
        mDescription = description;
        mVideoUrl = videoUrl;
        mThumbnailUrl = thumbnailUrl;
    }


    public int getIdSteps(){
        return  mIdSteps;
    }

    public String getShortDescription(){
        return mShortDescription;
    }

    public String getRecipeDescription(){
        return mDescription;
    }

    public String getVideoUrl(){
        return mVideoUrl;
    }

    public String getThumbnailUrl(){
        return mThumbnailUrl;
    }

    private Steps(Parcel in) {
        mIdSteps = in.readInt();
        mShortDescription = in.readString();
        mDescription = in.readString();
        mVideoUrl = in.readString();
        mThumbnailUrl = in.readString();
    }

    public static final Creator<Steps> CREATOR = new Creator<Steps>() {
        @Override
        public Steps createFromParcel(Parcel in) {
            return new Steps(in);
        }

        @Override
        public Steps[] newArray(int size) {
            return new Steps[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mIdSteps);
        dest.writeString(mShortDescription);
        dest.writeString(mDescription);
        dest.writeString(mVideoUrl);
        dest.writeString(mThumbnailUrl);
    }
}
