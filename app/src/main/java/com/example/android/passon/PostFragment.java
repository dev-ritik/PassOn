package com.example.android.passon;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import java.util.ListIterator;

/**
 * A simple {@link Fragment} subclass.
 */

/*
This class handles backend of posts input from db
serves as main screen data
 */
public class PostFragment extends Fragment {


    private FirebaseDatabase mfirebaseDatabase;
    public static DatabaseReference mPostDatabaseReference;
    public static DatabaseReference mUserDatabaseReference;
    public static ChildEventListener mChildEventListenerPost, mChildEventListenerRequest;//to listen the changes in db
    private FirebaseStorage mFirebaseStorage;
    public static StorageReference mChatPhotosStorageReference;
    public static ArrayList<Post> posts;

    public static RecyclerView mRecyclerViewPost;
    public static RecyclerView.Adapter mAdapterPost;
    public static RecyclerView.LayoutManager mLayoutManagerPost;

    public PostFragment() {
        // Required empty public constructor
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        Log.i("point pf57", "onattach");
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Log.i("point pf64", "oncreate");
//
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_post, container, false);
        mRecyclerViewPost = (RecyclerView) rootView.findViewById(R.id.post_recycler_view);
//        Log.i("point pf59", "oncreateview");
        if (posts == null) {
            posts = new ArrayList<>();
        }
        mAdapterPost = new PostAdapter(posts);
        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mPostDatabaseReference = mfirebaseDatabase.getReference().child("post1");
        mChatPhotosStorageReference = mFirebaseStorage.getReference("book_photos");
        mRecyclerViewPost.setAdapter(mAdapterPost);
        mLayoutManagerPost = new LinearLayoutManager(rootView.getContext());
        mRecyclerViewPost.setLayoutManager(mLayoutManagerPost);
//        attachDatabaseListener();
        Log.i("BooksFrag pf69", mAdapterPost.getItemCount() + "");
        return rootView;

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("point pf77", "onresume");
        attachDatabaseListener();
    }


    @Override
    public void onPause() {
        super.onPause();
//        detachDatabaseReadListener();
//        if (mAuthStateListener != null)
//            mAuth.removeAuthStateListener(mAuthStateListener);
//        posts.clear();
//        mAdapterPost.notifyItemRangeRemoved(0, PostFragment.mAdapterPost.getItemCount());
    }

    private void attachDatabaseListener() {
        mPostDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("We're done loading the initial " + dataSnapshot.getChildrenCount() + " pf95");
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
            Log.i("mChildEventListenerPost", "point pf111");
            mChildEventListenerPost = new ChildEventListener() {//working with db after authentication
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Log.i("onchildadded", "point pf115");
//                    Log.i(s, "point pf1151");
                    Log.i(Integer.toString(posts.size()), "point pf116");

                    //attached to all added child(all past and future child)
                    Post post = dataSnapshot.getValue(Post.class);//as Post has all the three required parameter
                    posts.add(post);
                    if (mAdapterPost != null) {
//                        Log.i("postAdapter", "point pf123");
                        mAdapterPost.notifyDataSetChanged();
//                        Log.i("point pf125", mAdapterPost.getItemCount() + "");

                    }
                    if (RequestFragment.mAdapterRequest != null) {
//                        Log.i("requestAdapter", "point pf128");
                        RequestFragment.mAdapterRequest.notifyDataSetChanged();
                    }
                    Log.i(Integer.toString(posts.size()), "point pf295");
//                    Log.i(Integer.toString(PostFragment.mAdapterPost.getItemCount()), "point m420");
//                    Log.i(Integer.toString(RequestFragment.mAdapterRequest.getItemCount()), "point m421");

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    // changed content of a child
//                    Post post = dataSnapshot.getValue(Post.class);//as Post has all the three required parameter
////                    Log.i("point pf137", post.gey() + "");
//
//                    Log.i("child changed" + post.getBookRequestUsers().get(post.getBookRequestUsers().size() - 1), "point pf138");
//                    Log.i("point pf139", posts.size() + "");
//                    ListIterator<Post> iterator = posts.listIterator();
//                    while (iterator.hasNext()) {
//                        if (iterator.next().getTime().equals(post.getTime()))
//                            iterator.set(post);
//                    }
//                    Log.i("point pf145", posts.size() + "");
//                    Log.i("child changed" + post.getBookRequestUsers().get(post.getBookRequestUsers().size() - 1), "point pf146");
//
//                    PostFragment.mAdapterPost.notifyDataSetChanged();

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    // child deleted
                    Post post = dataSnapshot.getValue(Post.class);//as Post has all the three required parameter
                    Log.i("post deleted", "point pf389");

                    for (Iterator<Post> iterator = posts.iterator(); iterator.hasNext(); ) {
                        if (iterator.next().getTime() == post.getTime())
                            iterator.remove();
//                        Log.i(Integer.toString(posts.size()), "point pf138");
                    }
                    Log.i(Integer.toString(posts.size()), "point pf389");
                    PostFragment.mAdapterPost.notifyDataSetChanged();
//                    RequestFragment.mAdapterRequest.notifyDataSetChanged();

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
            Log.i("child adddd", "point pf182");
        }
    }

    private void detachDatabaseReadListener() {
        if (mChildEventListenerPost != null)
            mPostDatabaseReference.removeEventListener(mChildEventListenerPost);
        mChildEventListenerPost = null;
    }

}
