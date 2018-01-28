package com.example.android.passon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Iterator;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final String ANONYMOUS = "anonymous";
    static boolean backupCalledAlready = false;

    private FirebaseDatabase mfirebaseDatabase;
    public static DatabaseReference mPostDatabaseReference;
    public static DatabaseReference mRequestDatabaseReference;
    public static DatabaseReference mUserDatabaseReference;
//    public static DatabaseReference mUserCountDatabaseReference;
    public static ChildEventListener mChildEventListenerPost, mChildEventListenerRequest;//to listen the changes in db
    private FirebaseStorage mFirebaseStorage;
    public static StorageReference mChatPhotosStorageReference;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

//    private RecyclerView mRecyclerViewPost, mRecyclerViewRequest;
//    public static RecyclerView.Adapter mAdapterPost, mAdapterRequest;
//    private RecyclerView.LayoutManager mLayoutManagerPost, mLayoutManagerRequest;
//
//    ArrayList<Post> posts;
//    ArrayList<Post> requests;
//    ArrayList<String> favouriteArrayList;

    public static ProgressBar mProgressBar;
    private LinearLayout mInputData;
//    private EditText bookName, filter1, filter2;
//    private Button postButton, requestButton;
    private Boolean bookNameEnable = false, filter1Enable = false, filter2Enable = false, userExists = false;
    private String email;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private long count = 0;

    public static String mUserId;
    public static String mUser;
    public static Uri mUserProfile;
    private String mEmailId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mUserId = ANONYMOUS;
        mUserProfile=null;
        mEmailId = "";
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken("163473474337-nrg6f504jf8rgs8rf7ashauuouv3bdf8.apps.googleusercontent.com")
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        SharedPreferences prefs = getSharedPreferences(RegisterActivity.CHAT_PREFS, 0);
        email = prefs.getString("Email", null);
        if (email == null) {
            Log.i("email is null", "standpoint m84");
            Intent intent = new Intent(this, LoginActivity.class);
            finish();
            startActivity(intent);
        }
        if (!backupCalledAlready) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            backupCalledAlready = true;
            //to set up offline compatibility
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mInputData = (LinearLayout) findViewById(R.id.inputData);
//        bookName = (EditText) findViewById(R.id.edit1);
//        filter1 = (EditText) findViewById(R.id.edit2);
//        filter2 = (EditText) findViewById(R.id.edit3);
//        postButton = (Button) findViewById(R.id.postButton);
//        requestButton = (Button) findViewById(R.id.requestButton);

        ViewPager viewPager = (ViewPager) findViewById(R.id.visions_viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        PassOnFragmentPagerAdapter adapter = new PassOnFragmentPagerAdapter(getSupportFragmentManager(),Main2Activity.this);

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Main2Activity.this, BookPostActivity.class));
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Main2Activity.this, BookRequestActivity.class));
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mPostDatabaseReference = mfirebaseDatabase.getReference().child("post");
        mChatPhotosStorageReference = mFirebaseStorage.getReference("book_photos");
        mRequestDatabaseReference = mfirebaseDatabase.getReference().child("request");
        mUserDatabaseReference = mfirebaseDatabase.getReference().child("user");
//        mUserCountDatabaseReference = mfirebaseDatabase.getReference().child("userCount");

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
//        mRecyclerViewPost = (RecyclerView) findViewById(R.id.post_recycler_view);
//        mRecyclerViewRequest = (RecyclerView) findViewById(R.id.request_recycler_view);
        mProgressBar.setVisibility(View.INVISIBLE);

