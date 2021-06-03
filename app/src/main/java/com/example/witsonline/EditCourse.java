package com.example.witsonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private Spinner spinner;
    private TextView numOfTags;
    private boolean facultySelected = false;
    private TextView numOfOutlines;
    private Button addOutline;
    private Button addTag;
    private ArrayList<String>allTags = new ArrayList<String>();
    private ArrayList<String>allOutlines=new ArrayList<String>();

    //for editing image
    public static final int IMAGE_REQUEST_CODE = 3;
    public static final int STORAGE_PERMISSION_CODE = 123;
    private boolean imgChanged = false;
    private Bitmap bitmap;
    private Uri filePath;
    private String visibility = "Public";
    private int facultyPos;

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
        addOutline = findViewById(R.id.buttonAddCourseOutline);
        addTag =  findViewById(R.id.buttonAddTag);
        numOfOutlines = findViewById(R.id.numberOfCourseOutlines);
        numOfTags = findViewById(R.id.numberOfTags);

        //setting views
        code.getEditText().setText(COURSE.CODE);
        name.getEditText().setText(COURSE.NAME);
        description.getEditText().setText(COURSE.DESCRIPTION);
        if(!COURSE.IMAGE.equals("null")){
            Glide.with(this).load(COURSE.IMAGE).into(image);
        }
        //store all current tags
        String [] currentTags = COURSE.TAGS.split(";");
        final int[] tagPosition = {0};
        if(currentTags.length!=0){
            tags.getEditText().setText(currentTags[0]);
        }
        //store all current outlines
        String [] currentOutlines = COURSE.OUTLINE.split(";");
        final int[] outlinePosition = {0};
        if(currentOutlines.length!=0){
            outline.getEditText().setText(currentOutlines[0]);
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
        faculties.add(0,"Select a faculty");
        facultyIDs.add(0);
        spinner = findViewById(R.id.spFaculty);
        PHPRequest req = new PHPRequest("https://lamp.ms.wits.ac.za/home/s2105624/");
        req.doRequest(EditCourse.this, "faculty",
                new ResponseHandler() {
                    @Override
                    public void processResponse(String response) {
                        addFacultyNames(response);
                    }
                });

        //Get Course data
        req.doRequest(EditCourse.this, "courseCodes",
                new ResponseHandler() {
                    @Override
                    public void processResponse(String response) {
                        getCourseCodes(response);
                    }
                });
        //store the visibility
        rgVisibility.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.privateVisibility){
                    visibility = "Private";
                }else{
                    visibility = "Public";
                }
            }
        });

        //store the visibility
        rgVisibility.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.privateVisibility){
                    visibility = "Private";
                }else{
                    visibility = "Public";
                }
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter(EditCourse.this,android.R.layout.simple_spinner_item,faculties){

            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    //Disable the first item of spinner.
                    return false;
                }
                else
                {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textview = (TextView) view;
                if (position == 0) {
                    textview.setTextColor(Color.GRAY);
                } else {
                    textview.setTextColor(Color.BLACK);
                }
                return view;
            }


        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0,false);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                facultyPos = position;
                facultySelected = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //Add course outline button on click
        addOutline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validateCourseOutlineOrTag(outline,allOutlines)){
                    //error will be displayed
                }
                else{
                    outlinePosition[0] =outlinePosition[0]+1;
                    Integer numberOfOutlines = Integer.valueOf(numOfOutlines.getText().toString())+1;
                    allOutlines.add(outline.getEditText().getText().toString());
                    numOfOutlines.setText(Integer.toString(numberOfOutlines));
                    if(currentOutlines.length>outlinePosition[0]){
                        outline.getEditText().setText(currentOutlines[outlinePosition[0]]);

                    }
                }

            }
        });

        //Add course tag button on click
        addTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validateCourseOutlineOrTag(tags,allTags)){
                    //error will be displayed
                }
                else{
                    tagPosition[0] =tagPosition[0]+1;
                    Integer numberOfTags = Integer.valueOf(numOfTags.getText().toString())+1;
                    allTags.add(tags.getEditText().getText().toString());
                    numOfTags.setText(Integer.toString(numberOfTags));
                    if(currentTags.length>tagPosition[0]){
                        tags.getEditText().setText(currentTags[tagPosition[0]]);

                    }
                }

            }
        });

        //edit button on click
        boolean validInput = true; //just using it for now to test course Images
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmpty(name) | isEmpty(description) | validateOutlineAndTag(numOfOutlines,outline,numOfTags,tags) | noFacultySelected(facultySelected) | !validCourseCode(code,courseCodes)){

                }else{

                    String finalOutline = convert(allOutlines);
                    String finalTags = convert(allTags);
                    String bm = "nofile";
                    if (imgChanged){
                        bm = getStringImage(bitmap);
                    }
                    final String file = bm;

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
                            parameters.put("outline", finalOutline);
                            parameters.put("visibility", visibility);
                            parameters.put("faculty", facultyIDs.get(facultyPos).toString());
                            parameters.put("bitmap",file);
                            parameters.put("tags", finalTags);
                            //parameters.put("path", getPath(filePath));
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
    // This function checks to see if no faculty was selected. This will produce an error
    public boolean noFacultySelected(Boolean check){
        if (check == false){
            Toast toast = Toast.makeText(EditCourse.this, "Select a faculty", Toast.LENGTH_LONG);
            toast.show();
            return true;
        }else{
            return false;
        }
    }
    public boolean validCourseCode(TextInputLayout text,List<String> codes){
        String codeInput = text.getEditText().getText().toString().trim();
        boolean codeExists = false;
        boolean valid = false;
        for (int i = 0; i < codes.size(); i++) {
            if (codeInput.equals(codes.get(i)) && !codeInput.equals(COURSE.CODE)) {
                codeExists = true;
            }
        }
        if (codeExists){
            text.setError("Course code already exists");
        }
        else if (codeInput.isEmpty()){
            text.setError("Field can't be empty");
        }
        else{
            text.setError(null);
            valid = true;
        }
        return valid;
    }

    //This function checks if course outlines and tags have been added
    public boolean validateOutlineAndTag(TextView numOfOutlines, TextInputLayout outline, TextView numOfTags, TextInputLayout tag){
        boolean invalid = false;
        if(Integer.valueOf(numOfOutlines.getText().toString())==0){
            outline.setError("Add course outline");
            invalid = true;
        }
        if(Integer.valueOf(numOfTags.getText().toString())==0){
            tag.setError("Add tags");
            invalid = true;
        }
        return invalid;
    }
    //This function converts course tags/outline so that they can be split using ; when displayed in curse home page
    public String convert(ArrayList<String>list){
        String out = list.get(0);
        for(int i=1;i<list.size()-1;i++){
            out = out+";"+list.get(i);
        }
        if(list.size()>1) {
            out = out +";"+ list.get(list.size()-1);
        }
        return out;
    }
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
    //This function checks if the course outline has be added already
    public boolean validateCourseOutlineOrTag(TextInputLayout text, ArrayList<String>outline){
        String codeInput = text.getEditText().getText().toString().trim();
        boolean valid = false;

        if(isEmpty(text)){
            valid = true;
        }
        else{
            for (int i = 0; i < outline.size(); i++) {
                if (codeInput.equals(outline.get(i).trim())) {
                    valid= true;
                    text.setError(text.getEditText().getText().toString()+" has been added already");
                    break;
                }
            }
        }
        return valid;
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
    //append JSON data into faculty arraylists
    public void addFacultyNames(String json){

        try {
            JSONArray all = new JSONArray(json);
            for (int i = 0; i < all.length();i++){
                JSONObject obj = all.getJSONObject(i);
                int id = obj.getInt("Faculty_ID");
                String name = obj.getString("Faculty_Name");
                faculties.add(name);
                facultyIDs.add(id);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }
    //get course codes to see if course already exists
    public void getCourseCodes(String json){

        try {
            JSONArray all = new JSONArray(json);
            for (int i = 0; i < all.length();i++){
                JSONObject obj = all.getJSONObject(i);
                String code = obj.getString("Course_Code");
                courseCodes.add(code);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(EditCourse.this, CourseHomePageInstructor.class);
        startActivity(i);
        finish();
    }


}