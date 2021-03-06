package com.example.android.passon;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Movie;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


import static com.example.android.passon.Main2Activity.mUserDatabaseReference;
import static com.example.android.passon.Main2Activity.mUserId;


/*
This class handles backend of profile related stuff
 */
public class ProfileActivity extends AppCompatActivity {

    public static DatabaseReference mChildUser;
    private LinearLayout requestDialog, contentProfile, dpSelectionLayout, dpSelectedLayout;
    private TextView userName;
    private ImageView displayPicture, backgroundButton, removeDp, galleryDp, cameraDp, updateDp, rejectdp, dialogProfileOriginal, dialogProfileChanged;
    EditText mobNo;
    Button galleryIntent, cameraIntent;
    FrameLayout layout_MainMenu;
    private static final int CHOOSE_CAMERA_RESULT1 = 1;
    private static final int GALLERY_RESULT2 = 2;
    private static final int DP_PHOTO_PICKER = 3;
    private static final int DP_PHOTO_CLICKER = 4;
    RatingBar ratingProfile;

    RelativeLayout dpChangeDialog;
    Uri tempuri;
    public static File file;
    InputMethodManager imm;
    private Uri selectedImageUri, downloadUrl, clickedImageUri;
    Bitmap dpCameraimage;
    com.squareup.picasso.Transformation transformationSuccessDp;

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

        Log.i("point pat113", "oncreate");
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
        dialogProfileOriginal = (ImageView) findViewById(R.id.dialogProfileOriginal);
        dialogProfileChanged = (ImageView) findViewById(R.id.dialogProfileChanged);

        ratingProfile = (RatingBar) findViewById(R.id.ratingProfile);
        String[] rating = Main2Activity.userInfo.getRating().split("\\+");
        ratingProfile.setRating(Integer.parseInt(rating[0]) / Integer.parseInt(rating[1]));

        transformationSuccessDp = new RoundedTransformationBuilder()
                .oval(true)
                .build();

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

