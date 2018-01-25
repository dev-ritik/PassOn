package com.example.android.passon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    static boolean backupCalledAlready = false;

    private FirebaseDatabase mfirebaseDatabase;
    public static DatabaseReference mMessagesDatabaseReference;
    public static ChildEventListener mChildEventListener;//to listen the changes in db
    private FirebaseStorage mFirebaseStorage;
    public static StorageReference mChatPhotosStorageReference;

    private RecyclerView mRecyclerView;
    public static RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    ArrayList<Post> posts;
    ArrayList<String> favouriteArrayList;

    private ProgressBar mProgressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!backupCalledAlready)
        {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            backupCalledAlready = true;
        }
        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mMessagesDatabaseReference = mfirebaseDatabase.getReference().child("input");
        mChatPhotosStorageReference = mFirebaseStorage.getReference("book_photos");

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mProgressBar.setVisibility(View.INVISIBLE);
        posts = new ArrayList<>();
        favouriteArrayList=new ArrayList<>();



        mAdapter = new PostAdapter(posts);
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        favouriteArrayList.add("qwerty");
        Post post=new Post(1,"a","b","c","d","e","f",favouriteArrayList);
        posts.add(post);
    }



    @Override
    protected void onPause() {
        super.onPause();
        detachDatabaseReadListener();
        posts.clear();
        mAdapter.notifyItemRangeRemoved(0, mAdapter.getItemCount());

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("resume", "point m323");
        if (mChildEventListener != null) {
            Log.i(mChildEventListener.toString(), "point m355");
        }
    }

    private void attachDatabaseListener() {
        mMessagesDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("We're done loading the initial " + dataSnapshot.getChildrenCount() + " items");
                mProgressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        if (mChildEventListener != null) {
            Log.i(mChildEventListener.toString(), "point m293");
        }
        if (mChildEventListener == null) {
            Log.i("mChildEventListener", "standpoint 298");
            mChildEventListener = new ChildEventListener() {//working with db after authentication
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Log.i("onchildadded", "point M114");

                    //attached to all added child(all past and future child)
                    Post post = dataSnapshot.getValue(Post.class);//as Post has all the three required parameter
                    posts.add(post);

                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    // changed content of a child
                    Log.i("child changed", "point m370");
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    // child deleted
                    Post post = dataSnapshot.getValue(Post.class);//as Post has all the three required parameter

                    for (Iterator<Post> iterator = posts.iterator(); iterator.hasNext(); ) {
                        if (iterator.next().getTimeCurrent() == post.getTimeCurrent())
                            iterator.remove();
                    }
                    Log.i(Integer.toString(posts.size()), "point m389");
                    mAdapter.notifyDataSetChanged();

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
            mMessagesDatabaseReference.addChildEventListener(mChildEventListener);
            Log.i("child addeddd", "point m610");
        }

    }

    private void detachDatabaseReadListener() {
        if (mChildEventListener != null)
            mMessagesDatabaseReference.removeEventListener(mChildEventListener);
        mChildEventListener = null;
    }

    public String calculateTime() {
        return android.text.format.DateFormat.format("MMM dd, yyyy hh:mm:ss aaa", new java.util.Date()).toString();

    }

}