//        posts = new ArrayList<>();
//        requests = new ArrayList<>();
//        favouriteArrayList = new ArrayList<>();
//        favouriteArrayList.add("qwerty");
//
//        mAdapterPost = new PostAdapter(posts);
//        mAdapterRequest = new RequestAdapter(requests);
////        mRecyclerViewPost.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
//
//        mRecyclerViewPost.setAdapter(mAdapterPost);
//        mRecyclerViewRequest.setAdapter(mAdapterRequest);
//
//        mLayoutManagerPost = new LinearLayoutManager(this);
//        mLayoutManagerRequest = new LinearLayoutManager(this);
//
//        mRecyclerViewPost.setLayoutManager(mLayoutManagerPost);
//        mRecyclerViewRequest.setLayoutManager(mLayoutManagerRequest);

//        mRecyclerViewPost.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
//        mRecyclerViewPost.setItemAnimator(new DefaultItemAnimator());

        //working of 3 edit text input
//        bookName.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (charSequence.toString().trim().length() > 0) {
//                    bookNameEnable = true;
//                } else {
//                    bookNameEnable = false;
//                }
//                if (bookNameEnable && filter1Enable && filter2Enable) {
//                    postButton.setEnabled(true);
//                    requestButton.setEnabled(true);
//                } else {
//                    postButton.setEnabled(false);
//                    requestButton.setEnabled(false);
//                }
//
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//            }
//        });
//        filter1.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (charSequence.toString().trim().length() > 0) {
//                    filter1Enable = true;
//
//                } else {
//                    filter1Enable = false;
//                }
//                if (bookNameEnable && filter1Enable && filter2Enable) {
//                    postButton.setEnabled(true);
//                    requestButton.setEnabled(true);
//                } else {
//                    postButton.setEnabled(false);
//                    requestButton.setEnabled(false);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//            }
//        });
//        filter2.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (charSequence.toString().trim().length() > 0) {
//                    filter2Enable = true;
//                } else {
//                    filter2Enable = false;
//                }
//                if (bookNameEnable && filter1Enable && filter2Enable) {
//                    postButton.setEnabled(true);
//                    requestButton.setEnabled(true);
//                } else {
//                    postButton.setEnabled(false);
//                    requestButton.setEnabled(false);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//            }
//        });

//        postButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //pust post object to database
//                Post post = new Post(1, "a", calculateTime(), bookName.getText().toString(), mUserId, mUser, filter1.getText().toString(), filter2.getText().toString(), true);
////                posts.add(post);
//                mPostDatabaseReference.push().setValue(post);
//                bookName.setText("");
//                filter1.setText("");
//                filter2.setText("");
//            }
//        });
//        requestButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //pust post object to database
//                Post post = new Post(1, "a", calculateTime(), bookName.getText().toString(),  mUserId, mUser, filter1.getText().toString(), filter2.getText().toString(), false);
////                requests.add(post);
//                mRequestDatabaseReference.push().setValue(post);
//                bookName.setText("");
//                filter1.setText("");
//                filter2.setText("");
//            }
//        });

