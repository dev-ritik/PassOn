package com.example.android.passon;

import android.support.constraint.solver.widgets.Snapshot;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;

import static com.example.android.passon.Main2Activity.mUserDatabaseReference;

/*
This class handles backend of all user to user interaction with notice menu item
 */
public class NotificationActivity extends AppCompatActivity {

    private RecyclerView mRecyclerViewRequest, mRecyclerViewConnected, mRecyclerViewNotice;
    public static RecyclerView.Adapter mAdapterRequest, mAdapterConnected, mAdapterNotice;
    public static ArrayList<ChatHead> requests;
    public static ArrayList<ChatHead> connections;
    public static ArrayList<String> timeRequests;
    public static ArrayList<String> timeConnections;
    private ChildEventListener mRequestEventListener, mConnectionEventListener, mNoticesEventListener;
    private DatabaseReference requestedUsersReference, connectionChildReference, noticeChild;

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
        timeConnections = new ArrayList<>();
        timeRequests = new ArrayList<>();
        notices = new ArrayList<>();

        mAdapterRequest = new ChatNameAdapter(requests, timeRequests, NotificationActivity.this, requestDialog, notificationActivityScreen, backgroundButtonNotification);

        mAdapterConnected = new ChatNameAdapter(connections, timeConnections, NotificationActivity.this, connectionDialog, notificationActivityScreen, backgroundButtonNotification);

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
                requestedUsersReference = dataSnapshot.child("connectionRequestUsers").getRef();
                mRequestEventListener = new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Log.i("point na141", requests.size() + "");
                        Map<String, Object> asdf = new HashMap<>();

                        asdf.put(dataSnapshot.getKey(), dataSnapshot.getValue());
                        ChatHead c1 = dataSnapshot.getValue(ChatHead.class);

                        requests.add(c1);
                        timeRequests.add(dataSnapshot.getKey());
                        mAdapterRequest.notifyDataSetChanged();
                        Log.i("point na154", dataSnapshot.getKey());
                        Log.i("point na155", requests.size() + "");

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                        ChatHead c1 = dataSnapshot.getValue(ChatHead.class);
//
//                        requests.add(c1);
//                        timeRequests.add(dataSnapshot.getKey());
////                        ChatHead asd1 = new ChatHead(dataSnapshot.getKey(), dataSnapshot.getValue().toString());
////                        requests.add(asd1);
//                        mAdapterRequest.notifyDataSetChanged();
//
//
//                        Log.i("child changed" + post.getBookName(), "point na160");
//
//                        ListIterator<Post> iterator = posts.listIterator();
//                        while (iterator.hasNext()) {
//                            if (iterator.next().getTime().equals(post.getTime()))
//                                iterator.set(post);
//                        }
//                        PostFragment.mAdapterPost.notifyDataSetChanged();

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        ChatHead c1 = dataSnapshot.getValue(ChatHead.class);
//                        Log.i(requests.size()+"", "point na181");
//                        Log.i(requests.get(0).getUsername(), "point na182");
//                        Log.i(requests.get(0).getUserId(), "point na183");
//                        Log.i(c1.getUsername(), "point na1831");
//                        Log.i(c1.getUserId(), "point na1832");

//                        requests.remove(dataSnapshot.getValue(ChatHead.class));
                        timeRequests.remove(dataSnapshot.getKey());
//                        Log.i(timeRequests.size()+"", "point na185");
//                        Log.i(requests.size()+"", "point na186");

                        for (Iterator<ChatHead> iterator = requests.iterator(); iterator.hasNext(); ) {
                            if (iterator.next().getUserId() == c1.getUserId())
                                iterator.remove();
                        }
                        Log.i(requests.size() + "", "point na198");
                        mAdapterRequest.notifyDataSetChanged();

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                requestedUsersReference.addChildEventListener(mRequestEventListener);
                Log.i("point na100", "yess");

                connectionChildReference = dataSnapshot.child("connectedUsers").getRef();
                mConnectionEventListener = new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        Map<String, Object> asdf = new HashMap<>();

                        asdf.put(dataSnapshot.getKey(), dataSnapshot.getValue());
                        ChatHead c1 = dataSnapshot.getValue(ChatHead.class);

                        connections.add(c1);
                        timeConnections.add(dataSnapshot.getKey());
//                        ChatHead asd1 = new ChatHead(dataSnapshot.getKey(), dataSnapshot.getValue().toString());
//                        requests.add(asd1);

                        mAdapterConnected.notifyDataSetChanged();
                        Log.i("point na230", dataSnapshot.getKey());
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        ChatHead c1 = dataSnapshot.getValue(ChatHead.class);
//                        Log.i(requests.size()+"", "point na181");
//                        Log.i(requests.get(0).getUsername(), "point na182");
//                        Log.i(requests.get(0).getUserId(), "point na183");
//                        Log.i(c1.getUsername(), "point na1831");
//                        Log.i(c1.getUserId(), "point na1832");

//                        requests.remove(dataSnapshot.getValue(ChatHead.class));
                        timeConnections.remove(dataSnapshot.getKey());
//                        Log.i(timeRequests.size()+"", "point na185");
//                        Log.i(requests.size()+"", "point na186");

                        for (Iterator<ChatHead> iterator = connections.iterator(); iterator.hasNext(); ) {
                            if (iterator.next().getUserId().equals(c1.getUserId()))
                                iterator.remove();
                        }
                        Log.i(connections.size() + "", "point na255");
                        mAdapterRequest.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                connectionChildReference.addChildEventListener(mConnectionEventListener);

                Log.i("point na171", "yess");

                noticeChild = dataSnapshot.child("notifications").getRef();

                mNoticesEventListener = new ChildEventListener() {
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
                };
                noticeChild.addChildEventListener(mNoticesEventListener);
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
        if (mConnectionEventListener != null) {
            Log.i(mConnectionEventListener.toString(), "point NA343");
            connectionChildReference.removeEventListener(mConnectionEventListener);
            mConnectionEventListener = null;
        }
        if (mRequestEventListener != null) {
            Log.i(mRequestEventListener.toString(), "point Na348");
            requestedUsersReference.removeEventListener(mRequestEventListener);
            mRequestEventListener = null;
        }
        if (mNoticesEventListener != null) {
            Log.i(mNoticesEventListener.toString(), "point Na348");
            noticeChild.removeEventListener(mNoticesEventListener);
            mNoticesEventListener = null;
        }
        requests.clear();
        mAdapterRequest.notifyItemRangeRemoved(0, mAdapterRequest.getItemCount());
        connections.clear();
        mAdapterConnected.notifyItemRangeRemoved(0, mAdapterConnected.getItemCount());
        notices.clear();
        mAdapterConnected.notifyItemRangeRemoved(0, mAdapterNotice.getItemCount());
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
