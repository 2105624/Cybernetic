package com.example.witsonline;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;


public class Dashboard extends AppCompatActivity implements View.OnScrollChangeListener, BottomNavigationView.OnNavigationItemSelectedListener {
    TextView name;
    //This is for the delay while loading user full name
    ProgressBar progressBar;
    private RelativeLayout relativeLayout; // for the entire page

    //delay for loading featured courses
    private ProgressBar progressBarFeatCourses;
    private LinearLayout featuredCourses;  // for the featured course


    //This is for the logout pop up menu
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button btnLogout, btnCancel;

    //CreatingViews
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    String URL = "https://lamp.ms.wits.ac.za/home/s2105624/";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBackPressed(){

    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        name = findViewById(R.id.textViewStudentName);

        //NO ACTION BAR ON THIS ACTIVITY
        Objects.requireNonNull(getSupportActionBar()).hide();

        //ADD COURSE CARD VIEWS TO THIS LINEAR LAYOUT
        LinearLayout dashboard = findViewById(R.id.dashboardLinearLayout);

        BottomNavigationView dashboardBottomNavigation = findViewById(R.id.dashboardBottomNavigation);
        dashboardBottomNavigation.setOnNavigationItemSelectedListener(Dashboard.this);

        featuredCourses = findViewById(R.id.dashboardFeaturedCourses);
        progressBarFeatCourses = findViewById(R.id.featCoursesProgressBar);
        if (USER.STUDENT) {
            dashboardBottomNavigation.inflateMenu(R.menu.menu_student);
            dashboardBottomNavigation.getMenu().findItem(R.id.menuHomeStudent).setChecked(true);
            featuredCourses.setVisibility(LinearLayout.VISIBLE);
            String getFeaturedCoursesMethod = "getFeaturedCourses";
            ArrayList<CourseV> courses = new ArrayList<CourseV>();
            getFeaturedCourses(URL, getFeaturedCoursesMethod, courses);
        } else {
            progressBarFeatCourses.setVisibility(View.GONE);
            dashboardBottomNavigation.inflateMenu(R.menu.menu_instructor);
            dashboardBottomNavigation.getMenu().findItem(R.id.menuHomeInstructor).setChecked(true);
            featuredCourses.setVisibility(LinearLayout.INVISIBLE);
        }

        //display the user's name and surname
        getName(USER.USER_NUM);

        progressBar = findViewById(R.id.dashboardProgressBar);
        relativeLayout = findViewById(R.id.dashboardRelLayout);
        progressBar.setVisibility(View.VISIBLE);
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
                    Intent intent5 = new Intent(Dashboard.this,LoginActivity.class);
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

    private void getName(String user) {
        String URL = "https://lamp.ms.wits.ac.za/home/s2105624/";

        String getStudentNameMethod = "getStudentName";
        String getInstructorNameMethod = "getInstructorName";

        //Initializing progressbar
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.dashboardProgressBar);

        //Displaying ProgressBar
        progressBar.setVisibility(View.VISIBLE);
        setProgressBarIndeterminateVisibility(true);

        if (USER.STUDENT) {
            PHPRequestBuilder requestBuilder = new PHPRequestBuilder(URL, getStudentNameMethod);

            ArrayList<String> parameter = new ArrayList<String>();
            parameter.add("Student_Number");
            parameter.add(user);

            ArrayList<ArrayList<String>> Parameters = new ArrayList<ArrayList<String>>();
            Parameters.add(parameter);

            requestBuilder.doBuild(Parameters);
            requestBuilder.doRequest(Dashboard.this, response -> addName(response));
        } else {
            PHPRequestBuilder requestBuilder = new PHPRequestBuilder(URL, getInstructorNameMethod);

            ArrayList<String> parameter = new ArrayList<String>();
            parameter.add("Instructor_Username");
            parameter.add(user);

            ArrayList<ArrayList<String>> Parameters = new ArrayList<ArrayList<String>>();
            Parameters.add(parameter);

            requestBuilder.doBuild(Parameters);
            requestBuilder.doRequest(Dashboard.this, response -> addName(response));
        }
    }

