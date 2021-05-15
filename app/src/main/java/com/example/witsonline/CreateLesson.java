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

import java.io.IOException;
import java.io.InputStream;
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

        //reuqest storage permission
        requestStoragePermission();

        //finding views
        lessonName = findViewById(R.id.lessonName);
        lessonText = findViewById(R.id.lessonText);
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
                if (isEmpty(lessonName) | isEmpty(lessonText)){
                    //error messages will be displayed
                }else{
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

        //Displaying the video
      //  videoView = findViewById(R.id.videoView);
      //  mediaController = new MediaController(this);
      //  mediaController.setAnchorView(videoView);
       // videoView.setMediaController(mediaController);
    //   Uri uri = Uri.parse("http://web.logiclabsolutions.com/xxx.mp4");
       // Uri uri = Uri.parse("https://www.youtube.com/watch?v=teiIw6Zp6X8");
      //  videoView.setVideoURI(uri);
      //  videoView.requestFocus();
      //  videoView.start();



    }

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

    @Override
    public void onBackPressed(){
        Intent i = new Intent(this,CourseHomePageInstructor.class);
        startActivity(i);
        finish();
    }
}