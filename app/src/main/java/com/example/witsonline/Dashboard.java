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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;


public class Dashboard extends AppCompatActivity implements View.OnScrollChangeListener, BottomNavigationView.OnNavigationItemSelectedListener {
    TextView name;
    ProgressBar progressBar;

    //This is for the logout pop up menu
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button btnLogout, btnCancel;

    //CreatingViews
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

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

        LinearLayout featuredCourses = findViewById(R.id.dashboardFeaturedCourses);
        if (USER.STUDENT) {
            dashboardBottomNavigation.inflateMenu(R.menu.menu_student);
            dashboardBottomNavigation.getMenu().findItem(R.id.menuHomeStudent).setChecked(true);
            featuredCourses.setVisibility(LinearLayout.VISIBLE);

            displayFeaturedCourses();
        } else {
            dashboardBottomNavigation.inflateMenu(R.menu.menu_instructor);
            dashboardBottomNavigation.getMenu().findItem(R.id.menuHomeInstructor).setChecked(true);

            featuredCourses.setVisibility(LinearLayout.INVISIBLE);
        }

        if ((USER.FNAME != null) && (USER.LNAME != null)) {
            name.setText(USER.FNAME + " " + USER.LNAME);
        }

        progressBar = findViewById(R.id.dashboardProgressBar);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
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

    private void displayFeaturedCourses() {
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
}