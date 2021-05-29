package com.example.witsonline;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;

public class LessonCardAdapter extends RecyclerView.Adapter<LessonCardAdapter.ViewHolder> {
    private Context context;

    //List to store all Courses
    ArrayList<LessonV> lessonVs;

    //Constructor of this class
    public LessonCardAdapter(ArrayList<LessonV> requestVs, Context context){
        super();
        //Getting all requests
        this.lessonVs = requestVs;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lesson_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Getting the particular item from the list
        final LessonV lessonV = lessonVs.get(position);

        //Showing data on the views
        holder.setIsRecyclable(false);
        holder.lessonName.setText(lessonV.getLessonName());
        holder.lessonText.setText(lessonV.getLessonText());
        holder.lessonResource.setText(lessonV.getLessonResource());
        holder.lessonUrl.setText(lessonV.getLessonUrl());

    }

    @Override
    public int getItemCount() {
        return lessonVs.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        //Views
        public TextView lessonName;
        public TextView lessonText;
        public TextView lessonResource;
        public TextView lessonUrl;


        //Initializing Views
        public ViewHolder(View itemView) {
            super(itemView);
            lessonName = (TextView) itemView.findViewById(R.id.viewLessonName);
            lessonText = (TextView) itemView.findViewById(R.id.viewLessonText);
            lessonResource = (TextView) itemView.findViewById(R.id.viewLessonResource);
            lessonUrl = (TextView) itemView.findViewById(R.id.viewLessonUrl);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LESSON.Code = COURSE.CODE;
                    LESSON.Text = lessonText.getText().toString();
                    LESSON.Name = lessonName.getText().toString();
                    LESSON.Resource = lessonResource.getText().toString();
                    LESSON.Url = lessonUrl.getText().toString();

                    if(USER.STUDENT){
                        Intent i = new Intent(context, LessonPage.class);
                        context.startActivity(i);
                    }
                    else{
                        Intent i = new Intent(context, LessonPageInstructor.class);
                        context.startActivity(i);
                    }
                }
            });

        }
    }
}
