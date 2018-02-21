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

import static com.example.android.passon.Main2Activity.mUserDatabaseReference;
import static com.example.android.passon.Main2Activity.mUserId;

//import static com.example.android.passon.MainActivity.mUserId;

public class ProfileActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    public static RecyclerView.Adapter mAdapter;
    public static ChildEventListener mChildEventListenerProfile, mChildEventListenerProfileTest;
    public static DatabaseReference mChildUser;
    ArrayList<ArrayList<ChatHead>> chats;
    ArrayList<String> chatsString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("Profile");

//        mChildUser= Main2Activity.mUserDatabaseReference.orderByChild("userId").equalTo(Main2Activity.mUserId).

        mRecyclerView = (RecyclerView) findViewById(R.id.chatHead);
        chats = new ArrayList<>();
        chatsString = new ArrayList<String>();
        mAdapter = new ChatNameAdapter(chats);
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        mRecyclerView.setAdapter(mAdapter);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(ProfileActivity.this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(horizontalLayoutManagaer);
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

        if (mChildEventListenerProfile == null) {
            Log.i("mChildEvenProfe", "standpoint pr96");
            mChildEventListenerProfile = new ChildEventListener() {//working with db after authentication
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Log.i("onchildadded", "standpoint pr100");

                    //attached to all added child(all past and future child)
//                    ChatHead chatHead = dataSnapshot.getValue(ChatHead.class);
//                    chats.add(chatHead);
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        Log.i(child.getValue().toString(), "standpoint pr106");
                    }
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    // changed content of a child
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    // child deleted
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    //moved position of a child
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // error or permission denied
                }

            };
//            mUserDatabaseReference.orderByChild("userId").equalTo(Main2Activity.mUserId).orderByChild("phoneNo").addChildEventListener(mChildEventListenerProfile);
//                    .addChildEventListener(mChildEventListenerProfile);
//            Main2Activity.mUserDatabaseReference.orderByChild("userId").equalTo(Main2Activity.mUserId).getRef().child("phoneNo").addChildEventListener(mChildEventListenerProfile);
//            Main2Activity.mUserDatabaseReference.orderByChild("userId").equalTo(Main2Activity.mUserId).getRef().child("connectionRequestUsers").addChildEventListener(mChildEventListenerProfile);
//            Main2Activity.mUserDatabaseReference.orderByChild("userId").equalTo(Main2Activity.mUserId).getRef().child("connectionRequestUsers").addChildEventListener(mChildEventListenerProfile);
//            mPostDatabaseReference = mfirebaseDatabase.getReference().child("post1");
            Log.i("dataSnapshot.toString()", "point 145");

            Query query = mUserDatabaseReference.orderByChild("userId").equalTo(Main2Activity.mUserId);
            query.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Log.i("toString()", "point 151");
                    UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
                    chats.add(new ArrayList<ChatHead>(userInfo.getConnectionRequestUsers()));
                    ArrayList<ChatHead> ram1=(ArrayList<ChatHead>)userInfo.getConnectionRequestUsers();

                    Log.i("point 154",userInfo.getPhoneNo()+"");
                    Log.i("point 161",ram1.get(0).getUsername());

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

//                    new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    for (DataSnapshot movieSnapshot : dataSnapshot.getChildren()) {
////                        Movie movie = dataSnapshot.getValue(Movie.class);
//                        Log.i(dataSnapshot.toString(),"point 152");
//                        Log.i(movieSnapshot.toString(),"point 153");
//
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//            query.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    for (DataSnapshot child : dataSnapshot.getChildren()) {
//                        child.getRef().child("connectionRequestUsers").push().setValue(requesterUid);
            if (mChildEventListenerProfile != null) {
                Log.i("mChildEventLisrPro add", "standpoint pr142");
            }

            if (mChildEventListenerProfileTest == null) {
                Log.i("mChildEvenProfe", "standpoint pr146");
                mChildEventListenerProfileTest = new ChildEventListener() {//working with db after authentication
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Log.i("onchildadded", "standpoint pr150");
//                         new UserInfo();
                        Log.i(dataSnapshot.getValue().toString(), "standpoint pr156");
                        Log.i(dataSnapshot.getChildren().toString(), "standpoint pr155");

                        //attached to all added child(all past and future child)
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            Log.i(child.getValue().toString(), "standpoint pr157");
                            //gives all datatypes and singley filled connection request users while giving doubley filled to be null
//                            UserInfo userInfo = child.getValue(UserInfo.class);
//                            UserInfo userInfo = dataSnapshot.getValue(UserInfo.class);
//                        dataSnapshot.getChildren().toString()
//                            Log.i(userInfo.getAddress(), "standpoint pr154");
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            List<Friends> list = new ArrayList<Friends>();
//                            for (DataSnapshot child: dataSnapshot.getChildren()) {
//                                list.add(child.getValue(Friends.class));
//                            }
//                        }                    }
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        // changed content of a child
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        // child deleted
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                        //moved position of a child
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // error or permission denied
                    }

                };
                mUserDatabaseReference.orderByChild("userId").equalTo(Main2Activity.mUserId).getRef().child("connectionRequestUsers").addChildEventListener(mChildEventListenerProfileTest);
                if (mChildEventListenerProfileTest != null) {
                    Log.i("mChildEventLisrPro add", "standpoint pr183");
//                Query query = mUserDatabaseReference.orderByChild("userId").equalTo(posteruid);
//                query.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        for (DataSnapshot child : dataSnapshot.getChildren()) {
//                            child.getRef().child("connectionRequestUsers").push().setValue(requesterUid);
//                        }
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("standpoint pr112");
        if (mChildEventListenerProfile != null) {
            Log.i(mChildEventListenerProfile.toString(), "standpoint pr114");
            mUserDatabaseReference.orderByChild("userId").equalTo(Main2Activity.mUserId).getRef().child("connectionRequestUsers").removeEventListener(mChildEventListenerProfile);
            mChildEventListenerProfile = null;
        }
        if (mChildEventListenerProfileTest != null) {
            Log.i(mChildEventListenerProfileTest.toString(), "standpoint pr114");
//                        Main2Activity.mUserDatabaseReference.orderByChild("userId").equalTo(Main2Activity.mUserId).getRef().child("connectionRequestUsers").removeEventListener(mChildEventListenerProfile);
            mUserDatabaseReference.orderByChild("userId").equalTo(Main2Activity.mUserId).removeEventListener(mChildEventListenerProfileTest);
            mChildEventListenerProfileTest = null;
        }
        chats.clear();
        mAdapter.notifyItemRangeRemoved(0, mAdapter.getItemCount());
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}