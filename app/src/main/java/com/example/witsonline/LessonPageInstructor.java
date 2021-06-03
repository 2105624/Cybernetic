package com.example.witsonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    //This is for delaying while we load the lesson info
    ProgressBar progressBar;
    RelativeLayout relativeLayout;
    //Volley Request Queue
    private RequestQueue requestQueue;

    //
    String lessonURL = "https://lamp.ms.wits.ac.za/home/s2105624/getLessonInfo.php?ccode=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_page_instructor);

        //Initializing views

        downloadButton = (Button) findViewById(R.id.lessonDownload);
        lessonName = (TextView) findViewById(R.id.lessonPageName);
        lessonCourse = (TextView) findViewById(R.id.lessonPageCourse);
        lessonText = (TextView) findViewById(R.id.lessonPageText);
        editLesson = (ImageButton) findViewById(R.id.editLesson);
        //lessonResource = (TextView)findViewById(R.id.lessonPageResource);

        requestQueue = Volley.newRequestQueue(this);
        //Progress bar for the whole page and the page's relative layout
        progressBar = findViewById(R.id.progressBarLessonPageInstructor);
        relativeLayout = findViewById(R.id.CourseLessonHomeInstructorLayout);
        progressBar.setVisibility(View.VISIBLE);

        //getting the data for the page
        getLessonData();

       /* // download the pdf resource
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri uri = Uri.parse(LESSON.Resource);
                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    Long reference = downloadManager.enqueue(request);
                } catch (Exception e) {
                    Toast.makeText(LessonPageInstructor.this, "No resources available", Toast.LENGTH_SHORT).show();
                }
            }
        }); */

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
                Intent i = new Intent(LessonPageInstructor.this, EditLesson.class);
                startActivity(i);
                finish();
            }
        });

    }

    //Get the ID of the youtube video
    public static String getVideoIdFromYoutubeUrl(String youtubeUrl) {
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

    private JsonArrayRequest getLessonDataFromServer() {

        //Initializing progressbar
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBarLessonPageInstructor);

        //Displaying ProgressBar
        progressBar.setVisibility(View.VISIBLE);
        setProgressBarIndeterminateVisibility(true);

        //JsonArrayRequest of volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(lessonURL + LESSON.ID,
                (response) -> {
                    //parse JSON response in parseCourseData method
                    parseLessonData(response);
                    //Hiding the progressBar
                    //progressBar.setVisibility(View.GONE);
                },
                (error) -> {
                    //progressBar.setVisibility(View.GONE);
                    //If an error occurs that means end of the list has been reached
                    //Toast.makeText(CourseHomePage.this, "No More Items Available", Toast.LENGTH_SHORT).show();
                    Log.d("newtag", "getLessonDataFromServer: error occured");
                });
        //Returning the request
        return jsonArrayRequest;
    }

    private void getLessonData() {
        //Adding the method to the queue by calling the method getLessonData
        requestQueue.add(getLessonDataFromServer());
    }

    //This method will parse json Data for lesson
    private void parseLessonData(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            // Creating the lesson object
            JSONObject json = null;
            try {
                //Getting json
                json = array.getJSONObject(i);

                //Adding data to the lesson object
                LESSON.Name = json.getString("Lesson_Name");
                LESSON.Url = json.getString("Lesson_URL");
                LESSON.Resource = json.getString("Lesson_Resource");
                LESSON.Text = json.getString("Lesson_Text");

                //Setting text to views
                lessonName.setText(LESSON.Name);
                lessonCourse.setText(COURSE.NAME);
                lessonText.setText(LESSON.Text);

                // download the pdf resource
                downloadButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                            Uri uri = Uri.parse(LESSON.Resource);
                            DownloadManager.Request request = new DownloadManager.Request(uri);
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            Long reference = downloadManager.enqueue(request);
                        } catch (Exception e) {
                            Toast.makeText(LessonPageInstructor.this, "No resources available", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                //Make the page visible after displaying the correct lesson info
                progressBar.setVisibility(View.GONE);
                relativeLayout.setVisibility(View.VISIBLE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, BrowseLessons.class);
        startActivity(i);
        finish();
    }
}