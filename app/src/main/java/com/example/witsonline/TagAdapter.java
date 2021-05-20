package com.example.witsonline;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> {
    private Context context;

    //List to store all Courses
    ArrayList<String>  tags;

    //Constructor of this class
    public TagAdapter(ArrayList<String> tags, Context context ){
        super();
        //Getting all requests
        this.tags = tags;
        this.context = context;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tag, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Getting the particular item from the list
        final String tag =  tags.get(position);

        //Showing data on the view
        holder.tag.setText(tag);

    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        //Views
        public TextView tag;


        //Initializing Views
        @RequiresApi(api = Build.VERSION_CODES.M)
        public ViewHolder(View itemView) {
            super(itemView);
            tag = (TextView) itemView.findViewById(R.id.tag);

        }
    }
}
