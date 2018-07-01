package com.example.android.bakingapp.fragment;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.bakingapp.R;
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

public class ExoActivityFragment extends Fragment implements ExoPlayer.EventListener{

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
    private int position;

    private String mDescription;
    private String mVideo;
    private String mThumb;
    private boolean isPhone;

    private static final String TAG = ExoActivityFragment.class.getSimpleName();

    public ExoActivityFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.exo_fragment, container, false);

        mPlayerView = root.findViewById(R.id.player_view);
        mText = root.findViewById(R.id.recipe_description_media);

        mCard = root.findViewById(R.id.cv_recipe_description);
        mButton = root.findViewById(R.id.cv_button);

        mLeft = root.findViewById(R.id.left_click_image);
        mRight = root.findViewById(R.id.right_click_image);

        isPhone = getResources().getBoolean(R.bool.isPhone);

        Bundle bundle = getArguments();

        if (bundle!=null){

            String description = bundle.getString("Description");
            final String thumbnail = bundle.getString("Thumbnail");
            final String video = bundle.getString("Video");
            final List<Steps> steps = bundle.getParcelableArrayList("List");
            position = bundle.getInt("Id");

            mText.setText(description);

            if (isPhone){
                mRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (position >= 0 && position < steps.size() - 1) {
                            position++;
                            if (mExoPlayer != null) releasePlayer();
                            mSteps = steps.get(position);
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
                        if (position > 0 && position <= steps.size()) {
                            position--;
                            if (mExoPlayer != null)
                                releasePlayer();
                            mSteps = steps.get(position);
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

                resizePlayer(getResources().getConfiguration().orientation);


            }

            initializeMediaSession();

            assert thumbnail != null;
            if (!thumbnail.isEmpty()){
                initializePlayer(Uri.parse(thumbnail));
            } else {
                assert video != null;
                if(!video.isEmpty()){
                    initializePlayer(Uri.parse(video));
                }
            }

        }

        return root;
    }


    private void resizePlayer(int orientation){
        if (orientation == Configuration.ORIENTATION_LANDSCAPE){
            mCard.setVisibility(View.GONE);
            mButton.setVisibility(View.GONE);
            mParams = (RelativeLayout.LayoutParams) mPlayerView.getLayoutParams();
            mParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            mParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            mPlayerView.setLayoutParams(mParams);

            if (((AppCompatActivity)getActivity()).getSupportActionBar()!=null){
                ((AppCompatActivity)getActivity()).getSupportActionBar() .hide();
            }
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
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

            if (((AppCompatActivity)getActivity()).getSupportActionBar()!=null){
                ((AppCompatActivity)getActivity()).getSupportActionBar().show();
            }

            getActivity().getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            );
        }
    }


    public void releasePlayer(){
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
        mMediaSession.setActive(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(getActivity());
        }
        return super.onOptionsItemSelected(item);
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
        mMediaSession.setCallback(new ExoActivityFragment.MySessionCallback());
        mMediaSession.setActive(true);

    }


    private void initializePlayer(Uri mediaUri){

        if (mExoPlayer == null){

            TrackSelector selector = new DefaultTrackSelector();
            LoadControl control = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), selector, control);
            mPlayerView.setPlayer(mExoPlayer);
            mPlayerView.requestFocus();

            mExoPlayer.addListener(this);

            String userAgent = Util.getUserAgent(getActivity(), "BakingApp");
            MediaSource source = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                   (getActivity()), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(source);
            mExoPlayer.setPlayWhenReady(true);

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
