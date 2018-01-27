package com.example.android.passon;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

public class ChatActivity extends AppCompatActivity {
    static boolean backupCalledAlready = false;

    private FirebaseDatabase mfirebaseDatabase;
    public static DatabaseReference mChatDatabaseReference;
    private static ChildEventListener mChildEventListener;//to listen the changes in db
    private FirebaseStorage mFirebaseStorage;

    private RecyclerView mRecyclerView;
    public static RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    ArrayList<Chat> chats;

//    private ProgressBar mProgressBar;
    private LinearLayout mInputData;
    private EditText editChat;
    private ImageButton photopickerButton,sendButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        String person1 = intent.getStringExtra("person1");
        String person2 = intent.getStringExtra("person2");
//        if (!backupCalledAlready) {
//            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
//            backupCalledAlready = true;
//            //to set up offline compatibility
//        }
        mfirebaseDatabase = FirebaseDatabase.getInstance();

        mFirebaseStorage = FirebaseStorage.getInstance();
//        mChatPhotosStorageReference = mFirebaseStorage.getReference("book_photos");
        mChatDatabaseReference = mfirebaseDatabase.getReference().child("ChatRoom").child(person1+person2);

//        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mRecyclerView = (RecyclerView) findViewById(R.id.chat_recycler_view);
//        mProgressBar.setVisibility(View.INVISIBLE);
        editChat=(EditText)findViewById(R.id.messageEditText);
        sendButton=(ImageButton)findViewById(R.id.sendButtonChat);
        photopickerButton=(ImageButton)findViewById(R.id.photoPickerButton);
        attachDatabaseListener();//take input from database

        chats = new ArrayList<>();

        mAdapter = new ChatAdapter(chats);
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //working of 3 edit text input
        editChat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    sendButton.setEnabled(true);
                } else {
                    sendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pust post object to database
                 Chat chat= new Chat(editChat.getText().toString(),calculateTime(),"Ritik");
                mChatDatabaseReference.push().setValue(chat);
                editChat.setText("");
            }
        });
        

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main2, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        detachDatabaseReadListener();
        chats.clear();
        mAdapter.notifyItemRangeRemoved(0, mAdapter.getItemCount());

    }

    @Override
    protected void onResume() {
        super.onResume();
        attachDatabaseListener();
        Log.i("resume", "point Ch323");
//        if (mChildEventListener != null) {
//            Log.i(mChildEventListener.toString(), "point m355");
    }


    private void attachDatabaseListener() {
        mChatDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("We're done loading the initial " + dataSnapshot.getChildrenCount() + " items");
//                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        
        if (mChildEventListener == null) {
//            Log.i("mChildEventListener", "standpoint CH298");
            mChildEventListener = new ChildEventListener() {//working with db after authentication
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Log.i("onchildadded", "point CH114");
                    Log.i(Integer.toString(chats.size()), "point CH289");

                    //attached to all added child(all past and future child)
                    Chat chat = dataSnapshot.getValue(Chat.class);//as Post has all the three required parameter
                    chats.add(chat);
                    mAdapter.notifyDataSetChanged();
                    Log.i(Integer.toString(chats.size()), "point Ch295");

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    // changed content of a child
                    Log.i("child changed", "point m370");
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    // child deleted
                    Chat chat = dataSnapshot.getValue(Chat.class);//as Post has all the three required parameter

                    for (Iterator<Chat> iterator = chats.iterator(); iterator.hasNext(); ) {
                        if (iterator.next().getTime() == chat.getTime())
                            iterator.remove();
                        Log.i(Integer.toString(chats.size()), "point CH311");
                    }
                    Log.i(Integer.toString(chats.size()), "point CH389");
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
            mChatDatabaseReference.addChildEventListener(mChildEventListener);
            Log.i("child addeddd", "point CH610");
        }

    }

    private void detachDatabaseReadListener() {
        if (mChildEventListener != null)
            mChatDatabaseReference.removeEventListener(mChildEventListener);
        mChildEventListener = null;
    }

    public String calculateTime() {
        return android.text.format.DateFormat.format("MMM dd, yyyy hh:mm:ss aaa", new java.util.Date()).toString();

    }

}
