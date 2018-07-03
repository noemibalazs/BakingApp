package com.example.android.bakingapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Steps;


import java.util.List;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsViewHolder> {

    private List<Steps> mSteps;
    private Context mContext;

    final private AdapterInterface mInterface;

    public interface AdapterInterface{
        void onHandler(int position, List<Steps> steps);
    }


    public StepsAdapter(Context context, List<Steps> steps, AdapterInterface inter){
        mContext = context;
        mSteps = steps;
        mInterface = inter;
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
        holder.mDescription.setText(shortDescription);


    }

    @Override
    public int getItemCount() {
        if (mSteps == null){ return 0;}
        return mSteps.size();
    }


    class StepsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mDescription;
        private int mPosition;

        public StepsViewHolder(View itemView) {
            super(itemView);
            mDescription = itemView.findViewById(R.id.each_step_tv);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            mInterface.onHandler(mPosition, mSteps);
        }
    }

}