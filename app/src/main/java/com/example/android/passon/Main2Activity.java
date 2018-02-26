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
import android.widget.ImageView;
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

/*
This class handles backend all firebase reference, user node updating, navigation drawer handling
 */
public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final String ANONYMOUS = "anonymous";
    static boolean backupCalledAlready = false;

    private FirebaseDatabase mfirebaseDatabase;
    public static DatabaseReference mPostDatabaseReference;
    public static DatabaseReference mRequestDatabaseReference;
    public static DatabaseReference mUserDatabaseReference;
    //    public static DatabaseReference mUserCountDatabaseReference;
    public static ChildEventListener mUserEventListener;//to listen the changes in db
    private FirebaseStorage mFirebaseStorage;
    public static StorageReference mChatPhotosStorageReference;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    public static ProgressBar mProgressBar;
    private LinearLayout mInputData;
    private String email;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private long count = 0;

    public static ArrayList<UserInfo> userInfos;

    public static UserInfo userInfo;

    public static String mUserId;
    public static String mUser;
    public static Uri mUserProfile;
    private String mEmailId;
    private TextView naveUserName, naveUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mUserId = ANONYMOUS;
        mUser = ANONYMOUS;
        mUserProfile = null;
        mEmailId = "";
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken("163473474337-nrg6f504jf8rgs8rf7ashauuouv3bdf8.apps.googleusercontent.com")
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        SharedPreferences prefs = getSharedPreferences(RegisterActivity.CHAT_PREFS, 0);
        email = prefs.getString("Email", null);

        Log.i("point ma107", "reached");

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

        userInfos = new ArrayList<>();

        mInputData = (LinearLayout) findViewById(R.id.inputData);
        naveUserName = (TextView) findViewById(R.id.nav_user_name);
        naveUserEmail = (TextView) findViewById(R.id.nav_user_id);

