package com.example.android.passon;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by ritik on 25-01-2018.
 */

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {
    private ArrayList<Post> Posts;

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bookPic;
        TextView bookName,filter1,filter2;
        CheckBox favouritePost;
        Button request;
        CardView cardView;

        private ViewHolder(View view) {
            super(view);
            bookPic=(ImageView)view.findViewById(R.id.bookPic);
            bookName=(TextView)view.findViewById(R.id.bookName);
            filter1=(TextView)view.findViewById(R.id.filter1);
            filter2=(TextView)view.findViewById(R.id.filter2);
            favouritePost = (CheckBox)view.findViewById(R.id.favorite);
            request=(Button)view.findViewById(R.id.request);
            cardView=(CardView)view.findViewById(R.id.card_view);

        }
    }
    public RequestAdapter(ArrayList<Post> posts) {
        Posts = posts;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_feed, parent, false);

        return new ViewHolder(v);
    }
    // create a new view
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Post post = Posts.get(position);
//        Log.i("point Re53",Integer.toString(Posts.size()));
        holder.filter1.setText(post.getFilter1());
        holder.filter2.setText(post.getFilter2());
        holder.bookName.setText(post.getBookName());
        holder.bookPic.setVisibility(View.GONE);
        holder.favouritePost.setChecked(true);
        if(holder.request==null) {
            Log.i("ReqAdapter", "Line 64");
        }
        holder.request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Sending request", Toast.LENGTH_SHORT).show();
            }
        });

    }
    @Override
    public int getItemCount() {
        return Posts.size();
    }
}