                if (getPackageManager().hasSystemFeature(
                        PackageManager.FEATURE_CAMERA)) {
                    Log.i("point pat178", "camera");

                    startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), DP_PHOTO_CLICKER);
                } else {
                    Toast.makeText(ProfileActivity.this, "camera not found", Toast.LENGTH_SHORT).show();
                }

            }

        });

        updateDp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImageUri != null) {
                    Log.i(selectedImageUri.toString(), "point pat152");

                    dpChangeDialog.setVisibility(View.INVISIBLE);
                    dpSelectionLayout.setVisibility(View.VISIBLE);
                    dpSelectedLayout.setVisibility(View.INVISIBLE);
                    backgroundButton.setVisibility(View.INVISIBLE);
//                    mProgressBar.setVisibility(View.VISIBLE);
                    StorageReference photoREf = Main2Activity.mDpPhotosStorageReference.child(selectedImageUri.getLastPathSegment());
                    photoREf.putFile(selectedImageUri).addOnSuccessListener(ProfileActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        //                    upload file to firebase onsucess of upload
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            downloadUrl = taskSnapshot.getStorage().getDownloadUrl().getResult();//url of uploaded image
                            Log.i(selectedImageUri.toString(), "point pat163");
//                            mProgressBar.setVisibility(View.INVISIBLE);
                            displayPicture.setImageResource(0);
                            setData(Main2Activity.mUserId, downloadUrl.toString());

                            //progress bar
                            layout_MainMenu.getForeground().setAlpha(0);


                            Picasso.get()
                                    .load(selectedImageUri)
//                                    .placeholder(R.mipmap.icon_profile_empty)
                                    .fit()
                                    .centerCrop()
                                    .placeholder(R.drawable.loading_small)
                                    .error(R.drawable.error)
                                    .transform(transformationSuccessDp)
                                    .into(displayPicture, new com.squareup.picasso.Callback() {
                                        @Override
                                        public void onSuccess() {
                                            Log.i("point pat333", "sucess");

                                            Picasso.get()
                                                    .load(selectedImageUri)
//                                                    .placeholder(R.mipmap.icon_profile_empty)
                                                    .fit()
                                                    .placeholder(R.drawable.loading_big)
                                                    .centerCrop()
                                                    .error(R.drawable.error)
                                                    .transform(transformationSuccessDp)
                                                    .into(dialogProfileOriginal);
                                            downloadUrl = null;
                                            selectedImageUri = null;
                                        }

                                        @Override
                                        public void onError(Exception e) {
                                            Log.i("point pat338", "error");
                                            downloadUrl = null;
                                            selectedImageUri = null;


                                        }
                                    });
                        }

                    });
                } else if (dpCameraimage != null) {
                    String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
                    File myDir = new File(root + "/saved_images");
                    myDir.mkdirs();
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());

                    String fname = "Image-" + timeStamp + ".jpg";
                    File file1 = new File(myDir, fname);
                    if (file1.exists())
                        file1.delete();
                    try {
                        FileOutputStream out = new FileOutputStream(file1);
                        dpCameraimage.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    dpChangeDialog.setVisibility(View.INVISIBLE);
                    dpSelectionLayout.setVisibility(View.VISIBLE);
                    dpSelectedLayout.setVisibility(View.INVISIBLE);
                    backgroundButton.setVisibility(View.INVISIBLE);

                    // Tell the media scanner about the new file so that it is
                    // immediately available to the user.
                    MediaScannerConnection.scanFile(ProfileActivity.this, new String[]{file1.toString()}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                public void onScanCompleted(String path, final Uri uri) {
                                    Log.i("point pat289", "Scanned " + path + ":");
                                    Log.i("point pat290", "uri=" + uri);
                                    clickedImageUri = uri;

                                    StorageReference photoREf = Main2Activity.mDpPhotosStorageReference.child(uri.getLastPathSegment());
                                    photoREf.putFile(clickedImageUri).addOnSuccessListener(ProfileActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        //                    upload file to firebase onsucess of upload
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            downloadUrl = taskSnapshot.getStorage().getDownloadUrl().getResult();//url of uploaded image
                                            Log.i(cameraDp.toString(), "point pa300");
//                            mProgressBar.setVisibility(View.INVISIBLE);
                                            displayPicture.setImageResource(0);
                                            setData(Main2Activity.mUserId, downloadUrl.toString());

                                            //progress bar

                                            Picasso.get()
                                                    .load(uri)
                                                    .placeholder(R.drawable.loading_small)
                                                    .fit()
                                                    .centerCrop()
                                                    .error(R.drawable.error)
                                                    .transform(transformationSuccessDp)
                                                    .into(displayPicture, new com.squareup.picasso.Callback() {
                                                        @Override
                                                        public void onSuccess() {
                                                            Log.i("point pat325", "sucess");
                                                            layout_MainMenu.getForeground().setAlpha(0);
                                                            Picasso.get()
                                                                    .load(clickedImageUri)
                                                                    .placeholder(R.drawable.loading_big)
                                                                    .fit()
                                                                    .centerCrop()
                                                                    .error(R.drawable.error)
                                                                    .transform(transformationSuccessDp)
                                                                    .into(dialogProfileOriginal);

                                                            downloadUrl = null;
                                                            clickedImageUri = null;

                                                        }

                                                        @Override
                                                        public void onError(Exception e) {
                                                            Log.i("point pat333a", "error");
                                                            downloadUrl = null;
                                                            clickedImageUri = null;


                                                        }
                                                    });
                                        }

                                    });
                                }
                            });
                } else {
                    Toast.makeText(ProfileActivity.this, "error", Toast.LENGTH_SHORT).show();
                    layout_MainMenu.getForeground().setAlpha(0);
                    dpChangeDialog.setVisibility(View.INVISIBLE);
                    dpSelectionLayout.setVisibility(View.VISIBLE);
                    dpSelectedLayout.setVisibility(View.INVISIBLE);
                    backgroundButton.setVisibility(View.INVISIBLE);

                }
            }
        });

        rejectdp.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                selectedImageUri = null;
                downloadUrl = null;
                dialogProfileOriginal.setVisibility(View.VISIBLE);
                dialogProfileChanged.setVisibility(View.INVISIBLE);
                layout_MainMenu.getForeground().setAlpha(0);
                dpChangeDialog.setVisibility(View.INVISIBLE);
                dpSelectionLayout.setVisibility(View.VISIBLE);
                dpSelectedLayout.setVisibility(View.INVISIBLE);
                backgroundButton.setVisibility(View.INVISIBLE);
            }
        });

        dpChangeDialog.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                Log.i("point pat215", "pic clicked");
                // must for making clicks not to go to background switch

            }
        });
        backgroundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Picasso.get()
                        .load(Main2Activity.userInfo.getdpUrl())
                        .fit()
                        .centerCrop()
                        .placeholder(R.drawable.loading_big)
                        .error(R.drawable.error)
                        .transform(transformationSuccessDp)
                        .into(dialogProfileChanged);
                dpChangeDialog.setVisibility(View.INVISIBLE);
                backgroundButton.setVisibility(View.INVISIBLE);
                layout_MainMenu.getForeground().setAlpha(0);
                dialogProfileOriginal.setVisibility(View.VISIBLE);
                dialogProfileChanged.setVisibility(View.INVISIBLE);
                Log.i("point pat218", "bb clicked");
            }
        });


        if (Main2Activity.mUser != null)

        {
            userName.setText(Main2Activity.mUser);
        } else

        {
            userName.setVisibility(View.GONE);
        }

        try

        {
            if (Main2Activity.userInfo.getdpUrl() != null) {
                Log.i(Main2Activity.userInfo.getdpUrl(), "point pat271");

//                displayPicture.setImageURI(MainActivity.mUserProfile);

                Picasso.get()
                        .load(Main2Activity.userInfo.getdpUrl())
                        .placeholder(R.drawable.loading_small)
                        .fit()
                        .centerCrop()
                        .error(R.drawable.error)
                        .transform(transformationSuccessDp)
                        .into(displayPicture, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                Log.i("point pat426", "sucess");
                            }

                            @Override
                            public void onError(Exception e) {
                                Log.i("point pat432", "error");

                            }
                        });
                Picasso.get()
                        .load(Main2Activity.userInfo.getdpUrl())
                        .fit()
                        .centerCrop()
                        .error(R.drawable.error)
                        .placeholder(R.drawable.loading_big)
                        .transform(transformationSuccessDp)
                        .into(dialogProfileOriginal, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                Log.i("point pat441", "sucess");
                            }

                            @Override
                            public void onError(Exception e) {
                                Log.i("point pat447", "error");

                            }
                        });

                Picasso.get()
                        .load(Main2Activity.userInfo.getdpUrl())
                        .fit()
                        .centerCrop()
                        .placeholder(R.drawable.loading_big)
                        .error(R.drawable.error)
                        .transform(transformationSuccessDp)
                        .into(dialogProfileChanged, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                Log.i("point pat477", "sucess");
                            }

                            @Override
                            public void onError(Exception e) {
                                Log.i("point pat482", "error");

                            }
                        });
            } else {
//                Log.i("profile pic=null", "standpoint pat75");

                displayPicture.setImageResource(R.mipmap.icon_profile_empty);
            }
        } catch (
                Exception e)

        {
            displayPicture.setImageResource(R.mipmap.icon_profile_empty);
            Log.i("point pat292", "error in dp loading");
        }

        TextView emailId = (TextView) findViewById(R.id.email);
        emailId.setText(email);

        mobNo = (EditText)

                findViewById(R.id.mobile_no);

        galleryIntent = (Button)

                findViewById(R.id.gallery_intent1);

        cameraIntent = (Button)

                findViewById(R.id.camera_intent1);

        cameraIntent.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                if (!(mobNo.getText().toString().equals(""))) {
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    Log.i("point pat220", "camera");

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

        galleryIntent.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                if (!(mobNo.getText().toString().equals(""))) {
                    Log.i("point pat238", "gallery");
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
                Log.i(selectedImageUri.toString(), "point pat286");
                dpSelectionLayout.setVisibility(View.INVISIBLE);
                dpSelectedLayout.setVisibility(View.VISIBLE);
                dialogProfileOriginal.setVisibility(View.INVISIBLE);
                dialogProfileChanged.setVisibility(View.VISIBLE);
//                com.squareup.picasso.Transformation transformation = new RoundedTransformationBuilder()
//                        .oval(false)
//                        .build();
                Picasso.get()
                        .load(selectedImageUri)
                        .placeholder(R.drawable.loading_big)
                        .error(R.drawable.error)
                        .transform(transformationSuccessDp)
                        .fit()
                        .centerCrop()
                        .into(dialogProfileChanged, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                Log.i("point pat357", "sucess");
                            }

                            @Override
                            public void onError(Exception e) {
                                Log.i("point pat362", "error");

                            }
                        });
            } else {
                Toast.makeText(this, "error in getting picture", Toast.LENGTH_SHORT).show();
            }

        }
        if (requestCode == DP_PHOTO_CLICKER) {
            if (resultCode == RESULT_OK) {
                dpSelectionLayout.setVisibility(View.INVISIBLE);
                dpSelectedLayout.setVisibility(View.VISIBLE);
                dialogProfileOriginal.setVisibility(View.INVISIBLE);
                dialogProfileChanged.setVisibility(View.VISIBLE);
                dpCameraimage = (Bitmap) data.getExtras().get("data");
                dialogProfileChanged.setImageBitmap(dpCameraimage);
            } else {
                Toast.makeText(this, "error in clicking picture", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("point pat112");
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


    private void setData(String userId, final String dpLink) {

        Log.i(userId, "point pat333");
        Query query = mUserDatabaseReference.orderByChild("userId").equalTo(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    child.getRef().child("dpUrl").setValue(dpLink).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Main2Activity.userInfo.setdpUrl(dpLink);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
