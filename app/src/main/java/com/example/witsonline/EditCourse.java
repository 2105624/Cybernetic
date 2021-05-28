package com.example.witsonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditCourse extends AppCompatActivity {

    private List<String> faculties = new ArrayList<>();
    private List<Integer>facultyIDs = new ArrayList<>();
    private List<String> courseCodes  = new ArrayList<>();
    
    //creating views
    private ImageView image;
    private Button btnEdit;
    private TextInputLayout code;
    private TextInputLayout name;
    private TextInputLayout description;
    private TextInputLayout outline;
    private TextInputLayout tags;
    private RadioGroup rgVisibility;
    private RadioButton rbPublic;
    private RadioButton rbPrivate;

    //for editing image
    public static final int IMAGE_REQUEST_CODE = 3;
    public static final int STORAGE_PERMISSION_CODE = 123;
    private boolean imgChanged = false;
    private Bitmap bitmap;
    private Uri filePath;

    //updating course
    private RequestQueue requestQueue;
    private String updateURL = "https://lamp.ms.wits.ac.za/home/s2105624/courseUpdate.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);

        //linking views
        image = findViewById(R.id.courseEditImage);
        btnEdit = findViewById(R.id.buttonEditCourse);
        rgVisibility = findViewById(R.id.rgVisibility);
        rbPrivate = findViewById(R.id.privateVisibility);
        rbPublic = findViewById(R.id.publicVisibility);
        code = findViewById(R.id.courseCode);
        name = findViewById(R.id.courseName);
        description = findViewById(R.id.courseDescription);
        outline = findViewById(R.id.courseOutline);
        tags = findViewById(R.id.courseTags);

        //setting views
        code.getEditText().setText(COURSE.CODE);
        name.getEditText().setText(COURSE.NAME);
        description.getEditText().setText(COURSE.DESCRIPTION);
        outline.getEditText().setText(COURSE.OUTLINE);
        tags.getEditText().setText(COURSE.TAGS);
        if(!COURSE.IMAGE.equals("null")){
            Glide.with(this).load(COURSE.IMAGE).into(image);
        }

        //image on click
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Complete action using"), IMAGE_REQUEST_CODE);
            }
        });

        //edit button on click
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgChanged){
                    StringRequest request = new StringRequest(Request.Method.POST, updateURL, new Response.Listener<String>() {
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
                            parameters.put("code", COURSE.CODE);
                            parameters.put("name", name.getEditText().getText().toString().trim());
                            parameters.put("description", description.getEditText().getText().toString().trim());
                            //parameters.put("outline", outline.getEditText().getText().toString().trim());
                            //parameters.put("visibility", visibility);
                            //parameters.put("instructor", USER.USERNAME);
                            //parameters.put("faculty", facultyIDs.get(facultyPos).toString());
                            parameters.put("bitmap", getStringImage(bitmap));
                            //parameters.put("tags", tags.getEditText().getText().toString().trim());
                            //   parameters.put("path", getPath(filePath));
                            //   parameters.put("ext", ext);
                            return parameters;
                        }
                    };
                    requestQueue.add(request);
                    Toast.makeText(EditCourse.this, "Course update successful", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(EditCourse.this, CourseHomePageInstructor.class);
                    startActivity(i);
                    finish();
                   // Toast.makeText(EditCourse.this, "Image changed", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(EditCourse.this, "Image unchanged", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //getting and setting bitmap
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
        }
        if (filePath != null) {
            imgChanged = true;
            try {
                ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), filePath);
                bitmap = ImageDecoder.decodeBitmap(source);
                image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            imgChanged = false;
        }
    }

    //getting the bitmap of image and encoding it as a string
    private String getStringImage(Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        final String temp = Base64.encodeToString(b, Base64.DEFAULT);
        Log.i("My_data_image", "" + temp);
        return temp;
    }

    public void requestStoragePermission(){
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


}