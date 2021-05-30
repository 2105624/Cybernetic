package com.example.witsonline;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
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
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateLesson extends AppCompatActivity {


    //Lesson components
    private TextInputLayout lessonName;
    private TextInputLayout lessonText;
    private TextInputLayout lessonURL;
    private Button btnCreate;

    //for adding a pdf
    private TextView selectedFile;
    private ImageButton btnUploadFile;
    private int REQ_PDF = 21;
    private String encodedPDF;
    public static final int STORAGE_PERMISSION_CODE = 123;
    private boolean fileSelected = false;

    //Insertion
    private RequestQueue requestQueue;
    private String insertURL = "https://lamp.ms.wits.ac.za/home/s2105624/lessonInsert.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lesson);

        //request storage permission
        requestStoragePermission();

        //finding views
        lessonName = findViewById(R.id.lessonName);
        lessonText = findViewById(R.id.lessonText);
        lessonURL = findViewById(R.id.lessonURL);
        btnCreate = findViewById(R.id.buttonCreateLesson);
        selectedFile = findViewById(R.id.selectedFileText);
        btnUploadFile = findViewById(R.id.imageButtonUploadFile);

        //selecting a file
        btnUploadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("application/pdf");
                chooseFile = Intent.createChooser(chooseFile,"Choose a file");
                startActivityForResult(chooseFile,REQ_PDF);
            }
        });

        //the insertion
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isEmpty(lessonName) | isEmpty(lessonText) | !isYoutubeUrl()){
                    //error messages will be displayed
                }else{
                    String url = lessonURL.getEditText().getText().toString().trim();
                   if (!fileSelected){
                        encodedPDF = "nofile";
                    }
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
                            parameters.put("url",url);
                            parameters.put(("fs"),encodedPDF);
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


    }

    //code for getting pdf input stream
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_PDF && resultCode == RESULT_OK && data != null){
            Uri path = data.getData();
            if (path != null) {
                try {
                    fileSelected = true;
                    InputStream inputStream = CreateLesson.this.getContentResolver().openInputStream(path);
                    byte[] pdfInBytes = new byte[inputStream.available()];
                    inputStream.read(pdfInBytes);
                    encodedPDF = Base64.encodeToString(pdfInBytes, Base64.DEFAULT);

                    //Toast.makeText(CreateLesson.this, "Document selected",Toast.LENGTH_SHORT).show();
                    selectedFile.setText("File selected");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                fileSelected = false;
            }
        }
    }
    //Escape aphzostrophe

    //request permission to access external storage
    private void requestStoragePermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            return;
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
            //can explain here why you need this permission.
        }
        //ask for permission
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
    }

    //This method will be called when user taps on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){

        //Checking if request code is our request
        if (requestCode == STORAGE_PERMISSION_CODE){

            //if granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission granted",Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(this, "Permission denied",Toast.LENGTH_LONG).show();
            }
        }
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

    // Test if URL is a valid URL
    public boolean isYoutubeUrl()
    {
        String youTubeURl = lessonURL.getEditText().getText().toString().trim();
        boolean success;
        String pattern = "^(http(s)?:\\/\\/)?((w){3}.)?youtu(be|.be)?(\\.com)?\\/.+";
        if (!youTubeURl.isEmpty() && youTubeURl.matches(pattern))
        {
            lessonURL.setError(null);
            success = true;
        }
        else
        {
            // Not Valid youtube URL
            if (youTubeURl.isEmpty()){
                lessonURL.setError("Field can't be empty");
            }else{
                lessonURL.setError("Invalid YouTube URL");
            }
            success = false;
        }
        return success;
    }
    @Override
    public void onBackPressed(){
        Intent i = new Intent(this,CourseHomePageInstructor.class);
        startActivity(i);
        finish();
    }
}