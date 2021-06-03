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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditLesson extends AppCompatActivity {
    //Lesson components
    private TextInputLayout editLessonName;
    private TextInputLayout editLessonText;
    private TextInputLayout editLessonURL;
    private Button btnEdit;

    //for adding a pdf
    private TextView selectedFileEdit;
    private ImageButton btnUploadFileEdit;
    private int REQ_PDF = 21;
    private String encodedPDF;
    public static final int STORAGE_PERMISSION_CODE = 123;
    private boolean fileSelected = false;

    //Insertion
    private RequestQueue requestQueue;
    private String insertURL = "https://lamp.ms.wits.ac.za/home/s2105624/lessonUpdate.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_lesson);

        //request storage permission
        requestStoragePermission();

        //finding views
        editLessonName = findViewById(R.id.editLessonName);
        editLessonText = findViewById(R.id.editLessonText);
        editLessonURL = findViewById(R.id.editLessonURL);
        btnEdit = findViewById(R.id.buttonEditLesson);
        selectedFileEdit = findViewById(R.id.selectedFileTextEdit);
        btnUploadFileEdit = findViewById(R.id.imageButtonUploadFileEdit);

        //check if there is a resource or nor
        if (LESSON.Resource.equals("null")){
            selectedFileEdit.setText("Select a file");
        }

        //setting views
        editLessonName.getEditText().setText(LESSON.Name);
        editLessonText.getEditText().setText(LESSON.Text);
        editLessonURL.getEditText().setText(LESSON.Url);

        //selecting a file
        btnUploadFileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("application/pdf");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(chooseFile, REQ_PDF);
            }
        });

        //the insertion
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isEmpty(editLessonName) | isEmpty(editLessonText) | !isYoutubeUrl()) {
                    //error messages will be displayed
                } else {
                    String url = editLessonURL.getEditText().getText().toString().trim();
                    if (!fileSelected) {
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
                            parameters.put("name", editLessonName.getEditText().getText().toString().trim());
                            parameters.put("text", editLessonText.getEditText().getText().toString().trim());
                            parameters.put("url", url);
                            parameters.put("id", LESSON.ID);
                            parameters.put(("fs"), encodedPDF);
                            return parameters;
                        }
                    };
                    requestQueue.add(request);
                    Toast.makeText(EditLesson.this, "Lesson update successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditLesson.this, LessonPageInstructor.class);
                    startActivity(intent);
                }
            }
        });
    }

    //code for getting pdf input stream
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_PDF && resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();
            if (path != null) {
                try {
                    fileSelected = true;
                    InputStream inputStream = EditLesson.this.getContentResolver().openInputStream(path);
                    byte[] pdfInBytes = new byte[inputStream.available()];
                    inputStream.read(pdfInBytes);
                    encodedPDF = Base64.encodeToString(pdfInBytes, Base64.DEFAULT);

                    //Toast.makeText(CreateLesson.this, "Document selected",Toast.LENGTH_SHORT).show();
                    selectedFileEdit.setText("File selected");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                fileSelected = false;
            }
        }
    }
    //Escape aphzostrophe

    //request permission to access external storage
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //can explain here why you need this permission.
        }
        //ask for permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    //This method will be called when user taps on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking if request code is our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //if granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    // This function checks if a required text is empty or not
    public boolean isEmpty(TextInputLayout text) {
        boolean empty = false;
        if (text.getEditText().getText().toString().isEmpty()) {
            text.setError("Field can't be empty");
            empty = true;
        } else {
            text.setError(null);
        }
        return empty;
    }

    // Test if URL is a valid URL
    public boolean isYoutubeUrl() {
        String youTubeURl = editLessonURL.getEditText().getText().toString().trim();
        boolean success;
        String pattern = "^(http(s)?:\\/\\/)?((w){3}.)?youtu(be|.be)?(\\.com)?\\/.+";
        if (!youTubeURl.isEmpty() && youTubeURl.matches(pattern)) {
            editLessonURL.setError(null);
            success = true;
        } else {
            // Not Valid youtube URL
            if (youTubeURl.isEmpty()) {
                editLessonURL.setError("Field can't be empty");
            } else {
                editLessonURL.setError("Invalid YouTube URL");
            }
            success = false;
        }
        return success;
    }



    @Override
    public void onBackPressed(){
        Intent i = new Intent(this,BrowseLessons.class);
        startActivity(i);
        finish();
    }

}
