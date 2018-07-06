package com.example.android.bakingapp.activity;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.bakingapp.loader.ImageLoader;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.adapter.ImageAdapter;
import com.example.android.bakingapp.model.Recipe;

import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Recipe>>{

    private RecyclerView mRecycle;
    private ProgressBar mProgressBar;
    private ImageAdapter mAdapter;

    private static final int LOADER_ID = 9;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressBar = findViewById(R.id.progress_bar);
        mRecycle = findViewById(R.id.cake_recycle_view);

        final int columns = getResources().getInteger(R.integer.gallery_columns);
        mRecycle.setLayoutManager(new GridLayoutManager(this, columns));
        mRecycle.setHasFixedSize(true);

        mAdapter = new ImageAdapter(this);
        mRecycle.setAdapter(mAdapter);



        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo !=null && networkInfo.isConnected()){

            LoaderManager managerLoader = getLoaderManager();
            managerLoader.initLoader(LOADER_ID, null, this);
        } else {
            mProgressBar.setVisibility(View.GONE);

        }

    }

    @Override
    public Loader<List<Recipe>> onCreateLoader(int id, Bundle args) {
        return new ImageLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<Recipe>> loader, List<Recipe> data) {
        mProgressBar.setVisibility(View.GONE);

        if (data !=null && !data.isEmpty()){
                mAdapter.bindData(data);
            Toast.makeText(this, getString(R.string.loading_recipes), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.sorry), Toast.LENGTH_SHORT).show();
        } if (!isChecked()){
            Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        } else {
            Log.v(TAG, "Network is available");
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Recipe>> loader) {
        mAdapter.bindData(null);

    }

    public boolean isChecked(){
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info!=null && info.isConnected()) {
            return true;
        }
        return false;
    }

}
