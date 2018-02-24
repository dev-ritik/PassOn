package com.example.android.passon;

import android.content.Intent;
import android.graphics.Movie;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

//import com.bumptech.glide.Glide;

//import com.makeramen.roundedimageview.RoundedTransformationBuilder;
//import com.squareup.picasso.Picasso;
//import com.squareup.picasso.Transformation;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.example.android.passon.Main2Activity.mUserDatabaseReference;
import static com.example.android.passon.Main2Activity.mUserId;

//import static com.example.android.passon.MainActivity.mUserId;

public class ProfileActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    public static RecyclerView.Adapter mAdapter;
    public static ChildEventListener mChildEventListenerProfile, mChildEventListenerProfileTest;
    public static DatabaseReference mChildUser;
    ArrayList<ChatHead> chats;
    ArrayList<String> chatsString;
    private LinearLayout requestDialog, contentProfile;
    private TextView userName;
    private ImageView cancelButton, acceptButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("Profile");

//        mChildUser= Main2Activity.mUserDatabaseReference.orderByChild("userId").equalTo(Main2Activity.mUserId).


//        requestDialog = (LinearLayout) findViewById(R.id.requestDialog);
        contentProfile = (LinearLayout) findViewById(R.id.contentProfile);

//        mRecyclerView = (RecyclerView) findViewById(R.id.chatHead);
//        chats = new ArrayList<>();
//        chatsString = new ArrayList<String>();
//        mAdapter = new ChatNameAdapter(chats, ProfileActivity.this, findViewById(R.id.requestDialog));

//        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
//        mRecyclerView.setAdapter(mAdapter);
//        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(ProfileActivity.this, LinearLayoutManager.HORIZONTAL, false);
//        mRecyclerView.setLayoutManager(horizontalLayoutManagaer);
        TextView userName = (TextView) findViewById(R.id.userName);
        ImageView profilePic = (ImageView) findViewById(R.id.profile_image);
        if (Main2Activity.mUser != null) {
            userName.setText(Main2Activity.mUser);
        } else {
            userName.setVisibility(View.GONE);
        }

        try {
            if (Main2Activity.mUserProfile != null) {
                Log.i(Main2Activity.mUserProfile.toString(), "standpoint pr60");
//            Glide.with(profilePic.getContext())
//                    .load(MainActivity.mUserProfile)
//                    .into(profilePic);

//                profilePic.setImageURI(MainActivity.mUserProfile);
                com.squareup.picasso.Transformation transformation = new RoundedTransformationBuilder()
                        .cornerRadiusDp(30)
                        .oval(false)
                        .build();
                Picasso.with(ProfileActivity.this)
                        .load(Main2Activity.mUserProfile.toString())
                        .transform(transformation)
                        .into(profilePic);
            } else {
//                Log.i("profile pic=null", "standpoint pr75");

                profilePic.setImageResource(R.mipmap.icon_profile_empty);
            }
        } catch (Exception e) {
            profilePic.setImageResource(R.mipmap.icon_profile_empty);
        }

        TextView emailId = (TextView) findViewById(R.id.email);
        emailId.setText(email);


//        Query query = mUserDatabaseReference.orderByChild("userId").equalTo(Main2Activity.mUserId);
//
//        query.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Log.i("child", "point pa189");
//                DatabaseReference abc = dataSnapshot.child("connectionRequestUsers").getRef();
//                abc.addChildEventListener(new ChildEventListener() {
//                    @Override
//                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//
////                            ChatHead c1=dataSnapshot.getValue(ChatHead.class);
//                        Log.i("point pa199", dataSnapshot.toString());
//                        ChatHead asd1 = new ChatHead(dataSnapshot.getKey(), dataSnapshot.getValue().toString());
//                        chats.add(asd1);
//                        mAdapter.notifyDataSetChanged();
//                        Log.i("point pa220", dataSnapshot.toString());
//                        Log.i("point pa221", asd1.getUsername());
//                        Log.i("point pa221a", asd1.getUserId());
//                    }
//
//                    @Override
//                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//                    }
//
//                    @Override
//                    public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//                    }
//
//                    @Override
//                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//                Log.i("point pa225", "yess");
//
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("standpoint pr112");
//        if (mChildEventListenerProfile != null) {
//            Log.i(mChildEventListenerProfile.toString(), "standpoint pr114");
//            mUserDatabaseReference.orderByChild("userId").equalTo(Main2Activity.mUserId).getRef().child("connectionRequestUsers").removeEventListener(mChildEventListenerProfile);
//            mChildEventListenerProfile = null;
//        }
//        if (mChildEventListenerProfileTest != null) {
//            Log.i(mChildEventListenerProfileTest.toString(), "standpoint pr114");
////                        Main2Activity.mUserDatabaseReference.orderByChild("userId").equalTo(Main2Activity.mUserId).getRef().child("connectionRequestUsers").removeEventListener(mChildEventListenerProfile);
//            mUserDatabaseReference.orderByChild("userId").equalTo(Main2Activity.mUserId).removeEventListener(mChildEventListenerProfileTest);
//            mChildEventListenerProfileTest = null;
//        }
//        chats.clear();
//        mAdapter.notifyItemRangeRemoved(0, mAdapter.getItemCount());
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}