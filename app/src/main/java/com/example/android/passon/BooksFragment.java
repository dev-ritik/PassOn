package com.example.android.passon;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class BooksFragment extends Fragment {


    private FirebaseDatabase mfirebaseDatabase;
    public static DatabaseReference mPostDatabaseReference;
    public static DatabaseReference mUserDatabaseReference;
    public static ChildEventListener mChildEventListenerPost,mChildEventListenerRequest;//to listen the changes in db
    private FirebaseStorage mFirebaseStorage;
    public static StorageReference mChatPhotosStorageReference;
    public static ArrayList<Post> posts;

    public static RecyclerView mRecyclerViewPost;
    public static RecyclerView.Adapter mAdapterPost;
    public static RecyclerView.LayoutManager mLayoutManagerPost;

    public BooksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_books, container, false);
        mRecyclerViewPost = (RecyclerView) rootView.findViewById(R.id.post_recycler_view);
        posts = new ArrayList<>();
        mAdapterPost = new PostAdapter(posts);
        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mPostDatabaseReference = mfirebaseDatabase.getReference().child("post1");
        mChatPhotosStorageReference = mFirebaseStorage.getReference("book_photos");
        mRecyclerViewPost.setAdapter(mAdapterPost);
        mLayoutManagerPost = new LinearLayoutManager(rootView.getContext());
        mRecyclerViewPost.setLayoutManager(mLayoutManagerPost);
        attachDatabaseListener();
        Log.i("BooksFrag",mAdapterPost.getItemCount()+ "");
        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();
        attachDatabaseListener();
    }


    @Override
    public void onPause() {
        super.onPause();
        detachDatabaseReadListener();
//        if (mAuthStateListener != null)
//            mAuth.removeAuthStateListener(mAuthStateListener);
        posts.clear();
        mAdapterPost.notifyItemRangeRemoved(0, BooksFragment.mAdapterPost.getItemCount());
    }

    private void attachDatabaseListener() {
        mPostDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("We're done loading the initial " + dataSnapshot.getChildrenCount() + " items");
                Main2Activity.mProgressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


//        if (mChildEventListenerPost != null) {
//            Log.i(mChildEventListenerPost.toString(), "point m293");
//        }
        if (mChildEventListenerPost == null) {
            Log.i("mChildEventListenerPost", "standpoint 298");
            mChildEventListenerPost = new ChildEventListener() {//working with db after authentication
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Log.i("onchildadded", "point M114");
                    Log.i(Integer.toString(posts.size()), "point m289");

                    //attached to all added child(all past and future child)
                    Post post = dataSnapshot.getValue(Post.class);//as Post has all the three required parameter
                    posts.add(post);
                    if (BooksFragment.mAdapterPost != null) {
                        BooksFragment.mAdapterPost.notifyDataSetChanged();
                    }
                    if (RequestFragment.mAdapterRequest != null) {
                        RequestFragment.mAdapterRequest.notifyDataSetChanged();
                    }
                    Log.i(Integer.toString(posts.size()), "point m295");
//                    Log.i(Integer.toString(BooksFragment.mAdapterPost.getItemCount()), "point m420");
//                    Log.i(Integer.toString(RequestFragment.mAdapterRequest.getItemCount()), "point m421");

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
                        if (iterator.next().getTime() == post.getTime())
                            iterator.remove();
                        Log.i(Integer.toString(posts.size()), "point m311");
                    }
                    Log.i(Integer.toString(posts.size()), "point m389");
                    BooksFragment.mAdapterPost.notifyDataSetChanged();
                    RequestFragment.mAdapterRequest.notifyDataSetChanged();

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
            mPostDatabaseReference.addChildEventListener(mChildEventListenerPost);
            Log.i("child addeddd", "point m610");
        }
    }

    private void detachDatabaseReadListener() {
        if (mChildEventListenerPost != null)
            mPostDatabaseReference.removeEventListener(mChildEventListenerPost);
        mChildEventListenerPost = null;
    }

}