//        mAuthStateListener = new FirebaseAuth.AuthStateListener()
//        {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                //to find if user is signed or not
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user != null) {
//                    //user is signed
////                    onSignInitilize(user.getUid(), user.getEmail(),user.getPhotoUrl());
////                    mUser = user.getDisplayName();//send name and operate with db
////                    Log.i(mUser,"standpoint M321");
//                    ArrayList<String> connected=new ArrayList<>();
//                    ArrayList<String> request=new ArrayList<>();
//                    connected.add("qwert");
//                    request.add("weert");
//                    UserInfo userInfo=new UserInfo(1,user.getDisplayName(),user.getUid(),null,user.getEmail(),2,"iitR",123456789,connected,request);
//                    mUserDatabaseReference.push().setValue(UserInfo);
//
//                } else {
//                    //user signed out
//                    onSignOutCleaner();
//                    startActivityForResult((new Intent(getApplicationContext(), com.example.android.anonymoustwitter.LoginActivity.class)),
//                            RC_SIGN_IN);
//                }
//            }
//        };
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.chat) {
//        mUserProfile=mAuth.getCurrentUser().getPhotoUrl();
            Intent intent = new Intent(getApplicationContext(), com.example.android.passon.ChatActivity.class);
            intent.putExtra("person1", "ritik");
            intent.putExtra("person2", "kumar");
            startActivity(intent);

        }else if (id == R.id.profile) {
//        mUserProfile=mAuth.getCurrentUser().getPhotoUrl();
            Intent intent = new Intent(getApplicationContext(), com.example.android.passon.ProfileActivity.class);
            intent.putExtra("email", mEmailId);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
//        detachDatabaseReadListener();
//        if (mAuthStateListener != null)
//            mAuth.removeAuthStateListener(mAuthStateListener);
        mUserId = ANONYMOUS;
        mEmailId = "";
//        posts.clear();
//        requests.clear();
//        mAdapterPost.notifyItemRangeRemoved(0, mAdapterPost.getItemCount());
//        mAdapterRequest.notifyItemRangeRemoved(0, mAdapterRequest.getItemCount());


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.i("point m373", "" + (currentUser == null));
        if (currentUser != null) {
            //user is signed
            mUser=currentUser.getDisplayName();
            mUserId=currentUser.getUid();
            mEmailId=currentUser.getEmail();
            Log.i(currentUser.getUid(), "point m376");
//            Query query = mUserDatabaseReference.orderByChild("userId").equalTo(currentUser.getUid());
//            query.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    count = dataSnapshot.getChildrenCount();
////                    for (DataSnapshot child : dataSnapshot.getChildren()) {
////                        count++;
////                    }
//                    Log.i(Long.toString(count),"point Ma396");
//
//
////                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
////            if(count==mUserDatabaseReference.)
//            if(count==0) {
//                Log.i(currentUser.getUid(), "standpoint m570");
//                Log.i("seems new", "point m558");
//
//                ArrayList<String> connected = new ArrayList<>();
//                ArrayList<String> request = new ArrayList<>();
//                connected.add("qwert");
//                request.add("weert");
//                UserInfo userInfo = new UserInfo(1, currentUser.getDisplayName(), currentUser.getUid(), null, currentUser.getEmail(), 2, "iitR", 123456789, connected, request);
//                mUserDatabaseReference.push().setValue(userInfo);
//            }
//            attachDatabaseListener();//take input from database
        } else if (currentUser == null)

        {
            Log.i("current user is null", "standpoint m389");
            Intent intent = new Intent(this, LoginActivity.class);
            finish();
            startActivity(intent);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
//        attachDatabaseListener();
//        if (mAuthStateListener != null)
//            mAuth.removeAuthStateListener(mAuthStateListener);
        Log.i("resume", "point m323");
//        if (mChildEventListenerPost != null) {
//            Log.i(mChildEventListenerPost.toString(), "point m355");
    }


//    private void attachDatabaseListener() {
//        mPostDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                System.out.println("We're done loading the initial " + dataSnapshot.getChildrenCount() + " items");
//                mProgressBar.setVisibility(View.INVISIBLE);
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//
////        if (mChildEventListenerPost != null) {
////            Log.i(mChildEventListenerPost.toString(), "point m293");
////        }
//        if (mChildEventListenerPost == null) {
//            Log.i("mChildEventListenerPost", "standpoint 298");
//            mChildEventListenerPost = new ChildEventListener() {//working with db after authentication
//                @Override
//                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                    Log.i("onchildadded", "point M114");
////                    Log.i(Integer.toString(posts.size()), "point m289");
//
//                    //attached to all added child(all past and future child)
//                    Post post = dataSnapshot.getValue(Post.class);//as Post has all the three required parameter
//                    posts.add(post);
//                    mAdapterPost.notifyDataSetChanged();
//                    mAdapterRequest.notifyDataSetChanged();
////                    Log.i(Integer.toString(posts.size()), "point m295");
////                    Log.i(Integer.toString(mAdapterPost.getItemCount()), "point m420");
////                    Log.i(Integer.toString(mAdapterRequest.getItemCount()), "point m421");
//
//                }
//
//                @Override
//                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                    // changed content of a child
//                    Log.i("child changed", "point m370");
//                }
//
//                @Override
//                public void onChildRemoved(DataSnapshot dataSnapshot) {
//                    // child deleted
//                    Post post = dataSnapshot.getValue(Post.class);//as Post has all the three required parameter
//
//                    for (Iterator<Post> iterator = posts.iterator(); iterator.hasNext(); ) {
//                        if (iterator.next().getTime() == post.getTime())
//                            iterator.remove();
//                        Log.i(Integer.toString(posts.size()), "point m311");
//                    }
//                    Log.i(Integer.toString(posts.size()), "point m389");
//                    mAdapterPost.notifyDataSetChanged();
//                    mAdapterRequest.notifyDataSetChanged();
//
//                }
//
//                @Override
//                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//                    //moved position of a child
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    // error or permission denied
//                }
//            };
//            mPostDatabaseReference.addChildEventListener(mChildEventListenerPost);
//            Log.i("child addeddd", "point m610");
//        }
//        if (mChildEventListenerRequest == null) {
////            Log.i("mChildEventListenerPost", "standpoint 298");
//            mChildEventListenerRequest = new ChildEventListener() {//working with db after authentication
//                @Override
//                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                    Log.i("onchildadded", "point M114");
////                    Log.i(Integer.toString(requests.size()), "point m289");
//
//                    //attached to all added child(all past and future child)
//                    Post post = dataSnapshot.getValue(Post.class);//as Post has all the three required parameter
//                    requests.add(post);
//                    mAdapterPost.notifyDataSetChanged();
//                    mAdapterRequest.notifyDataSetChanged();
//                    Log.i(Integer.toString(requests.size()), "point m295");
//                    Log.i(Integer.toString(mAdapterPost.getItemCount()), "point m473");
//                    Log.i(Integer.toString(mAdapterRequest.getItemCount()), "point m474");
//
//                }
//
//                @Override
//                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                    // changed content of a child
//                    Log.i("child changed", "point m370");
//                }
//
//                @Override
//                public void onChildRemoved(DataSnapshot dataSnapshot) {
//                    // child deleted
//                    Post post = dataSnapshot.getValue(Post.class);//as Post has all the three required parameter
//
//                    for (Iterator<Post> iterator = requests.iterator(); iterator.hasNext(); ) {
//                        if (iterator.next().getTime() == post.getTime())
//                            iterator.remove();
//                        Log.i(Integer.toString(requests.size()), "point m311");
//                    }
//                    Log.i(Integer.toString(requests.size()), "point m389");
//                    mAdapterPost.notifyDataSetChanged();
//                    mAdapterRequest.notifyDataSetChanged();
//
//                }
//
//                @Override
//                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//                    //moved position of a child
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    // error or permission denied
//                }
//            };
//            mRequestDatabaseReference.addChildEventListener(mChildEventListenerRequest);
//            Log.i("child addeddd", "point m610");
//        }
//
//    }
//
//    private void detachDatabaseReadListener() {
//        if (mChildEventListenerPost != null)
//            mPostDatabaseReference.removeEventListener(mChildEventListenerPost);
//        mChildEventListenerPost = null;
//        if (mChildEventListenerRequest != null)
//            mPostDatabaseReference.removeEventListener(mChildEventListenerRequest);
//        mChildEventListenerRequest = null;
//    }

    public String calculateTime() {
        return android.text.format.DateFormat.format("MMM dd, yyyy hh:mm:ss aaa", new java.util.Date()).toString();

    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        mUserId = ANONYMOUS;
        mEmailId = "";
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
        SharedPreferences prefs = getSharedPreferences(RegisterActivity.CHAT_PREFS, 0);
        prefs.edit().putString("Email", null).apply();
        prefs.edit().putString("Password", null).apply();
        Intent intent = new Intent(this, LoginActivity.class);
        finish();
        startActivity(intent);
    }

}
