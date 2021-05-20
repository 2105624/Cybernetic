package com.example.witsonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class LessonPage extends AppCompatActivity {
    TextView lessonName;
    TextView lessonCourse;
    TextView lessonText;
    //TextView lessonResource;
    DownloadManager downloadManager;

    // for downloading the pdf
    Button downloadButton;

    //Displaying YouTube videos
    private YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_page);

        //Initializing views

        downloadButton = (Button)findViewById(R.id.lessonDownload);
        lessonName = (TextView)findViewById(R.id.lessonPageName);
        lessonCourse = (TextView)findViewById(R.id.lessonPageCourse);
        lessonText = (TextView)findViewById(R.id.lessonPageText);
        //lessonResource = (TextView)findViewById(R.id.lessonPageResource);

        //Setting text to views
        lessonName.setText(LESSON.Name);
        lessonCourse.setText(COURSE.NAME);
        lessonText.setText(LESSON.Text);
        //lessonResource.setText(LESSON.Resource);


        // download the pdf resource
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    downloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri uri = Uri.parse(LESSON.Resource);
                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    Long reference = downloadManager.enqueue(request);
                } catch (Exception e) {
                    Toast.makeText(LessonPage.this, "No resources available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Display the video
        youTubePlayerView = findViewById(R.id.lessonYoutubePlayer);
        getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = LESSON.Url;
                youTubePlayer.cueVideo(videoId, 0);

            }
        });

    }
}