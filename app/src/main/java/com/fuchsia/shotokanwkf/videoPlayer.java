 package com.fuchsia.shotokanwkf;

 import android.content.Intent;
 import android.content.res.Configuration;
 import android.os.Bundle;
 import android.util.Log;
 import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

 public class videoPlayer extends AppCompatActivity {

     private static final String COMMON_TAG = "OrintationChange";

     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_video_player);

         getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
         getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
         getWindow().getDecorView().setSystemUiVisibility(
                 View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|
                         View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

         final YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
         getLifecycle().addObserver(youTubePlayerView);


         Intent intent = getIntent();
         final String videoId = intent.getStringExtra("nam");

         youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
             @Override
             public void onReady(@NonNull YouTubePlayer youTubePlayer) {

                 youTubePlayerView.enterFullScreen();
                 youTubePlayer.loadVideo(videoId, 0);
             }
         });


     }

     @Override
     protected void onSaveInstanceState(Bundle outState) {
         super.onSaveInstanceState(outState);
         Log.i(COMMON_TAG,"MainActivity onSaveInstanceState");
     }

     @Override
     protected void onRestoreInstanceState(Bundle savedInstanceState) {
         super.onRestoreInstanceState(savedInstanceState);
         Log.i(COMMON_TAG,"MainActivity onSaveInstanceState");
     }


     @Override
     public void onConfigurationChanged(Configuration newConfig) {
         super.onConfigurationChanged(newConfig);
         if(newConfig.orientation ==Configuration.ORIENTATION_LANDSCAPE){
             Log.i(COMMON_TAG, "landscape");
         }else if(newConfig.orientation==Configuration.ORIENTATION_PORTRAIT){
             Log.i(COMMON_TAG, "portrait");
         }
     }


 }
