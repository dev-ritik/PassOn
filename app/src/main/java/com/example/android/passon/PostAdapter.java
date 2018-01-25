package com.example.android.passon;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ritik on 25-01-2018.
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private ArrayList<Post> Posts;

    public class ViewHolder extends RecyclerView.ViewHolder {
         ImageView bookPic;
         TextView bookName,filter1,filter2;
         CheckBox favouritePost;



        private ViewHolder(View view) {
            super(view);
            bookPic=(ImageView)view.findViewById(R.id.bookPic);
            bookName=(TextView)view.findViewById(R.id.bookName);
            filter1=(TextView)view.findViewById(R.id.filter1);
            filter2=(TextView)view.findViewById(R.id.filter2);
            favouritePost = (CheckBox)view.findViewById(R.id.favorite);

        }
    }
    public PostAdapter(ArrayList<Post> posts) {
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
        Log.i("point Po53","adapter");
        holder.filter1.setText(post.getFilter1());
        holder.filter2.setText(post.getFilter2());
        holder.bookName.setText(post.getBookName());
        holder.bookPic.setImageResource(R.drawable.pic);
        holder.favouritePost.setChecked(true);

    }
    @Override
    public int getItemCount() {
        return Posts.size();
    }
}
