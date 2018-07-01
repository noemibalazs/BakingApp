package com.example.android.bakingapp.activity;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.fragment.ExoActivityFragment;
import com.example.android.bakingapp.model.Steps;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class ExoActivity extends AppCompatActivity {

    private boolean isTablet = true;
    private CardView mButton;
    private ImageView mLeft;
    private ImageView mRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exo);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String description = intent.getStringExtra("Description");
        String thumbnail = intent.getStringExtra("Thumbnail");
        String video = intent.getStringExtra("Video");
        List<Steps> step = intent.getParcelableArrayListExtra("List");
        int position = intent.getIntExtra("Id", 0);

        Bundle bundle = new Bundle();
        bundle.putString("Description", description);
        bundle.putString("Video", video);
        bundle.putString("Thumbnail", thumbnail);
        bundle.putInt("Id", position);
        bundle.putParcelableArrayList("List", (ArrayList<? extends Parcelable>) step);

        ExoActivityFragment fragment = new ExoActivityFragment();
        fragment.setArguments(bundle);
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.move, fragment).commit();

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


}
