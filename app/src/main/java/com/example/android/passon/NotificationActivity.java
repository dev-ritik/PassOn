package com.example.android.passon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
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

    private RecyclerView mRecyclerViewRequest, mRecyclerViewConnected, mRecyclerViewNotice;
    public static RecyclerView.Adapter mAdapterRequest, mAdapterConnected, mAdapterNotice;
    ArrayList<ChatHead> requests;
    ArrayList<ChatHead> connections;
    ArrayList<String> notices;
    public static LinearLayout requestDialog, connectionDialog;
    private FrameLayout notificationActivityScreen;
    private ImageView backgroundButtonNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        requestDialog = (LinearLayout) findViewById(R.id.requestDialog);
        connectionDialog = (LinearLayout) findViewById(R.id.connectionDialog);

        notificationActivityScreen = (FrameLayout) findViewById(R.id.notificationActivityScreen);
        notificationActivityScreen.getForeground().setAlpha(0);

        mRecyclerViewRequest = (RecyclerView) findViewById(R.id.requestedUsers);
        mRecyclerViewConnected = (RecyclerView) findViewById(R.id.connectedUsers);
        mRecyclerViewNotice = (RecyclerView) findViewById(R.id.noticeRecyclerView);

        backgroundButtonNotification = (ImageView) findViewById(R.id.backgroundButtonNotification);

        mRecyclerViewRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationActivityScreen.getForeground().setAlpha(120);
            }
        });
        mRecyclerViewConnected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("point na62", "recycler view");
                notificationActivityScreen.getForeground().setAlpha(120);
            }
        });
        backgroundButtonNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("point na71", "bb clicked");
                requestDialog.setVisibility(View.INVISIBLE);
                connectionDialog.setVisibility(View.INVISIBLE);
                notificationActivityScreen.getForeground().setAlpha(0);
            }
        });
        requestDialog.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                Log.i("point na82", "request clicked");
                // must for making clicks not to go to background switch

            }
        });
        connectionDialog.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                Log.i("point na92", "connection clicked");
                // must for making clicks not to go to background switch

            }
        });
        requests = new ArrayList<>();
        connections = new ArrayList<>();
        notices = new ArrayList<>();

        mAdapterRequest = new ChatNameAdapter(requests, NotificationActivity.this, requestDialog, notificationActivityScreen, backgroundButtonNotification);

        mAdapterConnected = new ChatNameAdapter(connections, NotificationActivity.this, connectionDialog, notificationActivityScreen, backgroundButtonNotification);

//        mRecyclerViewRequest.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        mRecyclerViewRequest.setAdapter(mAdapterRequest);
        LinearLayoutManager horizontalLayoutManagaerRequest = new LinearLayoutManager(NotificationActivity.this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewRequest.setLayoutManager(horizontalLayoutManagaerRequest);

        mRecyclerViewConnected.setAdapter(mAdapterConnected);
        LinearLayoutManager horizontalLayoutManagaerConnected = new LinearLayoutManager(NotificationActivity.this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewConnected.setLayoutManager(horizontalLayoutManagaerConnected);

        mAdapterNotice = new NotificationAdapter(notices);
        mRecyclerViewNotice.setAdapter(mAdapterNotice);
//        mRecyclerViewNotice.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        RecyclerView.LayoutManager mLayoutManagerNotice;
        mLayoutManagerNotice = new LinearLayoutManager(this);
        mRecyclerViewNotice.setLayoutManager(mLayoutManagerNotice);

        Query query = mUserDatabaseReference.orderByChild("userId").equalTo(Main2Activity.mUserId);

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.i("child", "point na64");
                DatabaseReference requestChild = dataSnapshot.child("connectionRequestUsers").getRef();
                requestChild.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

//                            ChatHead c1=dataSnapshot.getValue(ChatHead.class);
                        ChatHead asd1 = new ChatHead(dataSnapshot.getKey(), dataSnapshot.getValue().toString());
                        requests.add(asd1);
                        mAdapterRequest.notifyDataSetChanged();
//                        Log.i("point na75", dataSnapshot.toString());
                        Log.i("point na76", asd1.getUsername());
//                        Log.i("point na77", asd1.getUserId());
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

                        Log.i("point na107", dataSnapshot.toString());
                        ChatHead asd1 = new ChatHead(dataSnapshot.getKey(), dataSnapshot.getValue().toString());
                        connections.add(asd1);
                        mAdapterConnected.notifyDataSetChanged();
                        Log.i("point na112", asd1.getUsername());
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
                Log.i("point na171", "yess");

                DatabaseReference noticeChild = dataSnapshot.child("notifications").getRef();
                noticeChild.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        Log.i("point na178", dataSnapshot.toString());
                        String asd1 = new String(dataSnapshot.getValue().toString());
                        notices.add(asd1);
                        mAdapterConnected.notifyDataSetChanged();
//                        Log.i("point na182", dataSnapshot.toString());
//                        Log.i("point na183", dataSnapshot.getChildren().toString());
//                        Log.i("point na184", dataSnapshot.getValue().toString());
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
        System.out.println("point na166");
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
