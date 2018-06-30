package com.example.android.bakingapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Ingredients;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder> {

    private Context mContext;
    private List<Ingredients> mIngredients;



    public IngredientsAdapter(Context context, List<Ingredients> ingredients){
        mContext = context;
        mIngredients = ingredients;
    }

    @NonNull
    @Override
    public IngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.ingredients, parent, false);
        return new IngredientsViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsViewHolder holder, int position) {
        Ingredients ingredients = mIngredients.get(position);
        String quantity = ingredients.getQuantity();
        String measure = ingredients.getMeasure();
        String ing = ingredients.getIngredient();
        String wholeIngredient = quantity + " " + measure + " " + ing;
        holder.mText.setText(wholeIngredient);

    }

    @Override
    public int getItemCount() {
        if (mIngredients == null){ return 0;}
        return mIngredients.size();
    }


    class IngredientsViewHolder extends RecyclerView.ViewHolder{

        private TextView mText;

        public IngredientsViewHolder(View itemView) {
            super(itemView);
            mText = itemView.findViewById(R.id.ingredients_text);
        }
    }


}