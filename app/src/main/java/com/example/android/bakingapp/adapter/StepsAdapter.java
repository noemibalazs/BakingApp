package com.example.android.bakingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.activity.ExoActivity;
import com.example.android.bakingapp.activity.RecipeDetail;
import com.example.android.bakingapp.fragment.ExoFragment;
import com.example.android.bakingapp.inter.MyInterface;
import com.example.android.bakingapp.model.Steps;


import java.util.ArrayList;
import java.util.List;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsViewHolder> {

    private List<Steps> mSteps;
    private Context mContext;

    private MyInterface mInterface;

    public StepsAdapter(Context context, List<Steps> steps, MyInterface inte){
        mContext = context;
        mSteps = steps;
        mInterface = inte;
    }

    @NonNull
    @Override
    public StepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.step_by_step, parent, false);
        return new StepsViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsViewHolder holder, final int position) {
        final Steps steps = mSteps.get(position);
        String shortDescription = steps.getShortDescription();
        holder.mDescription.setText(shortDescription);
        holder.mArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInterface.onHandler(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mSteps == null){ return 0;}
        return mSteps.size();
    }


    class StepsViewHolder extends RecyclerView.ViewHolder{

        private TextView mDescription;
        private ImageView mArrow;

        public StepsViewHolder(View itemView) {
            super(itemView);
            mDescription = itemView.findViewById(R.id.each_step_tv);
            mArrow = itemView.findViewById(R.id.click_image);

        }

    }

}