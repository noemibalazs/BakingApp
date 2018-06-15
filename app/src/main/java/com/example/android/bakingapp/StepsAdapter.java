package com.example.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.model.Steps;

import java.net.PortUnreachableException;
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
        String description = steps.getRecipeDescription();
        String thumbnail = steps.getThumbnailUrl();
        String video = steps.getVideoUrl();
        int stepsId = steps.getIdSteps();
        holder.mDescription.setText(shortDescription);
        holder.mArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ExoActivity.class);
                mContext.startActivity(intent);
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

    public void bindSteps(List<Steps> st){
        mSteps = st;
        notifyDataSetChanged();
    }
}
