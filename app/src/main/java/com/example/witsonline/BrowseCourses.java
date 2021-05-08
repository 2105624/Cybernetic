package com.example.witsonline;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.M)
public class BrowseCourses extends AppCompatActivity implements View.OnScrollChangeListener, BottomNavigationView.OnNavigationItemSelectedListener  {
    //Creating a list of Courses
    private ArrayList<CourseV> listCourseVs;

    //This is for the logout pop up menu
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button btnLogout, btnCancel;

    //CreatingViews
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    String webURL = "https://lamp.ms.wits.ac.za/home/s2105624/courseFeed.php?page=";

    //Volley Request Queue
    private RequestQueue requestQueue;
    
    //The request counter to send ?page=1, ?page=2 requests
    private int courseCount = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_courses);



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
        recyclerView.setOnScrollChangeListener(this);
        
        //initializing our adapter
        adapter = new CourseCardAdapter(listCourseVs, this);
        
        //Adding adapter to recyclerview
        recyclerView.setAdapter(adapter);

        /*
        USER.COURSES = new ArrayList<String>();

        USER.COURSES.add("COMS1018A");
        USER.COURSES.add("COMS3007A");
        USER.COURSES.add("DUMMY1000");
        */

        BottomNavigationView dashboardBottomNavigation = findViewById(R.id.dashboardBottomNavigation);
        dashboardBottomNavigation.setOnNavigationItemSelectedListener(BrowseCourses.this);

        dashboardBottomNavigation.inflateMenu(R.menu.menu_student);
        dashboardBottomNavigation.setSelectedItemId(R.id.menuBrowseCourses);
        dashboardBottomNavigation.getMenu().findItem(R.id.menuBrowseCourses).setChecked(true);
    }

    //Request to get json from server we are passing an integer here
    //This integer will used to specify the page number for the request ?page = requestCount
    //This method would return a JsonArrayRequest that will be added to the request queue
    private JsonArrayRequest getDataFromServer(int requestCount){
        //Initializing progressbar
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        
        //Displaying ProgressBar
        progressBar.setVisibility(View.VISIBLE);
        setProgressBarIndeterminateVisibility(true);

        //JsonArrayRequest of volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(webURL + String.valueOf(requestCount)+"&studentNo="+USER.USER_NUM,
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
                    Toast.makeText(BrowseCourses.this, "No More Items Available", Toast.LENGTH_SHORT).show();
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
                courseV.setCourseName(json.getString("courseName"));
                courseV.setCourseDescription(json.getString("courseDescription"));
                courseV.setCourseInstructor(json.getString("courseInstructor"));
                courseV.setCourseCode(json.getString("courseCode"));
                courseV.setCourseRating(json.getString("courseRating"));
                courseV.setCourseOutline(json.getString("courseOutline"));
                courseV.setImageUrl(json.getString("courseImageUrl"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Adding the request object to the list
        //    if (json.getString("courseInstructor").equals("richard.klein")) {
                listCourseVs.add(courseV);
                adapter.notifyDataSetChanged();
       //     }

            /*
            if ((USER.COURSES != null) && (USER.COURSES.contains(json.getString("courseCode")))) {
                listCourseVs.add(courseV);
                adapter.notifyDataSetChanged();
            }
            */

            //listCourseVs.add(courseV);

            //Notifying the adapter that data has been added or changed
            //adapter.notifyDataSetChanged();
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

    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        //if Scrolled at last then
        if(isLastItemDistplaying(recyclerView)){
            //Calling the method getData again
            getData();
        }
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
                Intent intent5 = new Intent(BrowseCourses.this,LoginActivity.class);
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
            case R.id.menuHomeStudent :
                Intent intent = new Intent(BrowseCourses.this, Dashboard.class);
                startActivity(intent);
                finish();
                break;

            case R.id.menuMyCoursesStudent :
                Intent intent1 = new Intent(BrowseCourses.this, MyCourses.class);
                startActivity(intent1);
                finish();
                break;

            case R.id.menuBrowseCourses :
                break;

            case R.id.menuLogOutStudent :
                createNewViewDialog();
                break;
        }

        return false;
    }
    @Override
    public void onBackPressed(){

    }

}