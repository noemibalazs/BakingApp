package com.example.android.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.model.Steps;

import java.util.List;

public class StepsFragmentAdapter extends RecyclerView.Adapter<StepsFragmentAdapter.StepsFragmentViewHolder> {

    private Context mContext;
    private List<Steps> mSteps;


    public StepsFragmentAdapter(Context context, List<Steps> steps){
        mContext = context;
        mSteps = steps;
    }

    @NonNull
    @Override
    public StepsFragmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View root = LayoutInflater.from(mContext).inflate(R.layout.step_by_step, parent, false);
        return new StepsFragmentViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsFragmentViewHolder holder, int position) {
        Steps steps = mSteps.get(position);
        final String description = steps.getRecipeDescription();
        String shortDescription = steps.getShortDescription();
        final String video = steps.getVideoUrl();
        final String thumbnail = steps.getThumbnailUrl();
        holder.mText.setText(shortDescription);
        holder.mArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    public class StepsFragmentViewHolder extends RecyclerView.ViewHolder{

        private TextView mText;
        private ImageView mArrow;

        public StepsFragmentViewHolder(View itemView) {
            super(itemView);
            mText = itemView.findViewById(R.id.each_step_tv);
            mArrow = itemView.findViewById(R.id.click_image);
        }
    }
}
