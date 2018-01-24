package com.example.android.passon;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by ritik on 25-01-2018.
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private ArrayList<Post> Posts;

    public class ViewHolder extends RecyclerView.ViewHolder {


        private ViewHolder(View view) {
            super(view);
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

        ViewHolder vh = new ViewHolder(v);
        return  vh;
    }
        // create a new view
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

    }
    @Override
    public int getItemCount() {
        return Posts.size();
    }
}
