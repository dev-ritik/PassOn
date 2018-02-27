package com.example.android.passon;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Movie;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.bumptech.glide.Glide;

//import com.makeramen.roundedimageview.RoundedTransformationBuilder;
//import com.squareup.picasso.Picasso;
//import com.squareup.picasso.Transformation;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Callback;
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
    private LinearLayout requestDialog, contentProfile, dpSelectionLayout, dpSelectedLayout;
    private TextView userName;
    private ImageView displayPicture, backgroundButton, removeDp, galleryDp, cameraDp, updateDp, rejectdp, dialogProfile;
    EditText mobNo;
    Button galleryIntent, cameraIntent;
    FrameLayout layout_MainMenu;
    private static final int CHOOSE_CAMERA_RESULT1 = 1;
    private static final int GALLERY_RESULT2 = 2;
    private static final int DP_PHOTO_PICKER = 3;
    RatingBar ratingProfile;

    RelativeLayout dpChangeDialog;
    Uri tempuri;
    public static File file;
    InputMethodManager imm;
    private Uri selectedImageUri, downloadUrl;
    boolean ref = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");

        View view = this.getCurrentFocus();
        if (view != null) {
        }

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("Profile");

//        mChildUser= Main2Activity.mUserDatabaseReference.orderByChild("userId").equalTo(Main2Activity.mUserId).


//        requestDialog = (LinearLayout) findViewById(R.id.requestDialog);

        Log.i("point pa113", "oncreate");
        layout_MainMenu = (FrameLayout) findViewById(R.id.mainmenu);
        layout_MainMenu.getForeground().setAlpha(0);

        contentProfile = (LinearLayout) findViewById(R.id.contentProfile);
        dpChangeDialog = (RelativeLayout) findViewById(R.id.dpChangeDialog);
        dpSelectedLayout = (LinearLayout) findViewById(R.id.dpSelectedLayout);
        dpSelectionLayout = (LinearLayout) findViewById(R.id.dpSelectionLayout);

        displayPicture = (ImageView) findViewById(R.id.profile_image);
        TextView userName = (TextView) findViewById(R.id.userName);
        removeDp = (ImageView) findViewById(R.id.removeDp);
        galleryDp = (ImageView) findViewById(R.id.galleryDp);
        cameraDp = (ImageView) findViewById(R.id.cameraDp);
        updateDp = (ImageView) findViewById(R.id.updateDp);
        rejectdp = (ImageView) findViewById(R.id.rejectPic);
        backgroundButton = (ImageView) findViewById(R.id.backgroundButton);
        dialogProfile = (ImageView) findViewById(R.id.dialogProfile);

        ratingProfile = (RatingBar) findViewById(R.id.ratingProfile);
        String[] rating = Main2Activity.userInfo.getRating().split("\\+");
        ratingProfile.setRating(Integer.parseInt(rating[0]) / Integer.parseInt(rating[1]));

        displayPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow((null == getCurrentFocus()) ? null : getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                dpChangeDialog.setVisibility(View.VISIBLE);
                backgroundButton.setVisibility(View.VISIBLE);
                layout_MainMenu.getForeground().setAlpha(120);
            }
        });

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

                // TODO: Fire an intent to show an image picker
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpej");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), DP_PHOTO_PICKER);
            }
        });
        cameraDp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }

        });
        updateDp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImageUri != null) {
                    Log.i(selectedImageUri.toString(), "point pa152");
//                    mProgressBar.setVisibility(View.VISIBLE);
                    StorageReference photoREf = Main2Activity.mDpPhotosStorageReference.child(selectedImageUri.getLastPathSegment());
                    photoREf.putFile(selectedImageUri).addOnSuccessListener(ProfileActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        //                    upload file to firebase onsucess of upload
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            downloadUrl = taskSnapshot.getDownloadUrl();//url of uploaded image
                            Log.i(selectedImageUri.toString(), "point pa163");
//                            mProgressBar.setVisibility(View.INVISIBLE);
                            displayPicture.setImageResource(0);
                            setData(Main2Activity.mUserId, downloadUrl.toString());
                            downloadUrl = null;
                            selectedImageUri = null;

                            com.squareup.picasso.Transformation transformation = new RoundedTransformationBuilder()
                                    .cornerRadiusDp(30)
                                    .oval(false)
                                    .build();
                            Picasso.with(ProfileActivity.this)
                                    .load(selectedImageUri)
                                    .placeholder(R.mipmap.icon_profile_empty)
                                    .transform(transformation)
                                    .into(displayPicture, new com.squareup.picasso.Callback() {
                                        @Override
                                        public void onSuccess() {
                                            Log.i("point pa333", "sucess");
                                        }

                                        @Override
                                        public void onError() {
                                            Log.i("point pa338", "error");

                                        }
                                    });
                        }

                    });
                } else {
                    Toast.makeText(ProfileActivity.this, "error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        rejectdp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedImageUri = null;
                downloadUrl = null;
            }
        });

        dpChangeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("point pa215", "pic clicked");
                // must for making clicks not to go to background switch

            }
        });
        backgroundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpChangeDialog.setVisibility(View.INVISIBLE);
                backgroundButton.setVisibility(View.INVISIBLE);
                layout_MainMenu.getForeground().setAlpha(0);
                Log.i("point pa218", "bb clicked");
            }
        });