    private void addName(String JSON) throws JSONException {
        JSONObject NAMES = new JSONObject(JSON);

        String FName;
        String LName;

        if (USER.STUDENT) {
            FName = NAMES.getString("Student_FName");
            LName = NAMES.getString("Student_LName");
        } else {
            FName = NAMES.getString("Instructor_FName");
            LName = NAMES.getString("Instructor_LName");
        }

        USER.FNAME = FName;
        USER.LNAME = LName;

        name.setText(FName + " " + LName);
        progressBar.setVisibility(View.GONE);
        relativeLayout.setVisibility(View.VISIBLE);
    }

    private void displayFeaturedCourses() {
        //Initializing progressbar
        final ProgressBar progressBarFeatCourses = (ProgressBar) findViewById(R.id.featCoursesProgressBar);

        //Displaying ProgressBar
        progressBarFeatCourses.setVisibility(View.VISIBLE);
        setProgressBarIndeterminateVisibility(true);

        //Initializing Views
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Initializing our Course list
        //Creating a list of Courses
        if (USER.FEATURED_COURSES != null) {
            ArrayList<CourseV> listCourseVs = USER.FEATURED_COURSES;

            //Adding an scroll change listener to recyclerView
            recyclerView.setOnScrollChangeListener(this);

            //initializing our adapter
            adapter = new CourseCardAdapter(listCourseVs, this);

            //Adding adapter to recyclerview
            recyclerView.setAdapter(adapter);
        }

        progressBarFeatCourses.setVisibility(View.GONE);
        featuredCourses.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            // STUDENT MENU
            case R.id.menuHomeStudent :
                break;

            case R.id.menuMyCoursesStudent :
                Intent intent = new Intent(Dashboard.this, MyCourses.class);
                startActivity(intent);
                finish();
                break;

            case R.id.menuBrowseCourses :
                Intent intent1 = new Intent(Dashboard.this, BrowseCourses.class);
                startActivity(intent1);
                finish();
                break;

            case R.id.menuLogOutStudent :
                createNewViewDialog();
                break;

            case R.id.menuHomeInstructor :
                break;

            case R.id.menuMyCoursesInstructor :
                Intent intent3 = new Intent(Dashboard.this, MyCourses.class);
                startActivity(intent3);
                finish();
                break;

            case R.id.menuCreateCourse :
                Intent intent4 = new Intent(Dashboard.this, CreateCourse.class);
                startActivity(intent4);
                finish();
                break;

            case R.id.menuLogOutInstructor :
                createNewViewDialog();
                break;
        }

        return false;
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

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        //if Scrolled at last then
        if(isLastItemDistplaying(recyclerView)){
            //Calling the method getData again
        }
    }

    private void getFeaturedCourses(String URL, String method, ArrayList<CourseV> courses) {
        PHPRequestBuilder requestBuilder = new PHPRequestBuilder(URL, method);
        requestBuilder.doRequest(Dashboard.this, new ResponseHandler() {
            @Override
            public void processResponse(String response) throws JSONException {
                setFeaturedCourses(response, courses);
            }
        });

    }

    private void setFeaturedCourses(String JSON, ArrayList<CourseV> courses) throws JSONException {
        JSONArray featuredCourses = new JSONArray(JSON);
        for (int index = 0; index < featuredCourses.length(); index++) {
            JSONObject featuredCourse = featuredCourses.getJSONObject(index);
            CourseV course = new CourseV();

            course.setCourseCode(featuredCourse.getString("Course_Code"));
            course.setCourseName(featuredCourse.getString("Course_Name"));
            course.setCourseDescription(featuredCourse.getString("Course_Description"));
            course.setCourseOutline(featuredCourse.getString("Course_Outline"));
            course.setImageUrl(featuredCourse.getString("Course_Image"));
            course.setCourseRating(featuredCourse.getString("Course_Rating"));
            course.setCourseInstructor(featuredCourse.getString("Course_Instructor"));

            courses.add(course);
        }

        USER.FEATURED_COURSES = courses;
        displayFeaturedCourses();
    }
}