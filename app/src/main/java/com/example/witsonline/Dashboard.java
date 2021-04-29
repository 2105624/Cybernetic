package com.example.witsonline;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;


public class Dashboard extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    TextView name;
    ProgressBar progressBar;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBackPressed(){

    }
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

        if (USER.STUDENT) {
            dashboardBottomNavigation.inflateMenu(R.menu.menu_student);
            dashboardBottomNavigation.setSelectedItemId(R.id.menuHomeStudent);

        } else {
            dashboardBottomNavigation.inflateMenu(R.menu.menu_instructor);
            dashboardBottomNavigation.setSelectedItemId(R.id.menuHomeInstructor);
        }

        progressBar = findViewById(R.id.dashboardProgressBar);
        progressBar.setVisibility(View.VISIBLE);

        getName(USER.USER_NUM);
    }

    private void getName(String user) {
        String URL = "https://lamp.ms.wits.ac.za/home/s2105624/";

        String getStudentNameMethod = "getStudentName";
        String getInstructorNameMethod = "getInstructorName";

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
                Intent intent2 = new Intent(Dashboard.this, LoginActivity.class);
                startActivity(intent2);
                finish();
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
                Intent intent5 = new Intent(Dashboard.this,LoginActivity.class);
                startActivity(intent5);
                finish();
                break;
        }

        return false;
    }
}