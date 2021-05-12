package com.example.witsonline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

public class CreateLesson extends AppCompatActivity {

    //for displaying a video
    private Uri uri;
    private VideoView videoView;
    private MediaController mediaController;

    //Lesson components
    private TextInputLayout lessonName;
    private TextInputLayout lessonText;
    private Button btnCreate;


    //Insertion
    private RequestQueue requestQueue;
    private String insertURL = "https://lamp.ms.wits.ac.za/home/s2105624/lessonInsert.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lesson);

        //finding views
        lessonName = findViewById(R.id.lessonName);
        lessonText = findViewById(R.id.lessonText);
        btnCreate = findViewById(R.id.buttonCreateLesson);

        //the insertion
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmpty(lessonName) | isEmpty(lessonText)){
                    //error messages will be displayed
                }else{
                    StringRequest request = new StringRequest(Request.Method.POST, insertURL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println(response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println(error.getMessage());
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> parameters = new HashMap<>();
                            parameters.put("name", lessonName.getEditText().getText().toString().trim());
                            parameters.put("course", COURSE.CODE);
                            parameters.put("text", lessonText.getEditText().getText().toString().trim());
                            return parameters;
                        }
                    };
                    requestQueue.add(request);
                    Toast.makeText(CreateLesson.this, "Lesson creation successful", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(CreateLesson.this, CourseHomePageInstructor.class);
                    startActivity(i);
                    finish();

                }
            }
        });

        //Displaying the video
      //  videoView = findViewById(R.id.videoView);
      //  mediaController = new MediaController(this);
      //  mediaController.setAnchorView(videoView);
       // videoView.setMediaController(mediaController);
       Uri uri = Uri.parse("http://web.logiclabsolutions.com/xxx.mp4");
       // Uri uri = Uri.parse("https://www.youtube.com/watch?v=teiIw6Zp6X8");
      //  videoView.setVideoURI(uri);
      //  videoView.requestFocus();
      //  videoView.start();



    }

    // This function checks if a required text is empty or not
    public boolean isEmpty(TextInputLayout text) {
        boolean empty = false;
        if (text.getEditText().getText().toString().isEmpty()) {
            text.setError("Field can't be empty");
            empty = true;
        }else{
            text.setError(null);
        }
        return empty;
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(this,CourseHomePageInstructor.class);
        startActivity(i);
        finish();
    }
}