//        naveUserName.setText(mUser);
//        naveUserEmail.setText(mEmailId);

        ViewPager viewPager = (ViewPager) findViewById(R.id.visions_viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        PassOnFragmentPagerAdapter adapter = new PassOnFragmentPagerAdapter(getSupportFragmentManager(), Main2Activity.this);

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
        mPostDatabaseReference = mfirebaseDatabase.getReference().child("post1");
        mChatPhotosStorageReference = mFirebaseStorage.getReference("book_photos");
        mRequestDatabaseReference = mfirebaseDatabase.getReference().child("request1");
        mUserDatabaseReference = mfirebaseDatabase.getReference().child("user1");
//        mUserCountDatabaseReference = mfirebaseDatabase.getReference().child("userCount");

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
//        mRecyclerViewPost = (RecyclerView) findViewById(R.id.post_recycler_view);
//        mRecyclerViewRequest = (RecyclerView) findViewById(R.id.request_recycler_view);
        mProgressBar.setVisibility(View.INVISIBLE);


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


//        if (mUserEventListener == null) {
//            Log.i("mUserEventListener", "point ma212");
//            mUserEventListener = new ChildEventListener() {//working with db after authentication
//                @Override
//                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                    Log.i("onchildadded", "point M114");
//                    Log.i("point ma221", dataSnapshot.getChildrenCount() + "");
//                    Log.i("point ma222", dataSnapshot.toString());
//                    Log.i("point ma222a", dataSnapshot.getValue().toString());
//                    Log.i("point ma223", dataSnapshot.getChildren().toString());
//
//                    //attached to all added child(all past and future child)
//                    UserInfo userInfo1 = dataSnapshot.getValue(UserInfo.class);//as Post has all the three required parameter
//                    userInfos.add(userInfo1);
//
//                    Log.i(Integer.toString(userInfos.size()), "point m228");
//
//                }
//
//                @Override
//                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                    // changed content of a child
//                    Log.i("child changed", "point m235");
//                }
//
//                @Override
//                public void onChildRemoved(DataSnapshot dataSnapshot) {
//                    // child deleted
////                    Post post = dataSnapshot.getValue(Post.class);//as Post has all the three required parameter
////
////                    for (Iterator<UserInfo> iterator = userInfos.iterator(); iterator.hasNext(); ) {
////                        if (iterator.next().getTime() == post.getTime())
////                            iterator.remove();
////                        Log.i(Integer.toString(posts.size()), "point m311");
////                    }
////                    Log.i(Integer.toString(posts.size()), "point m389");
////                    PostFragment.mAdapterPost.notifyDataSetChanged();
////                    RequestFragment.mAdapterRequest.notifyDataSetChanged();
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
//            mUserDatabaseReference.addChildEventListener(mUserEventListener);
//            Log.i("child addeddd", "point m265");
//        }


//        Query query1 = mUserDatabaseReference.orderByChild("userId").equalTo(mUserId);
//        Log.i("point ma271", mUserId);
//        Log.i("point ma272", "here");
//        query1.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Log.i("point ma308", "child added");
//                Log.i(Integer.toString(userInfos.size()), "point m274");
//
//                //attached to all added child(all past and future child)
//                userInfo = dataSnapshot.getValue(UserInfo.class);//as Post has all the three required parameter
//
//                Log.i(userInfo.getPhoneNo() + "", "point m279");
//
//
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent intent = new Intent(getApplicationContext(), com.example.android.passon.ProfileActivity.class);
            intent.putExtra("email", mEmailId);
            startActivity(intent);
        } else if (id == R.id.nav_notifications) {
            startActivity(new Intent(Main2Activity.this, NotificationActivity.class));
        } else if (id == R.id.nav_transactions) {

        } else if (id == R.id.nav_logout) {
            logout();
        } else if (id == R.id.nav_app_info) {
            startActivity(new Intent(Main2Activity.this, AppInfoActivity.class));
        } else if (id == R.id.nav_user_feedback) {
            startActivity(new Intent(Main2Activity.this, SendFeedbackActivity.class));
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
//        mUserId = ANONYMOUS;
//        mEmailId = "";
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
            mUser = currentUser.getDisplayName();
            mUserId = currentUser.getUid();
            mEmailId = currentUser.getEmail();
            Log.i(currentUser.getUid(), "point m376");
            Query query = mUserDatabaseReference.orderByChild("userId").equalTo(currentUser.getUid());
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    count = dataSnapshot.getChildrenCount();
                    if (count == 0) {
                        Log.i(currentUser.getUid(), "standpoint m570");
//                        Log.i("seems new", "point m558");

                        ArrayList<String> connected = new ArrayList<>();
                        ArrayList<String> notifications = new ArrayList<>();
                        ArrayList<ChatHead> request = new ArrayList<>();
                        connected.add("qwert");
                        notifications.add("asdfghj d g dfgdg");
//                        request.add(new ChatHead("dcd","scs"));
                        UserInfo userInfo = new UserInfo(1, currentUser.getDisplayName(), currentUser.getUid(), "abc", currentUser.getEmail(), 2, "iitR", 123456789, connected, request, notifications);
                        mUserDatabaseReference.push().setValue(userInfo);
                    }

//                    Log.i("point Ma396", Long.toString(count));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
//            if(count==mUserDatabaseReference.)
//            Log.i("seems new", "point m462");
//            Log.i("point ma271", mUserId);
//            Log.i("point ma272", "here");
            query.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Log.i("point ma308", "child added");
                    Log.i(Integer.toString(userInfos.size()), "point m274");

                    //attached to all added child(all past and future child)
                    userInfo = dataSnapshot.getValue(UserInfo.class);//as Post has all the three required parameter

                    Log.i(userInfo.getPhoneNo() + "", "point m279");


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

        } else {
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


    public String calculateTime() {
        return android.text.format.DateFormat.format("MMM dd, yyyy hh:mm:ss aaa", new java.util.Date()).toString();

    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
        mUserId = ANONYMOUS;
        mUser = ANONYMOUS;
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
