package com.example.android.passon;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.view.Gravity.LEFT;
import static android.view.Gravity.RIGHT;

/**
 * Created by ritik on 26-01-2018.
 */

/*
adapter to display chat messages
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private ArrayList<String> notices;
    TextView noticeText;

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ViewHolder(View view) {
            super(view);
            noticeText=(TextView)view.findViewById(R.id.notice);
        }
    }
    public NotificationAdapter(ArrayList<String> notices) {
        this.notices = notices;
    }
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notifications, parent, false);

        return new NotificationAdapter.ViewHolder(v);
    }
    // create a new view
    @Override
    public void onBindViewHolder(final NotificationAdapter.ViewHolder holder, int position) {
        final String notice = notices.get(position);
        Log.i("point nap53",Integer.toString(notices.size()));
        noticeText.setText(notice);

    }
    @Override
    public int getItemCount() {
        return notices.size();
    }

}
