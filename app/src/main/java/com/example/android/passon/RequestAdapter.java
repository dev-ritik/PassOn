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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.android.passon.Main2Activity.mUserDatabaseReference;

/**
 * Created by ritik on 25-01-2018.
 */

/*
adapter to display request objects on mainscreen via data from request fragment
 */

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {
    private ArrayList<Post> Posts;
    private boolean tapCount=false;

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bookPic;
        TextView bookName, filter1, filter2, time,posterName,phoneNo,institute;
        CheckBox favouritePost;
        Button request;
        CardView cardView;
        LinearLayout detail;

        private ViewHolder(View view) {
            super(view);
//            bookPic = (ImageView) view.findViewById(R.id.bookPic);
            bookName = (TextView) view.findViewById(R.id.bookName);
            filter1 = (TextView) view.findViewById(R.id.filter1);
            filter2 = (TextView) view.findViewById(R.id.filter2);
            phoneNo=(TextView)view.findViewById(R.id.edit11);
            institute=(TextView)view.findViewById(R.id.edit12);
            request = (Button) view.findViewById(R.id.request);
            cardView = (CardView) view.findViewById(R.id.card_view);
            time = (TextView) view.findViewById(R.id.time);
            posterName=(TextView)view.findViewById(R.id.PosterName);
            detail=(LinearLayout)view.findViewById(R.id.profile_info1);

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
//        Log.i("point Ret53",Integer.toString(Posts.size()));
        holder.posterName.setText(post.getPosterName());
        holder.filter1.setText(post.getFilter1());
        holder.filter2.setText(post.getFilter2());
        holder.bookName.setText(post.getBookName());
//        holder.bookPic.setVisibility(View.GONE);
        holder.phoneNo.setText(post.getPhonenumber());
        holder.institute.setText(post.getInstitute());
        holder.time.setText(post.getTime());
        if (holder.request == null) {
            Log.i("ReqAdapter rat90", "Line 64");
        }
        holder.request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(view.getContext(), "request sent", Toast.LENGTH_SHORT).show();
                if (!tapCount) {
//                    Toast.makeText(view.getContext(), "Sending request", Toast.LENGTH_SHORT).show();
//                    changeData(post.getPosterId(), Main2Activity.mUserId);
                    holder.request.setText("close");
                    holder.detail.setVisibility(View.VISIBLE);
                    tapCount = true;
                } else {
//                    Toast.makeText(view.getContext(), "Request cancelled", Toast.LENGTH_SHORT).show();
                    tapCount = false;
                    holder.request.setText("open");
                    holder.detail.setVisibility(View.GONE);

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return Posts.size();
    }

    public void changeData(String posteruid, final String requesterUid) {

        Log.i(posteruid, "point rat121");
        Log.i(requesterUid, "point rat122");
        Query query = mUserDatabaseReference.orderByChild("userId").equalTo(posteruid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    child.getRef().child("connectionRequestUsers").push().setValue(requesterUid);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
