package com.example.witsonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
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

public class CourseHomePage extends AppCompatActivity implements  View.OnScrollChangeListener{
    LinearLayout outlineLayout;
    Button subscribe;
    Button review;
    Button tagadd;
    boolean browse;
    //Creating a list of Courses
    private ArrayList<ReviewV> listReviewVs;
    private ArrayList<String> tags;
    private ImageView image;
    //This is for the unsubscribe pop up menu
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button btnUnsubscribe, btnCancel;

    //This is for the review pop up menu
    private EditText edtReviewDescription;
    private RatingBar rtbReviewRating;
    private Button btnReviewPostReview;
    private TextView DialogueInstruction;

    //CreatingViews
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewTags;
    private RecyclerView.LayoutManager layoutManagerTags;
    private RecyclerView.Adapter adapterTags;
    String webURL = "https://lamp.ms.wits.ac.za/home/s2105624/loadReviews.php?page=";
    String tagURL = "https://lamp.ms.wits.ac.za/home/s2105624/tagretrieve.php?ccode=";

    //Volley Request Queue
    private RequestQueue requestQueue;

    //The request counter to send ?page=1, ?page=2 requests
    private int reviewCount = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_course_home_page);
        super.onCreate(savedInstanceState);
        TextView courseName =(TextView)findViewById(R.id.courseName);
        TextView courseDescription =(TextView)findViewById(R.id.courseDescription);
        TextView courseInstructor =(TextView)findViewById(R.id.courseInstructor);
        RatingBar courseRating = (RatingBar)findViewById(R.id.courseRating);
        subscribe = (Button)findViewById(R.id.subscribe);
        outlineLayout = findViewById(R.id.courseOutline);
        review = (Button)findViewById(R.id.review);
        tagadd = (Button)findViewById(R.id.tagadd);
        image = (ImageView)findViewById(R.id.courseImage);

        //To determine which activity we came from (BrowseCourses or MyCourses
        Bundle extras = getIntent().getExtras();
        String act = extras.getString("activity");
        if (act.contains("BrowseCourses")){
            browse = true;
        }
        else{
            browse = false;
        }
      //  Toast.makeText(CourseHomePage.this, act, Toast.LENGTH_LONG).show();

        if(!COURSE.IMAGE.equals("null")){
            Glide.with(this).load(COURSE.IMAGE).into(image);
        }
        courseName.setText(COURSE.NAME);
        courseDescription.setText(COURSE.DESCRIPTION);
        courseInstructor.setText("By: "+COURSE.INSTRUCTOR);
        courseRating.setRating(Float.parseFloat(COURSE.RATING));
        addOutlineTopics(COURSE.OUTLINE);

        //Initializing Views
        recyclerView = (RecyclerView)findViewById(R.id.reviewRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerViewTags = (RecyclerView)findViewById(R.id.tagsRecyclerView);
        recyclerViewTags.setHasFixedSize(true);
        layoutManagerTags = new LinearLayoutManager(this);
        recyclerViewTags.setLayoutManager(layoutManagerTags);

        //Initializing our Course list
        listReviewVs = new ArrayList<>();
        tags = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        //Calling methods to get data from server
        getData();
        getTagData();

        //Adding an scroll change listener to recyclerView
        recyclerView.setOnScrollChangeListener(this);
        recyclerViewTags.setOnScrollChangeListener(this);

        //initializing our adapters
        adapter = new ReviewCardAdapter(listReviewVs, this);
        adapterTags = new TagAdapter(tags, this);

        //Adding adapter to recyclerview
        recyclerView.setAdapter(adapter);

        try {
            doPostRequest("enrolment.php");
        } catch (IOException e) {
            e.printStackTrace();
        }
        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(subscribe.getText().toString().trim().equals("SUBSCRIBE")){
                    try {
                        doPostRequest("enrol.php");
                        subscribe.setText("SUBSCRIBED");
                        Toast toast = Toast.makeText(CourseHomePage.this, "Subscribed to "+COURSE.CODE, Toast.LENGTH_LONG);
                        toast.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    createNewViewDialog();
                }
            }
        });

        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewViewDialogReview();
            }

        });

        tagadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                createNewViewTagReview();
            }
        });
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
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.tagsProgressBar);

        //Displaying ProgressBar
        progressBar.setVisibility(View.VISIBLE);
        setProgressBarIndeterminateVisibility(true);

        //JsonArrayRequest of volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(tagURL + COURSE.CODE,
                (response) -> {
                    //Creating an array of tags making use of taglist method
                    String[] taglist=taglist(response);
                    Log.i("Length", "Length: "+Integer.toString(taglist.length));
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

    //This method will get Data from the web api
    private void getData(){
        //Adding the method to the queue by calling the method getDatafromServer
        requestQueue.add(getDataFromServer(reviewCount));
        //Incrementing the course counter
        reviewCount++;
    }

    private void getTagData(){
        //Adding the method to the queue by calling the method getTagData
        Log.i("method", "getTagData() called");
        requestQueue.add(getTagDataFromServer());
    }

    private String[] taglist(JSONArray all){
        try {
            JSONObject item = all.getJSONObject(0);

            COURSE.TAGS = (item.getString("Tags"));
        }catch (JSONException e) {
            e.printStackTrace();
        }
        String[] tagtemp = COURSE.TAGS.split(";");
        return tagtemp;
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
            View topicView = View.inflate(this, R.layout.outline_topic_entry ,null);
            TextView topicName = topicView.findViewById(R.id.outlineTopic);

            topicName.setText("➤ "+ outlineTopics[i]);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(10,0,10,0);

            outlineLayout.addView(topicView, params);
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
    private void doPostRequest(String phpFile) throws IOException {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://lamp.ms.wits.ac.za/~s2105624/" + phpFile).newBuilder();
        urlBuilder.addQueryParameter("studentNumber",USER.USERNAME);
        urlBuilder.addQueryParameter("courseCode", COURSE.CODE);
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                CourseHomePage.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(responseData.trim().equals("subscribed")){
                            subscribe.setText("SUBSCRIBED");
                        }
                        if(responseData.trim().equals("unsubscribed")){
                            subscribe.setText("SUBSCRIBE");
                            btnUnsubscribe.setText("SUBSCRIBE");
                        }
                    }
                });
            }
        });
    }
    public void createNewViewDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View viewPopUp = LayoutInflater.from(this)
                .inflate(R.layout.unsubscribe_dialog, null);

        btnUnsubscribe = (Button) viewPopUp.findViewById(R.id.unSubscribe);
        btnCancel = (Button) viewPopUp.findViewById(R.id.unsubscribeCancel);

        dialogBuilder.setView(viewPopUp);
        dialog = dialogBuilder.create();
        dialog.show();

        btnUnsubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    doPostRequest("unsubscribe.php");
                    Toast toast = Toast.makeText(CourseHomePage.this, "Unsubscribed to "+COURSE.CODE, Toast.LENGTH_LONG);
                    toast.show();
                    dialog.dismiss();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

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
                    Toast toast = Toast.makeText(CourseHomePage.this, "Please Insert Tag Name", Toast.LENGTH_LONG);
                    toast.show();
                }
                else {
                    try {
                        doTagAdd("tagInsert.php", newtag);
                        dialog.dismiss();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }
    public void createNewViewDialogReview(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View viewPopUp = LayoutInflater.from(this)
                .inflate(R.layout.review_dialog, null);

        dialogBuilder.setView(viewPopUp);
        dialog = dialogBuilder.create();
        dialog.show();

        btnReviewPostReview = (Button) viewPopUp.findViewById(R.id.dialogPostReview);
        rtbReviewRating = (RatingBar) viewPopUp.findViewById(R.id.dialogCourseRating);
        edtReviewDescription = (EditText) viewPopUp.findViewById(R.id.dialogCourseDescription);

        btnReviewPostReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float postReviewRating = rtbReviewRating.getRating();
                String postReviewDescription = edtReviewDescription.getText().toString();
                if(postReviewDescription.isEmpty()){
                    Toast toast = Toast.makeText(CourseHomePage.this, "Review description can't be empty", Toast.LENGTH_LONG);
                    toast.show();
                }
                else {
                    try {
                        doPostReview("addReview.php", postReviewRating, postReviewDescription);
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
        String alltags=COURSE.TAGS+";"+tag;
        Log.i("newtag", "alltags = "+alltags);
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://lamp.ms.wits.ac.za/s2105624/" + phpFile).newBuilder();
        urlBuilder.addQueryParameter("ccode",COURSE.CODE);
        urlBuilder.addQueryParameter("tags", alltags);
        String url = urlBuilder.build().toString();

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
                CourseHomePage.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                        Toast toast = Toast.makeText(CourseHomePage.this, "Tag posted", Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
            }
        });
    }

    private void doPostReview(String phpFile, float postReviewRating, String reviewDescription) throws IOException {
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://lamp.ms.wits.ac.za/~s2105624/" + phpFile).newBuilder();
        urlBuilder.addQueryParameter("studentNumber",USER.USERNAME);
        urlBuilder.addQueryParameter("courseCode", COURSE.CODE);
        urlBuilder.addQueryParameter("reviewRating", String.valueOf(postReviewRating));
        urlBuilder.addQueryParameter("reviewDescription", reviewDescription);
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                CourseHomePage.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateRatings(String.valueOf(postReviewRating),reviewDescription);
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                        Toast toast = Toast.makeText(CourseHomePage.this, "Review posted", Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
            }
        });
    }
    public void updateRatings(  String rating , String description){
        ReviewV reviewV = new ReviewV();
        reviewV.setReviewDescription(description);
        reviewV.setReviewRating(rating);
        reviewV.setStudentFName(USER.FNAME);
        reviewV.setStudentLName(USER.LNAME);
        listReviewVs.add(reviewV);
        float averageRating = 0;
        for(int i=0;i<listReviewVs.size();i++){
            averageRating = averageRating+Float.parseFloat(listReviewVs.get(i).getReviewRating());
        }
        COURSE.RATING = String.valueOf(averageRating/listReviewVs.size());
    }

    @Override
    public void onBackPressed(){
        if (browse){
            Intent intent = new Intent(CourseHomePage.this, BrowseCourses.class);
            startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent(CourseHomePage.this, MyCourses.class);
            startActivity(intent);
            finish();
        }
    }
}
