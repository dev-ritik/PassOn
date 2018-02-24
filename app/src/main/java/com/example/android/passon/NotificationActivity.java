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

/*
This class handles backend of all user to user interaction
 */
public class NotificationActivity extends AppCompatActivity {

    private RecyclerView mRecyclerViewRequest, mRecyclerViewConnected;
    public static RecyclerView.Adapter mAdapterRequest, mAdapterConnected;
    //    public ChildEventListener mChildEventListenerProfile, mChildEventListenerProfileTest;
    ArrayList<ChatHead> requests;
    ArrayList<ChatHead> connections;
    private LinearLayout requestDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        requestDialog = (LinearLayout) findViewById(R.id.requestDialog);

        mRecyclerViewRequest = (RecyclerView) findViewById(R.id.requestedUsers);
        mRecyclerViewConnected = (RecyclerView) findViewById(R.id.connectedUsers);
        requests = new ArrayList<>();
        connections = new ArrayList<>();
        mAdapterRequest = new ChatNameAdapter(requests, NotificationActivity.this, findViewById(R.id.requestDialog));
        mAdapterConnected = new ChatNameAdapter(connections, NotificationActivity.this, findViewById(R.id.requestDialog));

//        mRecyclerViewRequest.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        mRecyclerViewRequest.setAdapter(mAdapterRequest);
        LinearLayoutManager horizontalLayoutManagaerRequest = new LinearLayoutManager(NotificationActivity.this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewRequest.setLayoutManager(horizontalLayoutManagaerRequest);

        mRecyclerViewConnected.setAdapter(mAdapterRequest);
        LinearLayoutManager horizontalLayoutManagaerConnected = new LinearLayoutManager(NotificationActivity.this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewConnected.setLayoutManager(horizontalLayoutManagaerConnected);

        Query query = mUserDatabaseReference.orderByChild("userId").equalTo(Main2Activity.mUserId);

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.i("child", "point pa189");
                DatabaseReference requestChild = dataSnapshot.child("connectionRequestUsers").getRef();
                requestChild.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

//                            ChatHead c1=dataSnapshot.getValue(ChatHead.class);
                        Log.i("point pa199", dataSnapshot.toString());
                        ChatHead asd1 = new ChatHead(dataSnapshot.getKey(), dataSnapshot.getValue().toString());
                        requests.add(asd1);
                        mAdapterRequest.notifyDataSetChanged();
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
                Log.i("point na100", "yess");

                DatabaseReference connectionChild = dataSnapshot.child("connectedUsers").getRef();
                connectionChild.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        Log.i("point pa199", dataSnapshot.toString());
                        ChatHead asd1 = new ChatHead(dataSnapshot.getKey(), dataSnapshot.getValue().toString());
                        connections.add(asd1);
                        mAdapterRequest.notifyDataSetChanged();
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
                Log.i("point na136", "yess");

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
        requests.clear();
        mAdapterRequest.notifyItemRangeRemoved(0, mAdapterRequest.getItemCount());
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
