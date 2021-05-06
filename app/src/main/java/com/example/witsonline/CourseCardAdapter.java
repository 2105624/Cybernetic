package com.example.witsonline;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CourseCardAdapter extends RecyclerView.Adapter<CourseCardAdapter.ViewHolder> {
    private Context context;
    private ImageView image;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Button btnViewDialogSubscribe, btnViewDialogViewCourse;
    private Button btnUnsubscribe, btnCancel;
    //List to store all Courses
    ArrayList<CourseV> coursesVs;

    //Constructor of this class
    public CourseCardAdapter(ArrayList<CourseV> requestVs, Context context){
        super();
        //Getting all requests
        this.coursesVs = requestVs;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        try {
            doPostRequest("enrolment.php");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Getting the particular item from the list
        final CourseV courseV = coursesVs.get(position);

        //Showing data on the views

        holder.courseName.setText(courseV.getCourseName());
        holder.courseDescription.setText(courseV.getCourseDescription());
        holder.courseInstructor.setText(courseV.getCourseInstructor());
        holder.courseCode.setText(courseV.getCourseCode());
        holder.courseRatingBar.setRating(Float.parseFloat(courseV.getCourseRating()));
        holder.courseOutline = courseV.getCourseOutline();
        holder.courseRating= courseV.getCourseRating();
        holder.courseImage =courseV.getImageUrl();
        if(!courseV.getImageUrl().equals("null")){
            Glide.with(context).load(courseV.getImageUrl()).into(image);
        }

    }

    @Override
    public int getItemCount() {
        return coursesVs.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        //Views
       public TextView courseName;
        public TextView courseDescription;
        public TextView courseInstructor;
        public TextView courseCode;
        public String courseOutline;
        public String courseRating;
        public String courseImage;
        public RatingBar courseRatingBar;

        //Initializing Views
        public ViewHolder(View itemView) {
            super(itemView);
            courseName = (TextView) itemView.findViewById(R.id.courseName);
            courseDescription = (TextView) itemView.findViewById(R.id.courseDescription);
            courseInstructor = (TextView) itemView.findViewById(R.id.courseInstructor);
            courseCode = (TextView) itemView.findViewById(R.id.codeContainer);
            courseRatingBar = (RatingBar)itemView.findViewById(R.id.courseRating);
            image = (ImageView)itemView.findViewById(R.id.courseImage) ;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    COURSE.NAME = courseName.getText().toString();
                    COURSE.OUTLINE = courseOutline;
                    COURSE.IMAGE = courseImage;
                    COURSE.RATING =courseRating;
                    COURSE.INSTRUCTOR = courseInstructor.getText().toString();
                    COURSE.CODE = courseCode.getText().toString();
                    COURSE.DESCRIPTION = courseDescription.getText().toString();

                    try {
                        doPostRequest("enrolment.php");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(USER.STUDENT){
                        createNewViewDialog();
                    }
                    else{
                        Intent i = new Intent(context, CourseHomePageInstructor.class);
                        context.startActivity(i);
                    }
                }
            });

        }
    }
    public void createNewViewDialog(){
        dialogBuilder = new AlertDialog.Builder(context);
        final View viewPopUp = LayoutInflater.from(context)
                .inflate(R.layout.subscribe_dialog, null);

        btnViewDialogSubscribe = (Button) viewPopUp.findViewById(R.id.viewSubscribe);
        btnViewDialogViewCourse = (Button) viewPopUp.findViewById(R.id.viewViewCourse);

        dialogBuilder.setView(viewPopUp);
        dialog = dialogBuilder.create();
        dialog.show();


        btnViewDialogSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnViewDialogSubscribe.getText().toString().trim().equals("SUBSCRIBE")){
                    try {
                        doPostRequest("enrol.php");
                        btnViewDialogSubscribe.setText("UNSUBSCRIBE");
                    Toast toast = Toast.makeText(context, "Subscribed to "+ COURSE.CODE, Toast.LENGTH_LONG);
                        toast.show();
                        dialog.dismiss();
                        Intent intent = new Intent(context,BrowseCourses.class);
                        context.startActivity(intent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    dialog.dismiss();
                    createNewViewDialogUnsubscribe();
                }
            }
        });
       btnViewDialogViewCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent i = new Intent(context, CourseHomePage.class);
                context.startActivity(i);
            }
        });

    }
    public void createNewViewDialogUnsubscribe(){
        dialogBuilder = new AlertDialog.Builder(context);
        final View viewPopUp = LayoutInflater.from(context)
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
                    btnViewDialogSubscribe.setText("SUBSCRIBE");
                    Toast toast = Toast.makeText(context, "Unsubscribed to "+ COURSE.CODE, Toast.LENGTH_LONG);
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
            private Activity cont = (Activity)context;

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                cont.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (btnViewDialogSubscribe != null) {
                            if (responseData.trim().equals("subscribed")) {
                                btnViewDialogSubscribe.setText("UNSUBSCRIBE");
                            }
                        }
                        if (btnUnsubscribe != null) {
                            if (responseData.trim().equals("unsubscribed")) {
                                btnUnsubscribe.setText("SUBSCRIBE");
                            }
                        }
                    }
                });
            }
        });
    }
}