//        if (Main2Activity.userInfo.getdpUrl().length() != 0) {
//
////            Glide.with(displayPicture.getContext())
////                    .load(Main2Activity.userInfo.getdpUrl())
////                    .into(displayPicture);
//        }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {//signing prosses result called before onResume
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == DP_PHOTO_PICKER) {
            if (resultCode == RESULT_OK) {

                selectedImageUri = data.getData();
                Log.i(selectedImageUri.toString(), "point pa286");
//            displayPicture.setImageURI(selectedImageUri);
                dpSelectionLayout.setVisibility(View.INVISIBLE);
                dpSelectedLayout.setVisibility(View.VISIBLE);
                com.squareup.picasso.Transformation transformation = new RoundedTransformationBuilder()
                        .cornerRadiusDp(30)
                        .oval(false)
                        .build();
                Picasso.with(this)
                        .load(selectedImageUri)
                        .placeholder(R.mipmap.icon_profile_empty)
                        .transform(transformation)
                        .into(dialogProfile, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                Log.i("point pa357", "sucess");
                            }

                            @Override
                            public void onError() {
                                Log.i("point pa362", "error");

                            }
                        });
            } else {
                Toast.makeText(this, "error in getting picture", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("point pr112");
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
//        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
//        if (null == imageReturnedIntent) return;
//        Uri originalUri = null;
//        switch (requestCode) {
//            case CHOOSE_CAMERA_RESULT1:
//                if (resultCode == RESULT_OK) {
//                    if (file.exists()) {
//                        Toast.makeText(this, "The image was saved at " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
//                        ;
//                    }
//                    //Uri is at variable tempuri which can be converted to string
//
//                    // code for inserting in database
//                }
//
//                break;
//            case GALLERY_RESULT2:
//                if (resultCode == RESULT_OK) {
//                    originalUri = imageReturnedIntent.getData();
//                    final int takeFlags = imageReturnedIntent.getFlags()
//                            & (Intent.FLAG_GRANT_READ_URI_PERMISSION
//                            | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//                    getContentResolver().takePersistableUriPermission(originalUri, takeFlags);
//
//                    //Uri is originalUri which can be converted to string
//                    // code for inserting in database
//                }
//                break;
//        }
//
//    }

    private void setData(String userId, final String dpLink) {

        Log.i(userId, "point pa333");
        Query query = mUserDatabaseReference.orderByChild("userId").equalTo(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    child.getRef().child("dpUrl").setValue(dpLink);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
////return true so that its child work else vic-e-versa
//        int x = Math.round(ev.getX());
//        int y = Math.round(ev.getY());
////
//        Log.i("x ", x + "");
//        Log.i("y ", y + "");
////
//        Log.i("point up", super.dispatchTouchEvent(ev) + "");
//        Log.i("point op", isBackground(x, y) + "");
//
//
////        return isBackground(x, y);
//
////        return super.dispatchTouchEvent(ev);
////        if(ref){
////            ref=false;
////        }else ref=true;
//
//        boolean a=layout_MainMenu.onInterceptTouchEvent(ev);
//        layout_MainMenu.dispatchTouchEvent(ev);
//        Log.i("point pa386", ref + "");
//        return ref;
//
//    }
//
//    public boolean isBackground(int x, int y) {
//
//
//        Rect loc = new Rect();
//        int[] location = new int[2];
//
//        dpChangeDialog.getLocationOnScreen(location);
//
//        loc.left = location[0];
//        loc.top = location[1];
//        loc.right = loc.left + dpChangeDialog.getWidth();
//        loc.bottom = loc.top + dpChangeDialog.getHeight();
//
////        Log.i("point pa224 left",loc.left+"");
////        Log.i("point pa225 right",loc.right+"");
////        Log.i("point pa226 top",loc.top+"");
////        Log.i("point pa227 bottom",loc.bottom+"");
////        Log.i("point pa228 height",loc.height()+"");
////        Log.i("point pa229 width",loc.width()+"");
//
////        Log.i("dl x left", dpChangeDialog.getLeft() + "");
////        Log.i("dl x right", dpChangeDialog.getRight() + "");
////        Log.i("dl y top", dpChangeDialog.getTop() + "");
////        Log.i("dl y bottom", dpChangeDialog.getBottom() + "");
//
//        Log.i("point pa332", (x > loc.left) + "");
//        Log.i("point pa333", (x < loc.right) + "");
//        Log.i("point pa334", (y < loc.top) + "");
//        Log.i("point pa335", (y > loc.bottom) + "");
//        Log.i("point pa336", (dpChangeDialog.getVisibility() == View.VISIBLE) + "");
//        Log.i("point pa337", (((x > loc.left) || (x < loc.right) || (y < loc.top) || (y > loc.bottom)) && (dpChangeDialog.getVisibility() == View.VISIBLE)) + "");
//        if (((x < loc.left) || (x > loc.right) || (y < loc.top) || (y > loc.bottom)) && dpChangeDialog.getVisibility() == View.VISIBLE) {
////            Log.i("point pa337", "false");
//            dpChangeDialog.setVisibility(View.INVISIBLE);
//            layout_MainMenu.getForeground().setAlpha(0);
//            return false;
//        } else {
////            Log.i("point pa337", "true");
//            return true;
//        }
//
//    }

}


//    Uri uri = profile.getProfilePictureUri(28, 28);
//    com.squareup.picasso.Transformation transformation = new RoundedTransformationBuilder()
//            .cornerRadiusDp(30)
//            .oval(false)
//            .build();
//        Picasso.with(this)
//                .load(uri)
//                .placeholder(R.drawable.icon_profile_empty)
//                .transform(transformation)
//                .into(accountButton);
//.into(myImage,  new ImageLoadedCallback(progressBar) {
//@Override
//public void onSuccess() {
//        if (this.progressBar != null) {
//        this.progressBar.setVisibility(View.GONE);
//        }
//        }
//        });