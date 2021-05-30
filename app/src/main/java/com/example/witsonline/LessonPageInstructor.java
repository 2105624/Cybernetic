package com.example.witsonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LessonPageInstructor extends AppCompatActivity {

    TextView lessonName;
    TextView lessonCourse;
    TextView lessonText;
    //TextView lessonResource;
    DownloadManager downloadManager;

    // for downloading the pdf
    Button downloadButton;

    // for editing the lesson
    ImageButton editLesson;

    //Displaying YouTube videos
    private YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_page_instructor);

        //Initializing views

        downloadButton = (Button)findViewById(R.id.lessonDownload);
        lessonName = (TextView)findViewById(R.id.lessonPageName);
        lessonCourse = (TextView)findViewById(R.id.lessonPageCourse);
        lessonText = (TextView)findViewById(R.id.lessonPageText);
        editLesson = (ImageButton) findViewById(R.id.editLesson);
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
                    Toast.makeText(LessonPageInstructor.this, "No resources available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Display the video
        youTubePlayerView = findViewById(R.id.lessonYoutubePlayer);
        getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = getVideoIdFromYoutubeUrl(LESSON.Url);
                youTubePlayer.cueVideo(videoId, 0);

            }
        });

        editLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(LessonPageInstructor.this, LESSON.ID, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(LessonPageInstructor.this,EditLesson.class);
                startActivity(i);
            }
        });

    }
    //Get the ID of the youtube video
    public static String getVideoIdFromYoutubeUrl(String youtubeUrl)
    {
       /*
           Possibile Youtube urls.
           http://www.youtube.com/watch?v=WK0YhfKqdaI
           http://www.youtube.com/embed/WK0YhfKqdaI
           http://www.youtube.com/v/WK0YhfKqdaI
           http://www.youtube-nocookie.com/v/WK0YhfKqdaI?version=3&hl=en_US&rel=0
           http://www.youtube.com/watch?v=WK0YhfKqdaI
           http://www.youtube.com/watch?feature=player_embedded&v=WK0YhfKqdaI
           http://www.youtube.com/e/WK0YhfKqdaI
           http://youtu.be/WK0YhfKqdaI
        */
        String pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        //url is youtube url for which you want to extract the id.
        Matcher matcher = compiledPattern.matcher(youtubeUrl);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(this,BrowseLessons.class);
        startActivity(i);
        finish();
    }
}