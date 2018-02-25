package com.example.android.passon;

import android.content.Intent;
import android.graphics.Movie;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.bumptech.glide.Glide;

//import com.makeramen.roundedimageview.RoundedTransformationBuilder;
//import com.squareup.picasso.Picasso;
//import com.squareup.picasso.Transformation;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static com.example.android.passon.Main2Activity.mUserDatabaseReference;
import static com.example.android.passon.Main2Activity.mUserId;


/*
This class handles backend of profile related stuff
 */
public class ProfileActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    public RecyclerView.Adapter mAdapter;
    public static ChildEventListener mChildEventListenerProfile, mChildEventListenerProfileTest;
    public static DatabaseReference mChildUser;
    ArrayList<ChatHead> chats;
    ArrayList<String> chatsString;
    private LinearLayout requestDialog, contentProfile;
    private TextView userName;
    private ImageView displayPicture, acceptButton, removeDp, galleryDp, cameraDp;
    EditText mobNo;
    Button galleryIntent, cameraIntent;
    FrameLayout layout_MainMenu;
    private static final int CHOOSE_CAMERA_RESULT1 = 1;
    private static final int GALLERY_RESULT2 = 2;
    RelativeLayout dpChangeDialog;
    Uri tempuri;
    public static File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("Profile");

//        mChildUser= Main2Activity.mUserDatabaseReference.orderByChild("userId").equalTo(Main2Activity.mUserId).


//        requestDialog = (LinearLayout) findViewById(R.id.requestDialog);


        layout_MainMenu = (FrameLayout) findViewById(R.id.mainmenu);
        layout_MainMenu.getForeground().setAlpha(0);

        contentProfile = (LinearLayout) findViewById(R.id.contentProfile);
        displayPicture = (ImageView) findViewById(R.id.profile_image);
        TextView userName = (TextView) findViewById(R.id.userName);


        dpChangeDialog = (RelativeLayout) findViewById(R.id.dpChangeDialog);

//        layout_MainMenu.onInterceptTouchEvent()


        displayPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dpChangeDialog.setVisibility(View.VISIBLE);
                layout_MainMenu.getForeground().setAlpha(120);
            }
        });

