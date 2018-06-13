package com.example.android.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.bakingapp.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private Context mContext;
    private List<Recipe> mRecipes;

    public ImageAdapter (Context context){
        mContext = context;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(mContext).inflate(R.layout.loading_image, parent, false);
        return  new ImageViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Recipe recipe = mRecipes.get(position);
        String image = recipe.getImageRecipe();
        Picasso.with(holder.cake.getContext())
                .load(image)
                .error(R.drawable.cake)
                .placeholder(R.drawable.cake)
                .fit()
                .into(holder.cake);

    }

    @Override
    public int getItemCount() {
        if (mRecipes == null){ return 0;}
        return  mRecipes.size();
    }


    class ImageViewHolder extends RecyclerView.ViewHolder {

        private ImageView cake;

        private ImageViewHolder(View itemView) {
            super(itemView);
            cake = itemView.findViewById(R.id.cake_image);
        }
    }

    public void bindData(List<Recipe> rec){
        mRecipes = rec;
        notifyDataSetChanged();
    }
}
