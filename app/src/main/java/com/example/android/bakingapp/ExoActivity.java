package com.example.android.bakingapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.android.bakingapp.model.Steps;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

public class ExoActivity extends AppCompatActivity implements ExoPlayer.EventListener {

    private SimpleExoPlayerView mPlayerView;
    private SimpleExoPlayer mExoPlayer;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private TextView mText;

    private RelativeLayout.LayoutParams mParams;
    private CardView mCard;
    private CardView mButton;
    private ImageView mLeft;
    private ImageView mRight;

    private List<Steps> mStepsList;
    private Steps mSteps;

    private String mDescription;
    private String mVideo;
    private String mThumb;

    private static final String TAG = ExoActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exo);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Intent intent = getIntent();
        String description = intent.getStringExtra("Description");
        final String thumbnail = intent.getStringExtra("Thumbnail");
        final String video = intent.getStringExtra("Video");

        mPlayerView = findViewById(R.id.player_view);
        mText = findViewById(R.id.recipe_description_media);
        mText.setText(description);

        mCard = findViewById(R.id.cv_recipe_description);
        mButton = findViewById(R.id.cv_button);

        mLeft = findViewById(R.id.left_click_image);
        mRight = findViewById(R.id.right_click_image);

        mRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = intent.getIntExtra("Id", 0);
                id++;
                mSteps = mStepsList.get(id);
                if (id <= mStepsList.size() ){
                    mRight.setEnabled(true);
                    next(mSteps);
                }

            }
        });

        mLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        initializeMediaSession();

        if (!thumbnail.isEmpty()){
            initializePlayer(Uri.parse(thumbnail));
        } else {
            initializePlayer(Uri.parse(video));
        }

        resizePlayer(getResources().getConfiguration().orientation);
    }

    private void next(Steps next){

        mVideo = next.getVideoUrl();
        mThumb = next.getThumbnailUrl();
        mDescription = next.getRecipeDescription();
        mText.setText(mDescription);
        if (mThumb!=null){
            initializePlayer(Uri.parse(mThumb));
        } else {
            initializePlayer(Uri.parse(mVideo));
        }
    }

    private void resizePlayer(int orientation){
        if (orientation == Configuration.ORIENTATION_LANDSCAPE){
            mCard.setVisibility(View.GONE);
            mButton.setVisibility(View.GONE);
            mParams = (RelativeLayout.LayoutParams) mPlayerView.getLayoutParams();
            mParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            mParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mPlayerView.setLayoutParams(mParams);

            if (getSupportActionBar()!=null){
                getSupportActionBar().hide();
            }
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    |View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE) ;
        }

        else if (orientation == Configuration.ORIENTATION_PORTRAIT){
            mCard.setVisibility(View.VISIBLE);
            mButton.setVisibility(View.VISIBLE);
            mParams = (RelativeLayout.LayoutParams) mPlayerView.getLayoutParams();
            mParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            float f = mPlayerView.getResources().getDisplayMetrics().density;
            mParams.height = (int)(210 * f);
            mPlayerView.setLayoutParams(mParams);

            if (getSupportActionBar()!= null){
                getSupportActionBar().show();
            }

            this.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            );
        }
    }


    private void initializeMediaSession(){

        mMediaSession = new MediaSessionCompat(this, TAG);
        mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS|
                                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mMediaSession.setMediaButtonReceiver(null);

        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY|
                        PlaybackStateCompat.ACTION_PAUSE|
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS|
                        PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());
        mMediaSession.setCallback(new MySessionCallback());
        mMediaSession.setActive(true);

    }

    private void initializePlayer(Uri mediaUri){

        if (mExoPlayer == null){

            TrackSelector selector = new DefaultTrackSelector();
            LoadControl control = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, selector, control);
            mPlayerView.setPlayer(mExoPlayer);

            mExoPlayer.addListener(this);

            String userAgent = Util.getUserAgent(this, "BakingApp");
            MediaSource source = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    this, userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(source);
            mExoPlayer.setPlayWhenReady(true);

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int idMenu = item.getItemId();
        if (idMenu == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
        mMediaSession.setActive(false);
    }


    private void releasePlayer(){
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer=null;
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, mExoPlayer.getCurrentPosition(), 1f);
        } else if (playbackState == ExoPlayer.STATE_READY){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, mExoPlayer.getCurrentPosition(), 1f);
        }

        mMediaSession.setPlaybackState(mStateBuilder.build());

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }



    private class MySessionCallback extends MediaSessionCompat.Callback{

        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }
}
