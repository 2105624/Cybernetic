package com.example.witsonline;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.*;

@RequiresApi(api = Build.VERSION_CODES.M)
public class CreateCourse extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener  {

    //This is for the logout pop up menu
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button btnLogout, btnCancel;

    public static final int IMAGE_REQUEST_CODE = 3;
    public static final int STORAGE_PERMISSION_CODE = 123;
    private List<String> faculties = new ArrayList<>();
    private List<Integer>facultyIDs = new ArrayList<>();
    private List<String> courseCodes  = new ArrayList<>();
    private int facultyPos;
    private Spinner spinner;
    private boolean facultySelected = false;
    private RadioGroup rgVisibility;
    private ImageView image;
    private TextInputLayout code;
    private TextInputLayout name;
    private TextInputLayout description;
    private TextInputLayout outline;
    private TextInputLayout tags;
    private TextView numOfTags;
    private TextView numOfOutlines;
    private Button btnCreate;
    private Button addOutline;
    private Button addTag;
    private RadioButton rbPublic;
    private RadioButton rbPrivate;
    private Bitmap bitmap;
    private Uri filePath;
    private String visibility = "Public";
    private RequestQueue requestQueue;
    private String insertURL = "https://lamp.ms.wits.ac.za/home/s2105624/courseInsert.php";
    private String insertNoImageURL = "https://lamp.ms.wits.ac.za/home/s2105624/courseInsertNoImage.php";
    private boolean imgSelected;
    private ArrayList<String>allTags = new ArrayList<String>();
    private ArrayList<String>allOutlines=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

