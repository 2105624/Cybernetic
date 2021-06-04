package com.example.witsonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CourseHomePageInstructor extends AppCompatActivity implements  View.OnScrollChangeListener{
    LinearLayout outlineLayout;
    boolean browse;
    //Creating a list of Courses
    private ArrayList<ReviewV> listReviewVs;

    //Creating a list of tags
    private ArrayList<String> tags;
    private ArrayList<TagV> listTagVs;

    private ImageView image;
    private TextView courseName;
    private TextView courseDescription;
    private TextView courseInstructor;
    private RatingBar courseRating;

    //This is for the unsubscribe pop up menu
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button btnUnsubscribe, btnCancel;

    //This is for the review pop up menu
    private EditText edtReviewDescription;
    private RatingBar rtbReviewRating;
    private Button btnReviewPostReview;

    //This is for the add tag pop up menu
    private TextView DialogueInstruction;

    //this if for adding a tag
    private Button tagAdd;

    //CreatingViews
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewTags;
    private RecyclerView.LayoutManager layoutManagerTags;
    private RecyclerView.Adapter adapterTags;
    String webURL = "https://lamp.ms.wits.ac.za/home/s2105624/loadReviews.php?page=";
    String tagURL = "https://lamp.ms.wits.ac.za/home/s2105624/tagretrieve.php?ccode=";
    String courseURL = "https://lamp.ms.wits.ac.za/home/s2105624/getCourseInfo.php?ccode=";

    //Volley Request Queue
    private RequestQueue requestQueue;

    //button for a lesson
    private Button btnAddLesson;
    private Button btnViewLesson;

    //button for editing a course
    private ImageView imgEditCourse;

    //This is for the delay while loading correct course info
    private ProgressBar progressBar;
    private RelativeLayout relativeLayout;

    //The request counter to send ?page=1, ?page=2 requests
    private int reviewCount = 1;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_course_home_page_instructor);
        super.onCreate(savedInstanceState);
        courseName =(TextView)findViewById(R.id.courseName);
        courseDescription =(TextView)findViewById(R.id.courseDescription);
        courseInstructor =(TextView)findViewById(R.id.courseInstructor);
        courseRating = (RatingBar)findViewById(R.id.courseRating);
        outlineLayout = findViewById(R.id.courseOutline);
        image = (ImageView)findViewById(R.id.courseImage);
        tagAdd = (Button)findViewById(R.id.tagadd);

        //Initializing Views
        recyclerView = (RecyclerView)findViewById(R.id.reviewRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerViewTags = (RecyclerView)findViewById(R.id.tagsRecyclerViewInstructor);
        recyclerViewTags.setHasFixedSize(true);
        layoutManagerTags = new LinearLayoutManager(this);
        recyclerViewTags.setLayoutManager(layoutManagerTags);

        //Initializing our Course list
        listReviewVs = new ArrayList<>();
        listTagVs = new ArrayList<>();
        tags = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        //Adding an scroll change listener to recyclerView
        recyclerView.setOnScrollChangeListener(this);
        recyclerViewTags.setOnScrollChangeListener(this);

        //initializing our adapters
        adapter = new ReviewCardAdapter(listReviewVs, this);
        adapterTags = new TagAdapter(listTagVs, this);

        //Adding adapter to recyclerviews's
        recyclerView.setAdapter(adapter);
        recyclerViewTags.setAdapter(adapterTags);

        //Calling methods getData to fetch data
        getData();
        getTagData();
        getCourseData();

        //The setting of views happens within the parseCourseData function now
        /*//set views
        if(!COURSE.IMAGE.equals("null")){
            Glide.with(this).load(COURSE.IMAGE).into(image);
        }
        courseName.setText(COURSE.NAME);
        courseDescription.setText(COURSE.DESCRIPTION);
        courseInstructor.setText("By: "+COURSE.INSTRUCTOR);
        courseRating.setRating(Float.parseFloat(COURSE.RATING));
        addOutlineTopics(COURSE.OUTLINE); */

        //Adding on-click for adding a lesson
        btnAddLesson = findViewById(R.id.addLesson);
        btnAddLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseHomePageInstructor.this, CreateLesson.class);
              //  intent.putExtra("activity","instructor");
                startActivity(intent);
              //  finish();
            }
        });

        btnViewLesson = findViewById(R.id.viewLessons);
        btnViewLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseHomePageInstructor.this, BrowseLessons.class);
                startActivity(intent);
               // finish();
            }
        });
        tagAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                createNewViewTagReview();
            }
        });

        //edit course
        imgEditCourse = findViewById(R.id.editCourse);
        imgEditCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseHomePageInstructor.this, EditCourse.class);
                startActivity(intent);
                finish();
            }
        });
        //Progress bar for the whole page and the page's relative layout
        progressBar = findViewById(R.id.progressBar);
        relativeLayout = findViewById(R.id.CourseHomeInstructorLayout);
        progressBar.setVisibility(View.VISIBLE);
    }
    private JsonArrayRequest getDataFromServer(int requestCount){
        //Initializing progressbar
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.reviewProgressBar);

        //Displaying ProgressBar
        progressBar.setVisibility(View.VISIBLE);
        setProgressBarIndeterminateVisibility(true);

        //JsonArrayRequest of volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(webURL + String.valueOf(requestCount) + "&courseCode=" + COURSE.CODE,
                (response) -> {
                    //Calling method parseData to parse the json responce
                    parseData(response);
                    //Hiding the progressBar
                    progressBar.setVisibility(View.GONE);
                },
                (error) -> {
                    progressBar.setVisibility(View.GONE);
                    //If an error occurs that means end of the list has been reached
                    //Toast.makeText(CourseHomePage.this, "No More Items Available", Toast.LENGTH_SHORT).show();
                });
        //Returning the request
        return jsonArrayRequest;
    }
    private JsonArrayRequest getTagDataFromServer(){
        Log.i("method", "getTagDataFromServer() called");
        //Initializing progressbar
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.tagsProgressBarInstructor);

        //Displaying ProgressBar
        progressBar.setVisibility(View.VISIBLE);
        setProgressBarIndeterminateVisibility(true);

        //JsonArrayRequest of volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(tagURL + COURSE.CODE,
                (response) -> {
                    //Creating an array of tags making use of taglist method
                    String[] taglist=taglist(response);
//                    Log.i("Length", "Length: "+Integer.toString(taglist.length));
                    for (int i=0; i<taglist.length; i++){
                        tags.add(taglist[i]);
                        Log.i(Integer.toString(i), taglist[i]);
                    }
                    //Hiding the progressBar
                    progressBar.setVisibility(View.GONE);
                },
                (error) -> {
                    progressBar.setVisibility(View.GONE);
                    //If an error occurs that means end of the list has been reached
                    //Toast.makeText(CourseHomePage.this, "No More Items Available", Toast.LENGTH_SHORT).show();
                });
        //Returning the request
        return jsonArrayRequest;
    }

    private JsonArrayRequest getCourseDataFromServer(){
        //Initializing progressbar
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.tagsProgressBarInstructor);

        //Displaying ProgressBar
        progressBar.setVisibility(View.VISIBLE);
        setProgressBarIndeterminateVisibility(true);

        //JsonArrayRequest of volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(courseURL + COURSE.CODE,
                (response) -> {
                    //parse JSON response in parseCourseData method
                    parseCourseData(response);
                    //Hiding the progressBar
                    progressBar.setVisibility(View.GONE);
                },
                (error) -> {
                    progressBar.setVisibility(View.GONE);
                    //If an error occurs that means end of the list has been reached
                    //Toast.makeText(CourseHomePage.this, "No More Items Available", Toast.LENGTH_SHORT).show();
                });
        //Returning the request
        return jsonArrayRequest;
    }

    //These methods will get Data from the web api
    private void getData(){
        //Adding the method to the queue by calling the method getDatafromServer
        requestQueue.add(getDataFromServer(reviewCount));
        //Incrementing the course counter
        reviewCount++;
    }
    private void getTagData(){
        //Adding the method to the queue by calling the method getTagData
        requestQueue.add(getTagDataFromServer());
    }
    private void getCourseData(){
        //Adding the method to the queue by calling the method getTagData
        requestQueue.add(getCourseDataFromServer());
    }

    //This method will parse json Data
    private String[] taglist(JSONArray all){
        String[] tagTemps = null;
        for(int i = 0; i< all.length(); i++){
            // Tag object
            JSONObject json = null;
            try {
                //getting json
                JSONObject item = all.getJSONObject(i);

                //Adding data to tag object
                COURSE.TAGS = item.getString("Course_Tags");
                String[] tagtemp = COURSE.TAGS.split(";");
                tagTemps = tagtemp;
                for(String j : tagtemp){
                    TagV tagV = new TagV();
                    tagV.setTag(j);
                    if(!j.equals(null) && j.length()>0){
                        listTagVs.add(tagV);
                    }
                }

            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapterTags.notifyDataSetChanged();
        return tagTemps;
    }

    //This method will parse json Data for course
    private void parseCourseData(JSONArray array){
        for (int i = 0; i< array.length(); i++){
            // Creating the Course object
            JSONObject json = null;
            try {
                //Getting json
                json = array.getJSONObject(i);

                //Adding data to the course object
                COURSE.NAME = json.getString("Course_Name");
                COURSE.DESCRIPTION = json.getString("Course_Description");
                COURSE.OUTLINE = json.getString("Course_Outline");
                COURSE.IMAGE = json.getString("Course_Image");
                COURSE.TAGS = json.getString("Course_Tags");
                COURSE.FACULTY = json.getInt("Course_Faculty");
                //set views
                if(!COURSE.IMAGE.equals("null")){
                    Glide.with(this).load(COURSE.IMAGE).into(image);
                }
                courseName.setText(COURSE.NAME);
                courseDescription.setText(COURSE.DESCRIPTION);
                courseInstructor.setText("By: "+COURSE.INSTRUCTOR);
                courseRating.setRating(Float.parseFloat(COURSE.RATING));
                addOutlineTopics(COURSE.OUTLINE);

                //Make the page visible after displaying the correct course info
                relativeLayout.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    //This method will parse json Data
    private void parseData(JSONArray array){
        for (int i = 0; i< array.length(); i++){
            // Creating the Course object
            ReviewV reviewV = new ReviewV();
            JSONObject json = null;
            try {
                //Getting json
                json = array.getJSONObject(i);

                //Adding data to the course object
                reviewV.setStudentFName(json.getString("reviewStudentFName"));
                reviewV.setStudentLName(json.getString("reviewStudentLName"));
                reviewV.setReviewRating(json.getString("reviewRating"));
                reviewV.setReviewDescription(json.getString("reviewDescription"));
            } catch (JSONException e){
                e.printStackTrace();
            }
            //Adding the request object to the list
            listReviewVs.add(reviewV);
        }
        //Notifying the adapter that data has been added or changed
        adapter.notifyDataSetChanged();
    }
    //This method will check if the recyclerview has reached the bottom or not
    private boolean isLastItemDisplaying(RecyclerView recyclerView){
        if(recyclerView.getAdapter().getItemCount() != 0){
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() -1){
                return true;
            }
        }
        return false;
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

                topicName.setText("➤ " + outlineTopics[i]);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(10, 0, 10, 0);

                outlineLayout.addView(topicView, params);
            }
        }
    }
    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        //if Scrolled at last then
        if(isLastItemDisplaying(recyclerView)){
            //Calling the method getData again
            getData();
        }
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(CourseHomePageInstructor.this,MyCourses.class);
        startActivity(i);
        finish();
    }

    public void createNewViewTagReview(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View viewPopUp = LayoutInflater.from(this)
                .inflate(R.layout.review_dialog, null);

        dialogBuilder.setView(viewPopUp);
        dialog = dialogBuilder.create();
        dialog.show();
        DialogueInstruction = viewPopUp.findViewById(R.id.dialogTitle);
        DialogueInstruction.setText("Please Enter Tag Below");
        btnReviewPostReview = (Button) viewPopUp.findViewById(R.id.dialogPostReview);
        btnReviewPostReview.setText("Add");
        rtbReviewRating = (RatingBar) viewPopUp.findViewById(R.id.dialogCourseRating);
        rtbReviewRating.setVisibility (View.GONE);
        edtReviewDescription = (EditText) viewPopUp.findViewById(R.id.dialogCourseDescription);
        edtReviewDescription.setHint("Tag Description");

        btnReviewPostReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newtag= edtReviewDescription.getText().toString();
                Log.i("newtag", "newtag = "+newtag);
                if(newtag.isEmpty()){
                    Toast toast = Toast.makeText(CourseHomePageInstructor.this, "Please Insert Tag Name", Toast.LENGTH_LONG);
                    toast.show();
                }
                else {
                    try {
                        doTagAdd("taginsert.php", newtag);
                        dialog.dismiss();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }
    private void doTagAdd(String phpFile, String tag)throws IOException {

        Log.i("newtag", "doTagAdd called");
        Log.i("newtag", COURSE.TAGS);
        String alltags;
        if(COURSE.TAGS == "null" || COURSE.TAGS.equals("null")) {
            alltags = tag;
        }
        else{
            alltags=COURSE.TAGS+";"+tag;
        }

        Log.i("newtag", "alltags = "+alltags);
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://lamp.ms.wits.ac.za/home/s2105624/" + phpFile).newBuilder();
        urlBuilder.addQueryParameter("ccode",COURSE.CODE);
        urlBuilder.addQueryParameter("tags", alltags);
        String url = urlBuilder.build().toString();
        Log.i("newtag", url);

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Log.i("newtag", "it failed");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                Log.i("newtag", "it passed");
                CourseHomePageInstructor.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                        Toast toast = Toast.makeText(CourseHomePageInstructor.this, "Tag posted", Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
            }
        });
    }
}