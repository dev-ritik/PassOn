package com.example.android.passon;

import android.support.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.android.passon.Main2Activity.mPostDatabaseReference;
import static com.example.android.passon.Main2Activity.mUserDatabaseReference;

/**
 * Created by ritik on 25-01-2018.
 */

/*
adapter to show posts in the mainscreen via data from PostFragment adapter
 */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private ArrayList<Post> Posts;
    private boolean tapCount = false;

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bookPic;
        TextView bookName, filter1, filter2, time, posterName, phoneNo, institute;
        CheckBox favouritePost;
        Button request;
        LinearLayout detail;

        private ViewHolder(View view) {
            super(view);
//            bookPic = (ImageView) view.findViewById(R.id.bookPic);
            bookName = (TextView) view.findViewById(R.id.bookName);
            phoneNo = (TextView) view.findViewById(R.id.edit11);
            institute = (TextView) view.findViewById(R.id.edit12);
            filter1 = (TextView) view.findViewById(R.id.filter1);
            filter2 = (TextView) view.findViewById(R.id.filter2);
            request = (Button) view.findViewById(R.id.request);
            time = (TextView) view.findViewById(R.id.time);
            posterName = (TextView) view.findViewById(R.id.PosterName);
            detail = (LinearLayout) view.findViewById(R.id.profile_info1);
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Post post = Posts.get(position);
//        Log.i("point Po53",Integer.toString(Posts.size()));
        holder.posterName.setText(post.getPosterName());
        holder.filter1.setText(post.getFilter1());
        holder.filter2.setText(post.getFilter2());
        holder.bookName.setText(post.getBookName());
        holder.time.setText(post.getTime());
        holder.phoneNo.setText(post.getPhonenumber());
        holder.institute.setText(post.getInstitute());
        if(post.getBookRequestUsers().contains(Main2Activity.mUserId)){
            holder.request.setText("close");
            holder.detail.setVisibility(View.VISIBLE);
            tapCount=true;
        }
        holder.request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (!post.getPosterId().equals(Main2Activity.mUserId))
                if (!tapCount) {
//                    Toast.makeText(view.getContext(), "Sending request", Toast.LENGTH_SHORT).show();
                    if (!post.getBookRequestUsers().contains(Main2Activity.mUserId)) {
                        post.getBookRequestUsers().add(Main2Activity.mUserId);
                        setData(post.getPosterId(), post.getTime(), Main2Activity.mUserId, Main2Activity.mUser, position, post.getBookRequestUsers());
                        Toast.makeText(view.getContext(), "Sending Request", Toast.LENGTH_SHORT).show();
                    }
                    holder.request.setText("close");
                    holder.detail.setVisibility(View.VISIBLE);
                    tapCount = true;
                } else {
                    if (post.getBookRequestUsers().contains(Main2Activity.mUserId)) {
                        post.getBookRequestUsers().remove(Main2Activity.mUserId);
                        changeData(post.getPosterId(), post.getTime(), Main2Activity.mUserId, Main2Activity.mUser, post.getBookRequestUsers());
                        Toast.makeText(view.getContext(), "Request Cancelled", Toast.LENGTH_SHORT).show();
                    }
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

    public void setData(String posteruid, final String time, final String uid, final String username, final int position, final ArrayList<String> requestUsers) {

        Log.i(posteruid, "standpoint re91");
        Query query = mUserDatabaseReference.orderByChild("userId").equalTo(posteruid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    final Map<String, Object> users = new HashMap<>();
                    users.put(time, new ChatHead(uid,username));
//                    child.getRef().child("connectionRequestUsers").updateChildren(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                    child.getRef().child("connectionRequestUsers").updateChildren(users)

                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.i("point pat137", "completed changing");
                            if (Main2Activity.userInfo.getConnectionRequestUsers() == null)
                                Main2Activity.userInfo.setConnectionRequestUsers(users);
                            else
                                Main2Activity.userInfo.getConnectionRequestUsers().put(time,new ChatHead(uid,username));
                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Query query1 = mPostDatabaseReference.orderByChild("time").equalTo(time);
        query1.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    child.getRef().child("bookRequestUsers").setValue(requestUsers);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void changeData(String posteruid, final String time, final String uid, final String username, final ArrayList<String> requestUsers) {

        Log.i(posteruid, "standpoint re140");
        Query query = mUserDatabaseReference.orderByChild("userId").equalTo(posteruid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Map<String, Object> users = new HashMap<>();
                    users.put(time,null);
                    child.getRef().child("connectionRequestUsers").updateChildren(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.i("point pat181", "completed changing");
                            Main2Activity.userInfo.getConnectionRequestUsers().put(time,new ChatHead(uid, null));
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Query query1 = mPostDatabaseReference.orderByChild("time").equalTo(time);
        query1.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    child.getRef().child("bookRequestUsers").setValue(requestUsers);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}