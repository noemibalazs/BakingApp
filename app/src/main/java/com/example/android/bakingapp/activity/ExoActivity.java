package com.example.android.bakingapp.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.UriMatcher;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.media.tv.TvView;
import android.net.Uri;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.model.Steps;
import com.google.android.exoplayer2.C;
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

import java.util.ArrayList;
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

    private Steps mSteps;
    private List<Steps> step;
    private int position;
    private long positionPlayer = 0;
    private int windowIndex = 0;
    private int index;
    private boolean play = true;

    private String mDescription;
    private String mVideo;
    private String mThumb;

    public static final String ID = "id";
    public static final String LONG = "position";
    public static final String WINDOW = "window";
    public static final String DESCRIPTION = "description";
    public static final String VIDEO = "video";
    public static final String THUMBNAIL = "thumbnail";
    public static final String LIST = "list";

    private static final String TAG = ExoActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exo);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Intent intent = getIntent();
        mDescription = intent.getStringExtra("Description");
        mThumb = intent.getStringExtra("Thumbnail");
        mVideo = intent.getStringExtra("Video");
        step = intent.getParcelableArrayListExtra("List");
        position = intent.getIntExtra("Id", 0);

        mPlayerView = findViewById(R.id.player_view);
        mText = findViewById(R.id.recipe_description_media);
        mText.setText(mDescription);

        mCard = findViewById(R.id.cv_recipe_description);
        mButton = findViewById(R.id.cv_button);

        mLeft = findViewById(R.id.left_click_image);
        mRight = findViewById(R.id.right_click_image);

        if (savedInstanceState!=null){
            index = savedInstanceState.getInt(ID);
            positionPlayer = savedInstanceState.getLong(LONG);
            windowIndex = savedInstanceState.getInt(WINDOW);
        }

        mRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position >= 0 && position < step.size() - 1) {
                    position++;
                    if (mExoPlayer != null) releasePlayer();
                    mSteps = step.get(position);
                    mRight.setEnabled(true);
                    mVideo = mSteps.getVideoUrl();
                    Log.v(TAG, "hhhhhhhhhhhhhhhhhh " + mVideo);
                    mThumb = mSteps.getThumbnailUrl();
                    Log.v(TAG, "HHHHHHHHHHHHHHHHHHHH " + mThumb);
                    mDescription = mSteps.getRecipeDescription();
                    mText.setText(mDescription);
                    if (!mThumb.isEmpty()) {
                        initializePlayer(Uri.parse(mThumb));
                    } else if (!mVideo.isEmpty()) {
                        initializePlayer(Uri.parse(mVideo));
                    }

                }
            }
        });

        mLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position > 0 && position <= step.size()) {
                    position--;
                    if (mExoPlayer != null)
                        releasePlayer();
                    mSteps = step.get(position);
                    mLeft.setEnabled(true);
                    mVideo = mSteps.getVideoUrl();
                    Log.v(TAG, "hhhhhhhhhhhhhhhhhh " + mVideo);
                    mThumb = mSteps.getThumbnailUrl();
                    Log.v(TAG, "HHHHHHHHHHHHHHHHHHHH " + mThumb);
                    mDescription = mSteps.getRecipeDescription();
                    mText.setText(mDescription);
                    if (!mVideo.isEmpty()) {
                        initializePlayer(Uri.parse(mVideo));
                    } else if (!mThumb.isEmpty()) {
                        initializePlayer(Uri.parse(mThumb));
                    }
                }

            }
        });

        initializeMediaSession();
        mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.question_mark));

        if (!mThumb.isEmpty()){
            initializePlayer(Uri.parse(mThumb));
        } else if(!mVideo.isEmpty()){
            initializePlayer(Uri.parse(mVideo));
        }  else if (TextUtils.isEmpty(mVideo) && TextUtils.isEmpty(mThumb)){
            initializePlayer(null);
            mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.question_mark));
        }

        resizePlayer(getResources().getConfiguration().orientation);
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
            mExoPlayer.setPlayWhenReady(play);
            if (positionPlayer != C.TIME_UNSET) mExoPlayer.seekTo(windowIndex, positionPlayer);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23){
            if ( !TextUtils.isEmpty(mThumb) || !TextUtils.isEmpty(mVideo)){
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || mExoPlayer == null){
            if ( !TextUtils.isEmpty(mThumb) || !TextUtils.isEmpty(mVideo)){
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        index = position;
        if (Util.SDK_INT <= 23){
        releasePlayer();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23){
            releasePlayer();
        }
    }

    private void releasePlayer(){
        if (mExoPlayer != null){
            positionPlayer = mExoPlayer.getCurrentPosition();
            windowIndex = mExoPlayer.getCurrentWindowIndex();
            play = mExoPlayer.getPlayWhenReady();
            mExoPlayer.release();
            mExoPlayer = null;
        }
        if (mMediaSession != null){
            mMediaSession.setActive(false);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mExoPlayer != null){
        outState.putLong(LONG, mExoPlayer.getCurrentPosition());
        outState.putInt(WINDOW, mExoPlayer.getCurrentWindowIndex());
        }

        outState.putInt(ID, index);
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
