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
public class RequestFragment extends Fragment {

    public static RecyclerView mRecyclerViewRequest;
    public static RecyclerView.Adapter mAdapterRequest;
    public static RecyclerView.LayoutManager mLayoutManagerRequest;
    public static ArrayList<Post> requests;

    private FirebaseDatabase mfirebaseDatabase;
    public static DatabaseReference mRequestDatabaseReference;
    public static ChildEventListener mChildEventListenerPost,mChildEventListenerRequest;//to listen the changes in db
    private FirebaseStorage mFirebaseStorage;
    public static StorageReference mChatPhotosStorageReference;

    public RequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_request, container, false);
        mRecyclerViewRequest = (RecyclerView) rootView.findViewById(R.id.request_recycler_view);
        requests = new ArrayList<>();
        mAdapterRequest = new RequestAdapter(requests);
        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mRequestDatabaseReference = mfirebaseDatabase.getReference().child("request1");
        mChatPhotosStorageReference = mFirebaseStorage.getReference("book_photos");
        mRecyclerViewRequest.setAdapter(mAdapterRequest);
        mLayoutManagerRequest = new LinearLayoutManager(rootView.getContext());
        mRecyclerViewRequest.setLayoutManager(mLayoutManagerRequest);
        attachDatabaseListener();

        return rootView;
    }

    private void attachDatabaseListener() {

        if (mChildEventListenerRequest == null) {
//            Log.i("mChildEventListenerPost", "standpoint 298");
            mChildEventListenerRequest = new ChildEventListener() {//working with db after authentication
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Log.i("onchildadded", "point M114");
                    Log.i(Integer.toString(requests.size()), "point m289");

                    //attached to all added child(all past and future child)
                    Post post = dataSnapshot.getValue(Post.class);//as Post has all the three required parameter
                    requests.add(post);
                    if (BooksFragment.mAdapterPost != null) {
                        BooksFragment.mAdapterPost.notifyDataSetChanged();
                    }
                    if(RequestFragment.mAdapterRequest!=null) {
                        RequestFragment.mAdapterRequest.notifyDataSetChanged();
                    }
                    Log.i(Integer.toString(requests.size()), "point m295");
//                    Log.i(Integer.toString(mAdapterPost.getItemCount()), "point m473");
//                    Log.i(Integer.toString(mAdapterRequest.getItemCount()), "point m474");

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

                    for (Iterator<Post> iterator = requests.iterator(); iterator.hasNext(); ) {
                        if (iterator.next().getTime() == post.getTime())
                            iterator.remove();
                        Log.i(Integer.toString(requests.size()), "point m311");
                    }
                    Log.i(Integer.toString(requests.size()), "point m389");
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
            mRequestDatabaseReference.addChildEventListener(mChildEventListenerRequest);
            Log.i("child addeddd", "point m610");
        }

    }
    private void detachDatabaseReadListener() {
        if (mChildEventListenerRequest != null)
            mRequestDatabaseReference.removeEventListener(mChildEventListenerRequest);
        mChildEventListenerRequest = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        attachDatabaseListener();

    }

    @Override
    public void onPause() {
        detachDatabaseReadListener();
//        if (mAuthStateListener != null)
//            mAuth.removeAuthStateListener(mAuthStateListener);
        requests.clear();
        RequestFragment.mAdapterRequest.notifyItemRangeRemoved(0, RequestFragment.mAdapterRequest.getItemCount());
        super.onPause();
    }

}
