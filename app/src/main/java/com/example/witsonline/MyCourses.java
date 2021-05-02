package com.example.witsonline;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.*;

@RequiresApi(api = Build.VERSION_CODES.M)
public class MyCourses extends AppCompatActivity implements View.OnScrollChangeListener, BottomNavigationView.OnNavigationItemSelectedListener {
    //Creating a list of Courses
    private ArrayList<CourseV> listCourseVs;

    //CreatingViews
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    String webURL = "https://lamp.ms.wits.ac.za/home/s2105624/loadCourses.php?page=";

    //Volley Request Queue
    private RequestQueue requestQueue;

    //The request counter to send ?page=1, ?page=2 requests
    private int courseCount = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_courses);

        //testing getting username
        Toast.makeText(MyCourses.this, USER.USERNAME, Toast.LENGTH_SHORT).show();

        //Initializing Views
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Initializing our Course list
        listCourseVs = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        //Calling method getData to fetch data
        getData();

        //Adding an scroll change listener to recyclerView
        recyclerView.setOnScrollChangeListener(MyCourses.this);

        //initializing our adapter
        adapter = new CourseCardAdapter(listCourseVs, this);

        //Adding adapter to recyclerview
        recyclerView.setAdapter(adapter);

        BottomNavigationView dashboardBottomNavigation = findViewById(R.id.dashboardBottomNavigation);
        dashboardBottomNavigation.setOnNavigationItemSelectedListener(MyCourses.this);

        if (USER.STUDENT) {
            dashboardBottomNavigation.inflateMenu(R.menu.menu_student);
            dashboardBottomNavigation.getMenu().findItem(R.id.menuMyCoursesStudent).setChecked(true);
        } else {
            dashboardBottomNavigation.inflateMenu(R.menu.menu_instructor);
            dashboardBottomNavigation.getMenu().findItem(R.id.menuMyCoursesInstructor).setChecked(true);
        }

        //get courses and convert from JSON
        USER.COURSES = new ArrayList<String>();

        if (USER.STUDENT){
            settakings();
        }
        else{
            setmakings();
        }

        try {
            Thread.sleep(1500);
        }
        catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        thels(COURSE.temp);
    }

    //Request to get json from server we are passing an integer here
    //This integer will used to specify the page number for the request ?page = requestcount
    //This method would return a JsonArrayRequest that will be added to the request queue
    private JsonArrayRequest getDataFromServer(int requestCount){
        //Initializing progressbar
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar1);

        //Displaying ProgressBar
        progressBar.setVisibility(View.VISIBLE);
        setProgressBarIndeterminateVisibility(true);

        //JsonArrayRequest of volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(webURL + String.valueOf(requestCount),
                (response) -> {
                    //Calling method parseData to parse the json responce
                    try {
                        parseData(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //Hiding the progressBar
                    progressBar.setVisibility(View.GONE);
                },
                (error) -> {
                    progressBar.setVisibility(View.GONE);
                    //If an error occurs that means end of the list has been reached
                    Toast.makeText(MyCourses.this, "No More Items Available", Toast.LENGTH_SHORT).show();
                });
        //Returning the request
        return jsonArrayRequest;
    }

    //This method will get Data from the web api
    private void getData(){
        //Adding the method to the queue by calling the method getDatafromServer
        requestQueue.add(getDataFromServer(courseCount));
        //Incrementing the course counter
        courseCount++;
    }

    //This method will parse json Data
    private void parseData(JSONArray array) throws JSONException {
        for (int i = 0; i< array.length(); i++) {
            // Creating the Course object
            CourseV courseV = new CourseV();
            JSONObject json = null;
            try {
                //Getting json
                json = array.getJSONObject(i);

                //Adding data to the course object
                courseV.setCourseName(json.getString("courseName"));
                courseV.setCourseDescription(json.getString("courseDescription"));
                courseV.setCourseInstructor(json.getString("courseInstructor"));
            } catch (JSONException e) {
                e.printStackTrace();
            }


            //Adding the request object to the list
            if (USER.STUDENT) {
                if (USER.COURSES.contains("courseCode")) {
                    listCourseVs.add(courseV);
                    adapter.notifyDataSetChanged();
                }
            } else {
                if (USER.USERNAME.equals(json.getString("courseInstructor"))) {
                    listCourseVs.add(courseV);
                    adapter.notifyDataSetChanged();
                }
            }

            //listCourseVs.add(courseV);

            //Notifying the adapter that data has been added or changed
            //adapter.notifyDataSetChanged();
        }
    }

    public void settakings(){

        OkHttpClient client = new OkHttpClient();

        RequestBody addinf=new FormBody.Builder()
                .add("unum", (USER.USER_NUM))
                .build();
        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2105624/lstakings.php")
                .post(addinf)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException exc) {
                exc.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                if (response.isSuccessful()) {
                    COURSE.temp=(response.body().string());
                } else {
                    throw new IOException("Unexpected code " + response);
                }
            }
        });
    }

    public void setmakings(){

        OkHttpClient client = new OkHttpClient();

        RequestBody addinf=new FormBody.Builder()
                .add("unum", (USER.USERNAME))
                .build();
        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2105624/lsmakings.php")
                .post(addinf)
                .build();
       // "SELECT COURSE_CODE FROM COURSE WHERE INSTRUCTOR_USERNAME='$unum'"
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException exc) {
                exc.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {

                if (response.isSuccessful()) {
                    COURSE.temp=(response.body().string());
                } else {
                    throw new IOException("Unexpected code " + response);
                }
            }
        });
    }

    public void thels(String json){
        try {
            JSONArray all = new JSONArray(json);
            for (int i=0; i<all.length(); i++){
                JSONObject item=all.getJSONObject(i);
                USER.COURSES.add(item.getString("COURSE_CODE"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //This method will check if the recyclerview has reached the bottom or not
    private boolean isLastItemDistplaying(RecyclerView recyclerView){
        if(recyclerView.getAdapter().getItemCount() != 0){
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() -1){
                return true;
            }
        }
        return false;
    }

    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        //if Scrolled at last then
        if(isLastItemDistplaying(recyclerView)){
            //Calling the method getData again
            getData();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            // STUDENT MENU
            case R.id.menuHomeStudent :
                Intent intent = new Intent(MyCourses.this, Dashboard.class);
                startActivity(intent);
                finish();
                break;

            case R.id.menuMyCoursesStudent :
                break;

            case R.id.menuBrowseCourses :
                Intent intent1 = new Intent(MyCourses.this, BrowseCourses.class);
                startActivity(intent1);
                finish();
                break;

            case R.id.menuLogOutStudent :
                Intent intent2 = new Intent(MyCourses.this, LoginActivity.class);
                startActivity(intent2);
                finish();
                break;

            case R.id.menuHomeInstructor :
                Intent intent3 = new Intent(MyCourses.this, Dashboard.class);
                startActivity(intent3);
                finish();
                break;

            case R.id.menuMyCoursesInstructor :
                break;

            case R.id.menuCreateCourse :
                Intent intent4 = new Intent(MyCourses.this, CreateCourse.class);
                startActivity(intent4);
                finish();
                break;

            case R.id.menuLogOutInstructor :
                Intent intent5 = new Intent(MyCourses.this,LoginActivity.class);
                startActivity(intent5);
                finish();
                break;
        }

        return false;
    }
}