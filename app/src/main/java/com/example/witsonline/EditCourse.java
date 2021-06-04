package com.example.witsonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditCourse extends AppCompatActivity {

    private List<String> courseCodes  = new ArrayList<>();
    
    //creating views
    private ImageView image;
    private Button btnEdit;
    private TextInputLayout name;
    private TextInputLayout description;
    private TextInputLayout outline;
    private TextInputLayout tag;
    private RadioGroup rgVisibility;
    private RadioButton rbPublic;
    private RadioButton rbPrivate;
    private TextView numOfTags;
    private TextView numOfOutlines;
    private Button addOutline;
    private Button addTag;
    private ArrayAdapter arrayAdapterTag;
    private ArrayList<String>allTags = new ArrayList<String>();
    private ArrayList<String>allOutlines=new ArrayList<String>();
    private ListView tagList;

    //for editing image
    public static final int IMAGE_REQUEST_CODE = 3;
    public static final int STORAGE_PERMISSION_CODE = 123;
    private boolean imgChanged = false;
    private Bitmap bitmap;
    private Uri filePath;
    LinearLayout outlineLayout;
    LinearLayout tagLayout;
    private String visibility = "Public";
    private int facultyPos;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button btnDeleteOutline;
    private Button btnAddOutline;
    private Button btnDeleteTag;
    private Button btnAddTag;


    //updating course
    private RequestQueue requestQueue;
    private String updateURL = "https://lamp.ms.wits.ac.za/home/s2105624/courseUpdate.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);

        Toast.makeText(EditCourse.this, ""+COURSE.FACULTY, Toast.LENGTH_SHORT).show();

        //linking views
        image = findViewById(R.id.courseEditImage);
        btnEdit = findViewById(R.id.buttonEditCourse);
        rgVisibility = findViewById(R.id.rgVisibility);
        rbPrivate = findViewById(R.id.privateVisibility);
        rbPublic = findViewById(R.id.publicVisibility);
        name = findViewById(R.id.courseName);
        outlineLayout = findViewById(R.id.courseOutline);
        tagLayout = findViewById(R.id.courseTag);
        description = findViewById(R.id.courseDescription);
        addOutline = findViewById(R.id.buttonAddCourseOutline);
        addTag =  findViewById(R.id.buttonAddTag);


        //setting views
        name.getEditText().setText(COURSE.NAME);
        description.getEditText().setText(COURSE.DESCRIPTION);
        if(!COURSE.IMAGE.equals("null")){
            Glide.with(this).load(COURSE.IMAGE).into(image);
        }
        //store all current tags
        allTags = new ArrayList<String>(Arrays.asList(COURSE.TAGS.split(";")));
       // arrayAdapterTag = new ArrayAdapter<String>(this,R.layout.outline_topic_entry,currentTags);
        if(COURSE.OUTLINE.length()>0) {
            addOutlineTopics(COURSE.OUTLINE);
        }
        //store all current outlines
        allOutlines = new ArrayList<String>(Arrays.asList(COURSE.OUTLINE.split(";")));
        if(COURSE.TAGS.length()>0) {
            addTags(COURSE.TAGS);
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

        PHPRequest req = new PHPRequest("https://lamp.ms.wits.ac.za/home/s2105624/");

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

        //Add course outline button on click
        addOutline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAddOutline("",0);
            }
        });

        //Add course tag button on click
        addTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAddTag("",0);
            }
        });


        //edit button on click
        boolean validInput = true; //just using it for now to test course Images
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmpty(name) | isEmpty(description) | validateOutlineAndTag(allOutlines,allTags) ){

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
    void addOutlineTopics(String outline) {
        //this assume every modules is separated by a space
        String[] outlineTopics = outline.split(";");
        outlineLayout.removeAllViews();

        //for each topic create a card to come up:
        for(int i=0; i < outlineTopics.length; i++)
        {
            if(outlineTopics[i].length()>0) {
                View topicView = View.inflate(this, R.layout.outline_topic_entry, null);
                TextView topicName = topicView.findViewById(R.id.outlineTopic);


                int finalI = i;
                topicName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogAddOutline(outlineTopics[finalI], finalI);
                    }
                });
                topicName.setText("➤ " + outlineTopics[i]);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(10, 5, 10, 5);

                outlineLayout.addView(topicView, params);
            }
        }
    }
    void addTags(String tags) {
        //this assume every modules is separated by a space
        tagLayout.removeAllViews();
        String[] tagsList = tags.split(";");
        //for each topic create a card to come up:
        for(int i=0; i < tagsList.length; i++)
        {
            if(tagsList[i].length()>0) {
                View topicView = View.inflate(this, R.layout.tag, null);
                TextView topicName = topicView.findViewById(R.id.tag);

                topicName.setText(tagsList[i]);
                int finalI = i;
                topicName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogAddTag(topicName.getText().toString(), finalI);
                    }
                });

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(10, 5, 10, 5);

                tagLayout.addView(topicView, params);

            }
        }
    }
    public void dialogAddOutline(String currentOutline,int index){
        dialogBuilder = new AlertDialog.Builder(this);
        final View viewPopUp = LayoutInflater.from(this)
                .inflate(R.layout.add_outline_dialog, null);

        btnAddOutline = (Button) viewPopUp.findViewById(R.id.addOutlne);
        btnDeleteOutline = (Button) viewPopUp.findViewById(R.id.deleteOutline);
        outline = (TextInputLayout) viewPopUp.findViewById(R.id.courseOutline);
        dialogBuilder.setView(viewPopUp);
        dialog = dialogBuilder.create();
        dialog.show();
        //Editing
        if(currentOutline.length()>0){
            btnDeleteOutline.setText("delete");
            btnAddOutline.setText("Save");
            outline.getEditText().setText(currentOutline);
            btnAddOutline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (validateCourseOutlineOrTag(outline, allOutlines)&&!outline.getEditText().getText().toString().equals(currentOutline)) {
                        //error dislayes
                    } else {
                        //change outline
                        allOutlines.set(index,outline.getEditText().getText().toString());
                        addOutlineTopics(convert(allOutlines));
                        dialog.dismiss();
                    }

                }
            });
            btnDeleteOutline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    allOutlines.remove(index);
                    addOutlineTopics(convert(allOutlines));
                    dialog.dismiss();
                }
            });
        }
        //Adding new outline
        else {
            btnAddOutline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (validateCourseOutlineOrTag(outline, allOutlines)) {
                        //error dislayes
                    } else {
                        allOutlines.add(outline.getEditText().getText().toString());
                        addOutlineTopics(convert(allOutlines));
                        dialog.dismiss();
                    }

                }
            });
            btnDeleteOutline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();
                }
            });
        }

    }
    public void dialogAddTag(String currentTag,int index){
        dialogBuilder = new AlertDialog.Builder(this);
        final View viewPopUp = LayoutInflater.from(this)
                .inflate(R.layout.add_tag_dialog, null);

        btnAddTag = (Button) viewPopUp.findViewById(R.id.addTag);
        btnDeleteTag = (Button) viewPopUp.findViewById(R.id.deleteTag);
        tag = (TextInputLayout) viewPopUp.findViewById(R.id.courseTag) ;

        dialogBuilder.setView(viewPopUp);
        dialog = dialogBuilder.create();
        dialog.show();
        //For editing
        if(currentTag.length()>0){
            btnDeleteTag.setText("Delete");
            btnAddTag.setText("Save");
            tag.getEditText().setText(currentTag);
            btnAddTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (validateCourseOutlineOrTag(tag, allTags)&&!tag.getEditText().getText().toString().equals(currentTag)) {
                        //error dislayes
                    } else {
                        allTags.set(index,tag.getEditText().getText().toString());
                        addTags(convert(allTags));
                        dialog.dismiss();
                    }
                }
            });
            btnDeleteTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    allTags.remove(index);
                    addTags(convert(allTags));
                    dialog.dismiss();
                }
            });
        }
        //for adding new tags
        else {
            btnAddTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (validateCourseOutlineOrTag(tag, allTags)) {
                        //error dislayes
                    } else {
                        allTags.add(tag.getEditText().getText().toString());
                        addTags(convert(allTags));
                        dialog.dismiss();
                    }
                }
            });
            btnDeleteTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
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
    public boolean validateOutlineAndTag(ArrayList<String>allOutlines,ArrayList<String>allTags){
        boolean invalid = false;
        if(allOutlines.size()<0){
            Toast toast = Toast.makeText(EditCourse.this, "Add at least one course outline topic", Toast.LENGTH_LONG);
            toast.show();
            invalid =true;
        }
        if(allTags.size()<0){
            Toast toast = Toast.makeText(EditCourse.this, "add at least one tag", Toast.LENGTH_LONG);
            toast.show();
            invalid = true;
        }
        return invalid;
    }
    //This function converts course tags/outline so that they can be split using ; when displayed in curse home page
    public String convert(ArrayList<String>list){
       String out = "";
       if(list.size()>0){ out = out+list.get(0);
        for(int i=1;i<list.size()-1;i++){
            out = out+";"+list.get(i);
        }
        if(list.size()>1) {
            out = out +";"+ list.get(list.size()-1);
        }}
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