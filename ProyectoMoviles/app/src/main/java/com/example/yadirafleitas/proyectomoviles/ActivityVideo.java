package com.example.yadirafleitas.proyectomoviles;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * Created by Yadira Fleitas on 25/11/2015.
 */
public class ActivityVideo extends AppCompatActivity {
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       final VideoView videoView =
                (VideoView) findViewById(R.id.videoView);

//        String videoURL = "http://www.ebookfrenzy.com/android_book/movie.mp4";
//        MediaController controller= new MediaController (this);
//        controller.setAnchorView (videoView);
//        controller.setMediaPlayer(videoView);
//
//        Uri uri = Uri.parse(videoURL);
//        videoView.setVideoURI(uri);
//
//        videoView.setMediaController (controller);


        videoView.setVideoPath ("http://www.ebookfrenzy.com/android_book/movie.mp4");
        videoView.requestFocus();
        videoView.start();
    }



}