       // START dashboard & layout code
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_course);

        BottomNavigationView dashboardBottomNavigation = findViewById(R.id.dashboardBottomNavigation);
        dashboardBottomNavigation.setOnNavigationItemSelectedListener(CreateCourse.this);

        dashboardBottomNavigation.inflateMenu(R.menu.menu_instructor);
        dashboardBottomNavigation.getMenu().findItem(R.id.menuCreateCourse).setChecked(true);
        // END dashboard & layout code

        //START activity code
        // Toast toast = Toast.makeText(CreateCourseActivity.this, USER.USERNAME, Toast.LENGTH_LONG);
        //   toast.show();
        imgSelected = false;

        //get views from UI
        rgVisibility = findViewById(R.id.rgVisibility);
        rbPrivate = findViewById(R.id.privateVisibility);
        rbPublic = findViewById(R.id.publicVisibility);
        code = findViewById(R.id.courseCode);
        name = findViewById(R.id.courseName);
        description = findViewById(R.id.courseDescription);
        outline = findViewById(R.id.courseOutline);
        tags = findViewById(R.id.courseTags);
        btnCreate = findViewById(R.id.buttonCreateCourse);
        image = findViewById(R.id.courseImage);
        addOutline = findViewById(R.id.buttonAddCourseOutline);
        addTag =  findViewById(R.id.buttonAddTag);
        numOfOutlines = findViewById(R.id.numberOfCourseOutlines);
        numOfTags = findViewById(R.id.numberOfTags);

        requestStoragePermission();

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Complete action using"), IMAGE_REQUEST_CODE);
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



        PHPRequest req = new PHPRequest("https://lamp.ms.wits.ac.za/home/s2105624/");

        //Get Faculty data
        req.doRequest(CreateCourse.this, "faculty",
                new ResponseHandler() {
                    @Override
                    public void processResponse(String response) {
                        addFacultyNames(response);
                    }
                });

        //Get Course data
        req.doRequest(CreateCourse.this, "courseCodes",
                new ResponseHandler() {
                    @Override
                    public void processResponse(String response) {
                        getCourseCodes(response);
                    }
                });

        //Add faculties into the spinner
        faculties.add(0,"Select a faculty");
        facultyIDs.add(0);
        spinner = findViewById(R.id.spFaculty);
        ArrayAdapter<String> adapter = new ArrayAdapter(CreateCourse.this,android.R.layout.simple_spinner_item,faculties){

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
                    Integer numberOfOutlines = Integer.valueOf(numOfOutlines.getText().toString())+1;
                    allOutlines.add(outline.getEditText().getText().toString());
                    numOfOutlines.setText(Integer.toString(numberOfOutlines));
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
                    Integer numberOfTags = Integer.valueOf(numOfTags.getText().toString())+1;
                    allTags.add(tags.getEditText().getText().toString());
                    numOfTags.setText(Integer.toString(numberOfTags));
                }

            }
        });


        //Create course button on click
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmpty(name) | isEmpty(description) | validateOutlineAndTag(numOfOutlines,outline,numOfTags,tags) | noFacultySelected(facultySelected) | !validCourseCode(code,courseCodes)) {
                    //error messages will be displayed
                } else {
                    String finalOutline = convert(allOutlines);
                    String finalTags = convert(allTags);
                    if (imgSelected) {
                        //    String ext = FilenameUtils.getExtension(getPath(filePath)); //get extension of image
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
                                parameters.put("code", code.getEditText().getText().toString().trim());
                                parameters.put("name", name.getEditText().getText().toString().trim());
                                parameters.put("description", description.getEditText().getText().toString().trim());
                                parameters.put("outline", finalOutline);
                                parameters.put("visibility", visibility);
                                parameters.put("instructor", USER.USERNAME);
                                parameters.put("faculty", facultyIDs.get(facultyPos).toString());
                                parameters.put("bitmap", getStringImage(bitmap));
                                parameters.put("tags", finalTags);
                                //   parameters.put("path", getPath(filePath));
                                //   parameters.put("ext", ext);
                                return parameters;
                            }
                        };
                        requestQueue.add(request);
                        Toast.makeText(CreateCourse.this, "Course creation successful", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(CreateCourse.this, Dashboard.class);
                        startActivity(i);
                        finish();
                    } else {
                        StringRequest request = new StringRequest(Request.Method.POST, insertNoImageURL, new Response.Listener<String>() {
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
                                parameters.put("code", code.getEditText().getText().toString().trim());
                                parameters.put("name", name.getEditText().getText().toString().trim());
                                parameters.put("description", description.getEditText().getText().toString().trim());
                                parameters.put("outline", finalOutline);
                                parameters.put("visibility", visibility);
                                parameters.put("instructor", USER.USERNAME);
                                parameters.put("faculty", facultyIDs.get(facultyPos).toString());
                                parameters.put("tags", finalTags);
                                return parameters;
                            }
                        };
                        requestQueue.add(request);


                        Toast.makeText(CreateCourse.this, "Course creation successful", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(CreateCourse.this, Dashboard.class);
                        startActivity(i);
                        finish();
                    }

                }
                //  String ext = FilenameUtils.getExtension(getPath(filePath)); //get extension of image
                // String images = getStringImage(bitmap);
                //  String ext2  = FilenameUtils.getExtension(getStringImage(bitmap)); //get extension of image
                //  outline.getEditText().setText(getStringImage(bitmap));
                //try get extension from the getStringImage function like how you used getPathCOMS
                //   Toast.makeText(CreateCourseActivity.this, getStringImage(bitmap), Toast.LENGTH_SHORT).show();

                //This should update the tags in the database

            }
        });
    }
        //END activity code


    //START dashboard code
    //This method will check if the recyclerview has reached the bottom or not
    public boolean isLastItemDistplaying(RecyclerView recyclerView){
        if(recyclerView.getAdapter().getItemCount() != 0){
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() -1){
                return true;
            }
        }
        return false;
    }

    public void createNewViewDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View viewPopUp = LayoutInflater.from(this)
                .inflate(R.layout.logout_dialog, null);

        btnLogout = (Button) viewPopUp.findViewById(R.id.btnLogout);
        btnCancel = (Button) viewPopUp.findViewById(R.id.btnLogoutCancel);

        dialogBuilder.setView(viewPopUp);
        dialog = dialogBuilder.create();
        dialog.show();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent5 = new Intent(CreateCourse.this,LoginActivity.class);
                startActivity(intent5);
                finish();
                dialog.dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuHomeInstructor :
                Intent intent = new Intent(CreateCourse.this, Dashboard.class);
                startActivity(intent);
                finish();
                break;

            case R.id.menuMyCoursesInstructor :
                Intent intent1 = new Intent(CreateCourse.this, MyCourses.class);
                startActivity(intent1);
                finish();
                break;

            case R.id.menuCreateCourse :
                break;

            case R.id.menuLogOutInstructor :
                createNewViewDialog();
                break;
        }
        return false;
    }
    //END dashboard code

    //START activity code

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

    //This function checks if course outlines and tags have been added
    public boolean validateOutlineAndTag(TextView numOfOutlines,TextInputLayout outline, TextView numOfTags,TextInputLayout tag){
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

    // This function checks to see if no faculty was selected. This will produce an error
    public boolean noFacultySelected(Boolean check){
        if (check == false){
            Toast toast = Toast.makeText(CreateCourse.this, "Select a faculty", Toast.LENGTH_LONG);
            toast.show();
            return true;
        }else{
            return false;
        }
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


    //This function checks if the course code already exists
    public boolean validCourseCode(TextInputLayout text,List<String> codes){
        String codeInput = text.getEditText().getText().toString().trim();
        boolean codeExists = false;
        boolean valid = false;
        for (int i = 0; i < codes.size(); i++) {
            if (codeInput.equals(codes.get(i))) {
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

    //getting and setting bitmap
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
        }
        if (filePath != null) {
            imgSelected = true;
            try {
                ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), filePath);
                bitmap = ImageDecoder.decodeBitmap(source);
                image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            imgSelected = false;
        }
    }

    //getting the bitmap of image and encoding it as a string
    public String getStringImage(Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        final String temp = Base64.encodeToString(b, Base64.DEFAULT);
        Log.i("My_data_image", "" + temp);
        return temp;
    }

    //not actually using this anymore. Might be useful later on
    public String getPath(Uri uri){
        String path = "";
        Cursor cursor = getContentResolver().query(uri,null,null,null,null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":")+1);
        cursor.close();

        cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,null,MediaStore.Images.Media._ID+" = ? ",new String[]{document_id},null);
        cursor.moveToFirst();
        if (cursor != null && cursor.moveToFirst()) {
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        }
        cursor.close();
        return path;
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
    public List<String> getFaculties(){
        return faculties;
    }
    public List<Integer> getFacultyIDs(){
        return facultyIDs;
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
    //For testing.
    public List<String> getCourseCodeList(){

        return courseCodes;
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(CreateCourse.this,CreateCourse.class);
        startActivity(intent);
        finish();
    }
    //END activity code

}
