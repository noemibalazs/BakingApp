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

import com.example.android.bakingapp.activity.ExoActivity;
import com.example.android.bakingapp.fragment.ExoActivityFragment;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Steps;


import java.util.ArrayList;
import java.util.List;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsViewHolder> {

    private List<Steps> mSteps;
    private Context mContext;


    public StepsAdapter(Context context, List<Steps> steps){
        mContext = context;
        mSteps = steps;
    }

    @NonNull
    @Override
    public StepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.step_by_step, parent, false);
        return new StepsViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsViewHolder holder, int position) {
        Steps steps = mSteps.get(position);
        String shortDescription = steps.getShortDescription();
        final String description = steps.getRecipeDescription();
        final String thumbnail = steps.getThumbnailUrl();
        final String video = steps.getVideoUrl();
        final int stepsId = steps.getIdSteps();
        holder.mDescription.setText(shortDescription);
        holder.mArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ExoActivity.class);
                intent.putExtra("Description", description);
                intent.putExtra("Thumbnail", thumbnail);
                intent.putExtra("Video", video);
                intent.putExtra("Id", stepsId);
                intent.putParcelableArrayListExtra("List", (ArrayList<? extends Parcelable>) mSteps);
                mContext.startActivity(intent);

                Bundle bundle = new Bundle();
                bundle.putString("Description", description);
                bundle.putString("Video", video);
                bundle.putString("Thumbnail", thumbnail);
                ExoActivityFragment fragment = new ExoActivityFragment();
                fragment.setArguments(bundle);

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
            mDescription =itemView.findViewById(R.id.each_step_tv);
            mArrow = itemView.findViewById(R.id.click_image);
        }
    }

}