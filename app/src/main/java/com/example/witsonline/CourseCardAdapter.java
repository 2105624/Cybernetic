package com.example.witsonline;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CourseCardAdapter extends RecyclerView.Adapter<CourseCardAdapter.ViewHolder> {
    private Context context;

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
    public CourseCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CourseCardAdapter.ViewHolder holder, int position) {
        //Getting the particular item from the list
        final CourseV courseV = coursesVs.get(position);

        //Showing data on the views

        holder.courseName.setText(courseV.getCourseName());
        holder.courseDescription.setText(courseV.getCourseDescription());
        holder.courseInstructor.setText(courseV.getCourseInstructor());

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

        //Initializing Views
        public ViewHolder(View itemView) {
            super(itemView);
            courseName = (TextView) itemView.findViewById(R.id.courseName);
            courseDescription = (TextView) itemView.findViewById(R.id.courseDescription);
            courseInstructor = (TextView) itemView.findViewById(R.id.courseInstructor);

        }
    }
}
