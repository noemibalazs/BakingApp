package com.example.android.bakingapp.fragment;

import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.activity.RecipeDetail;
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


public class ExoFragment extends Fragment implements ExoPlayer.EventListener {

    private SimpleExoPlayerView mPlayerView;
    private SimpleExoPlayer mExoPlayer;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;

    private String thumbnail;
    private String video;
    private String description;

    private RelativeLayout.LayoutParams mParams;

    private TextView mText;
    private CardView mCard;
    private long positionPlayer = 0;
    private boolean play = true;

    public static final String VIDEO = "video";
    public static final String THUMBNAIL = "thumbnail";
    public static final String DESCRIPTION = "description";
    public static final String LONG = "position";

    private Boolean isTablet;

    private static final String TAG = ExoFragment.class.getSimpleName();

    public ExoFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.exo_fragment, container, false);

        mPlayerView = root.findViewById(R.id.p_view);
        mText = root.findViewById(R.id.recipe_media);
        mCard = root.findViewById(R.id.cv_description);

        mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.question_mark));

        isTablet = ((RecipeDetail)getActivity()).tabletMode();

        Bundle bundle = getArguments();

        if (bundle != null){
           description = bundle.getString("Description");
           thumbnail = bundle.getString("Thumbnail");
           video = bundle.getString("Video");

           mText.setText(description);

           initializeMediaSession();

           if (isTablet){
                 resizePlayer(getResources().getConfiguration().orientation);}

            if (!thumbnail.isEmpty()) {
                initializePlayer(Uri.parse(thumbnail));
            } else if (!video.isEmpty()) {
                    initializePlayer(Uri.parse(video));
            } else if (TextUtils.isEmpty(video) && TextUtils.isEmpty(thumbnail)){
                initializePlayer(null);
                mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.question_mark));
            }
        }
        return root;
    }

    private void initializeMediaSession(){

        mMediaSession = new MediaSessionCompat(getActivity(), TAG);
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
        mMediaSession.setCallback(new ExoFragment.MySessionCallback());
        mMediaSession.setActive(true);

    }


    private void initializePlayer(Uri mediaUri){

        if (mExoPlayer == null){

            TrackSelector selector = new DefaultTrackSelector();
            LoadControl control = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), selector, control);
            mPlayerView.setPlayer(mExoPlayer);

            mExoPlayer.addListener(this);

            String userAgent = Util.getUserAgent(getActivity(), "BakingApp");
            MediaSource source = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    (getActivity()), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(source);
            mExoPlayer.setPlayWhenReady(play);
            if (positionPlayer != C.TIME_UNSET) mExoPlayer.seekTo(positionPlayer);

        }
    }

    private void resizePlayer(int orientation){
        if (orientation == Configuration.ORIENTATION_PORTRAIT){
            mCard.setVisibility(View.VISIBLE);
            mParams = (RelativeLayout.LayoutParams) mPlayerView.getLayoutParams();
            mParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            float f = mPlayerView.getResources().getDisplayMetrics().density;
            mParams.height = (int)(430 * f);
            mPlayerView.setLayoutParams(mParams);

            getActivity().getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE){
            mCard.setVisibility(View.VISIBLE);
            mParams = (RelativeLayout.LayoutParams) mPlayerView.getLayoutParams();
            mParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            float f = mPlayerView.getResources().getDisplayMetrics().density;
            mParams.height = (int)(430 * f);
            mPlayerView.setLayoutParams(mParams);

            getActivity().getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState!=null){
            description = savedInstanceState.getString(DESCRIPTION);
            video = savedInstanceState.getString(VIDEO);
            thumbnail = savedInstanceState.getString(THUMBNAIL);
            positionPlayer = savedInstanceState.getLong(LONG);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(DESCRIPTION, description);
        outState.putString(VIDEO, video);
        outState.putString(THUMBNAIL, thumbnail);
        outState.putLong(LONG, positionPlayer);
    }

    public void releasePlayer(){
        if (mExoPlayer!=null){
            positionPlayer = mExoPlayer.getCurrentPosition();
            play = mExoPlayer.getPlayWhenReady();
            mExoPlayer.release();
            mExoPlayer = null;
        }
       if (mMediaSession != null){
            mMediaSession.setActive(false);
       }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23){
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23){
            releasePlayer();
        }
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
