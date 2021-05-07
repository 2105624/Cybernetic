package com.example.witsonline;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReviewCardAdapter extends RecyclerView.Adapter<ReviewCardAdapter.ViewHolder> {
    private Context context;

    //List to store all Courses
    ArrayList<ReviewV>  reviewVs;

    //Constructor of this class
    public ReviewCardAdapter(ArrayList<ReviewV> requestVs, Context context ){
        super();
        //Getting all requests
        this.reviewVs = requestVs;
        this.context = context;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Getting the particular item from the list
        final ReviewV reviewV =  reviewVs.get(position);

        //Showing data on the view
        holder.studentName.setText(reviewV.getStudentFName() +" "+ reviewV.getStudentLName());
        holder.reviewDescription.setText(reviewV.getReviewDescription());
        if(reviewV.getReviewRating()!=null) {
            holder.reviewRating.setRating(Float.parseFloat(reviewV.getReviewRating()));
        }

    }

    @Override
    public int getItemCount() {
        return reviewVs.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        //Views
        public TextView studentName;
        public TextView reviewDescription;
        public RatingBar reviewRating;

        //Initializing Views
        @RequiresApi(api = Build.VERSION_CODES.M)
        public ViewHolder(View itemView) {
            super(itemView);
            studentName = (TextView) itemView.findViewById(R.id.studentName);
            reviewDescription = (TextView) itemView.findViewById(R.id.reviewDescription);
            reviewRating = (RatingBar) itemView.findViewById(R.id.reviewRating);

        }
    }
}
