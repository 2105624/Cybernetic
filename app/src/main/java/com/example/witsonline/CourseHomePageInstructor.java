package com.example.witsonline;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CourseHomePageInstructor extends AppCompatActivity implements View.OnScrollChangeListener, BottomNavigationView.OnNavigationItemSelectedListener {

    LinearLayout outlineLayout;
    Button subscribe;
    Button review;
    private ArrayList<ReviewV> listReviewVs;
    private ImageView image;

    //CreatingViews
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    String webURL = "https://lamp.ms.wits.ac.za/home/s2105624/loadReviews.php?page=";

    //Volley Request Queue
    private RequestQueue requestQueue;

    //The request counter to send ?page=1, ?page=2 requests
    private int reviewCount = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_home_page_instructor);
        BottomNavigationView dashboardBottomNavigation = findViewById(R.id.dashboardBottomNavigation);
        dashboardBottomNavigation.setOnNavigationItemSelectedListener(CourseHomePageInstructor.this);
        dashboardBottomNavigation.inflateMenu(R.menu.menu_instructor);

        TextView courseName =(TextView)findViewById(R.id.courseName);
        TextView courseDescription =(TextView)findViewById(R.id.courseDescription);
        TextView courseInstructor =(TextView)findViewById(R.id.courseInstructor);
        RatingBar courseRating = (RatingBar)findViewById(R.id.courseRating);
        subscribe = (Button)findViewById(R.id.subscribe);
        outlineLayout = findViewById(R.id.courseOutline);
        review = (Button)findViewById(R.id.review);
        image = (ImageView)findViewById(R.id.courseImage);


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

        //Initializing our Course list
        listReviewVs = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        //Calling method getData to fetch data
        getData();

        //Adding an scroll change listener to recyclerView
        recyclerView.setOnScrollChangeListener(this);

        //initializing our adapter
        adapter = new ReviewCardAdapter(listReviewVs, this);
    }

    private boolean isLastItemDistplaying(RecyclerView recyclerView){
        if(recyclerView.getAdapter().getItemCount() != 0){
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() -1){
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuHomeInstructor :
                Intent intent = new Intent(CourseHomePageInstructor.this, Dashboard.class);
                startActivity(intent);
                finish();
                break;

            case R.id.menuMyCoursesInstructor :
                Intent intent1 = new Intent(CourseHomePageInstructor.this, MyCourses.class);
                startActivity(intent1);
                finish();
                break;

            case R.id.menuCreateCourse :
                Intent intent2 = new Intent(CourseHomePageInstructor.this, CreateCourse.class);
                startActivity(intent2);
                finish();
                break;

            case R.id.menuLogOutInstructor :
                Intent intent3 = new Intent(CourseHomePageInstructor.this, LoginActivity.class);
                startActivity(intent3);
                finish();
                break;
        }
        return false;
    }
    //Request to get json from server we are passing an integer here
    //This integer will used to specify the page number for the request ?page = requestCount
    //This method would return a JsonArrayRequest that will be added to the request queue
    private JsonArrayRequest getDataFromServer(int requestCount) {
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

    //This method will get Data from the web api
    private void getData() {
        //Adding the method to the queue by calling the method getDatafromServer
        requestQueue.add(getDataFromServer(reviewCount));
        //Incrementing the course counter
        reviewCount++;
    }

    //This method will parse json Data
    private void parseData(JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
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
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Adding the request object to the list
            listReviewVs.add(reviewV);
        }
        //Notifying the adapter that data has been added or changed
        adapter.notifyDataSetChanged();
    }

    //This method will check if the recyclerview has reached the bottom or not
    private boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1) {
                return true;
            }
        }
        return false;
    }
    void addOutlineTopics(String outline) {
        //this assume every modules is separated by a space
        String[] outlineTopics = outline.split(" ");

        outlineLayout.removeAllViews();

        //for each topic create a card to come up:
        for (int i = 0; i < outlineTopics.length; i++) {
            View topicView = View.inflate(this, R.layout.outline_topic_entry, null);
            TextView topicName = topicView.findViewById(R.id.outlineTopic);

            topicName.setText("➤ " + outlineTopics[i]);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 0, 10, 0);

            outlineLayout.addView(topicView, params);
        }
    }

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        //if Scrolled at last then
       if (isLastItemDisplaying(recyclerView)) {
            //Calling the method getData again
            getData();
        }
    }
}