//        layout_MainMenu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dpChangeDialog.setVisibility(View.INVISIBLE);
//                layout_MainMenu.getForeground().setAlpha(0);
//            }
//        });
        removeDp = (ImageView) findViewById(R.id.removeDp);
        galleryDp = (ImageView) findViewById(R.id.galleryDp);
        cameraDp = (ImageView) findViewById(R.id.cameraDp);

        removeDp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setData(Main2Activity.mUserId, null);
                Toast.makeText(ProfileActivity.this, "removed", Toast.LENGTH_SHORT).show();
                dpChangeDialog.setVisibility(View.INVISIBLE);
                layout_MainMenu.getForeground().setAlpha(0);

            }
        });

        galleryDp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        cameraDp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        if (Main2Activity.mUser != null) {
            userName.setText(Main2Activity.mUser);
        } else {
            userName.setVisibility(View.GONE);
        }

        try {
            if (Main2Activity.mUserProfile != null) {
                Log.i(Main2Activity.mUserProfile.toString(), "standpoint pr60");
//            Glide.with(displayPicture.getContext())
//                    .load(MainActivity.mUserProfile)
//                    .into(displayPicture);

//                displayPicture.setImageURI(MainActivity.mUserProfile);
                com.squareup.picasso.Transformation transformation = new RoundedTransformationBuilder()
                        .cornerRadiusDp(30)
                        .oval(false)
                        .build();
                Picasso.with(ProfileActivity.this)
                        .load(Main2Activity.mUserProfile.toString())
                        .transform(transformation)
                        .into(displayPicture);
            } else {
//                Log.i("profile pic=null", "standpoint pr75");

                displayPicture.setImageResource(R.mipmap.icon_profile_empty);
            }
        } catch (Exception e) {
            displayPicture.setImageResource(R.mipmap.icon_profile_empty);
        }

        TextView emailId = (TextView) findViewById(R.id.email);
        emailId.setText(email);

        mobNo = (EditText) findViewById(R.id.mobile_no);
        galleryIntent = (Button) findViewById(R.id.gallery_intent1);
        cameraIntent = (Button) findViewById(R.id.camera_intent1);

        cameraIntent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(mobNo.getText().toString().equals(""))) {
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    Log.i("point pa220", "camera");

                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                    file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "IMG_" + timeStamp + ".jpg");
                    tempuri = Uri.fromFile(file);
                    i.putExtra(MediaStore.EXTRA_OUTPUT, tempuri);
                    i.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                    startActivityForResult(i, CHOOSE_CAMERA_RESULT1);
                } else {
                    Toast.makeText(ProfileActivity.this, "Please update mobile number first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        galleryIntent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(mobNo.getText().toString().equals(""))) {
                    Log.i("point pa238", "gallery");
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/jpeg");
                    startActivityForResult(intent, GALLERY_RESULT2);
                } else {
                    Toast.makeText(ProfileActivity.this, "Please update mobile number first", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("standpoint pr112");
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (null == imageReturnedIntent) return;
        Uri originalUri = null;
        switch (requestCode) {
            case CHOOSE_CAMERA_RESULT1:
                if (resultCode == RESULT_OK) {
                    if (file.exists()) {
                        Toast.makeText(this, "The image was saved at " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
                        ;
                    }
                    //Uri is at variable tempuri which can be converted to string

                    // code for inserting in database
                }

                break;
            case GALLERY_RESULT2:
                if (resultCode == RESULT_OK) {
                    originalUri = imageReturnedIntent.getData();
                    final int takeFlags = imageReturnedIntent.getFlags()
                            & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                            | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    getContentResolver().takePersistableUriPermission(originalUri, takeFlags);

                    //Uri is originalUri which can be converted to string

                    // code for inserting in database
                }
                break;
        }

    }

    private void setData(String userId, final String dpLink) {

        Log.i(userId, "point pa333");
        Query query = mUserDatabaseReference.orderByChild("userId").equalTo(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    child.getRef().child("connectedUsers").setValue(dpLink);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//return true so that its child work else vic-e-versa
        int x = Math.round(ev.getX());
        int y = Math.round(ev.getY());
//        for (int i = 0; i < 2; i++) {
//            if (isBackground(x, y, ev.)) {
//                return true;
//            }
//    }
        Log.i("x ", x + "");
        Log.i("y ", y + "");

        Log.i("point up", super.dispatchTouchEvent(ev) + "");
        Log.i("point op", isBackground(x, y) + "");


        return isBackground(x, y);
//        return super.dispatchTouchEvent(ev);

    }

    public boolean isBackground(int x, int y) {


        Rect loc = new Rect();
        int[] location = new int[2];

        dpChangeDialog.getLocationOnScreen(location);

        loc.left = location[0];
        loc.top = location[1];
        loc.right = loc.left + dpChangeDialog.getWidth();
        loc.bottom = loc.top + dpChangeDialog.getHeight();
//        Log.i("point pa224 left",loc.left+"");
//        Log.i("point pa225 right",loc.right+"");
//        Log.i("point pa226 top",loc.top+"");
//        Log.i("point pa227 bottom",loc.bottom+"");
//        Log.i("point pa228 height",loc.height()+"");
//        Log.i("point pa229 width",loc.width()+"");
//        Log.i("dl x left",dpChangeDialog.getLeft()+"");
//        Log.i("dl x right",dpChangeDialog.getRight()+"");
//        Log.i("dl y top",dpChangeDialog.getTop()+"");
//        Log.i("dl y bottom",dpChangeDialog.getBottom()+"");

        Log.i("point pa332", (x > loc.left) + "");
        Log.i("point pa333", (x < loc.right) + "");
        Log.i("point pa334", (y < loc.top) + "");
        Log.i("point pa335", (y > loc.bottom) + "");
        Log.i("point pa336", (dpChangeDialog.getVisibility() == View.VISIBLE) + "");
        Log.i("point pa337", (((x > loc.left) || (x < loc.right) || (y < loc.top) || (y > loc.bottom)) && (dpChangeDialog.getVisibility() == View.VISIBLE)) + "");
        if (((x < loc.left) || (x > loc.right) || (y < loc.top) || (y > loc.bottom)) && dpChangeDialog.getVisibility() == View.VISIBLE) {
//            Log.i("point pa337", "false");
            dpChangeDialog.setVisibility(View.INVISIBLE);
            layout_MainMenu.getForeground().setAlpha(0);
            return false;
        } else {
//            Log.i("point pa337", "true");
            return true;
        }

//        return super.dispatchTouchEvent(ev);
    }

}