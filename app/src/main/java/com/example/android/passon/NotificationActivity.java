package com.example.android.passon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.ArrayList;

import static com.example.android.passon.Main2Activity.mUserDatabaseReference;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    public static RecyclerView.Adapter mAdapter;
//    public ChildEventListener mChildEventListenerProfile, mChildEventListenerProfileTest;
    ArrayList<ChatHead> chats;
    ArrayList<String> chatsString;
    private LinearLayout requestDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        requestDialog = (LinearLayout) findViewById(R.id.requestDialog);

        mRecyclerView = (RecyclerView) findViewById(R.id.chatHead);
        chats = new ArrayList<>();
        chatsString = new ArrayList<String>();
        mAdapter = new ChatNameAdapter(chats, NotificationActivity.this, findViewById(R.id.requestDialog));

//        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        mRecyclerView.setAdapter(mAdapter);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(NotificationActivity.this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(horizontalLayoutManagaer);

        Query query = mUserDatabaseReference.orderByChild("userId").equalTo(Main2Activity.mUserId);

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.i("child", "point pa189");
                DatabaseReference abc = dataSnapshot.child("connectionRequestUsers").getRef();
                abc.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

//                            ChatHead c1=dataSnapshot.getValue(ChatHead.class);
                        Log.i("point pa199", dataSnapshot.toString());
                        ChatHead asd1 = new ChatHead(dataSnapshot.getKey(), dataSnapshot.getValue().toString());
                        chats.add(asd1);
                        mAdapter.notifyDataSetChanged();
                        Log.i("point pa220", dataSnapshot.toString());
                        Log.i("point pa221", asd1.getUsername());
                        Log.i("point pa221a", asd1.getUserId());
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
                Log.i("point pa225", "yess");

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
//            mUserDatabaseReference.orderByChild("userId").equalTo(Main2Activity.mUserId).removeEventListener(mChildEventListenerProfileTest);
//            mChildEventListenerProfileTest = null;
//        }
        chats.clear();
        mAdapter.notifyItemRangeRemoved(0, mAdapter.getItemCount());
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
