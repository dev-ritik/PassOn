package com.example.android.passon;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

/* This class handles backend of book post via fab and pushes to db

 */
public class BookPostActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final int CHOOSE_CAMERA_RESULT = 1;
    private static final int GALLERY_RESULT = 2;
    public static File file;
    private FirebaseDatabase mfirebaseDatabase;
    public static DatabaseReference mPostDatabaseReference;
    public static DatabaseReference mUserDatabaseReference;
    private FirebaseStorage mFirebaseStorage;
    public static StorageReference mChatPhotosStorageReference;

    Uri tempuri;

    private EditText bookName,phoneNo,institute;
    Spinner s1, s2;
    String filter1, filter2;
    private Button postButton, cameraIntent, galleryIntent;
    private Boolean bookNameEnable = false, filter1Enable = false, filter2Enable = false;
//    private Uri originalUri = null, downloadUrl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_post);

        bookName = (EditText) findViewById(R.id.edit1);
        phoneNo=(EditText)findViewById(R.id.edit11);
        institute=(EditText)findViewById(R.id.edit12);
        s1 = (Spinner) findViewById(R.id.s1);
        s2 = (Spinner) findViewById(R.id.s2);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.subject_spinner, android.R.layout.simple_spinner_dropdown_item);
        s1.setAdapter(adapter);
        s1.setOnItemSelectedListener(this);
        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(this, R.array.class_spinner, android.R.layout.simple_spinner_dropdown_item);
        s2.setAdapter(adapter1);
        s2.setOnItemSelectedListener(this);
        postButton = (Button) findViewById(R.id.postButton);
//        cameraIntent = (Button) findViewById(R.id.camera_intent);
//        galleryIntent = (Button) findViewById(R.id.gallery_intent);

        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mPostDatabaseReference = mfirebaseDatabase.getReference().child("post1");
        mUserDatabaseReference = mfirebaseDatabase.getReference().child("user1");
        mChatPhotosStorageReference = mFirebaseStorage.getReference("book_photos");

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("point bpa80", "Permission is granted");
            } else {

                Log.v("point bpa83", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("point bpa87", "Permission is granted");
        }


//        cameraIntent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
//                file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "IMG_" + timeStamp + ".jpg");
//                tempuri = Uri.fromFile(file);
//                i.putExtra(MediaStore.EXTRA_OUTPUT, tempuri);
//                i.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
//                startActivityForResult(i, CHOOSE_CAMERA_RESULT);
//            }
//        });
//
//        galleryIntent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("image/jpej");
//                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
//                startActivityForResult(Intent.createChooser(intent, "Complete action using"), GALLERY_RESULT);
//            }
//        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(bookName.getText().toString().equals("") || filter1.equals("") || filter2.equals("") || filter1.equals("Select Subject") || filter2.equals("Select Class"))) {
//                    pust post object to database
                    ArrayList<String> bookRequestUsers=new ArrayList<>();
                    bookRequestUsers.add("ritik");
                    Post post = new Post(1, null, calculateTime(), bookName.getText().toString(), Main2Activity.mUserId, Main2Activity.mUser, filter1, filter2,bookRequestUsers, true,phoneNo.getText().toString(),institute.getText().toString());
//                posts.add(post);
                    PostFragment.mPostDatabaseReference.push().setValue(post);
                    bookName.setText("");
                    filter1 = "";
                    filter2 = "";
                    Toast.makeText(BookPostActivity.this, "Book added successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(BookPostActivity.this, Main2Activity.class));
                } else {
                    Toast.makeText(BookPostActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
//        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
//        if (null == imageReturnedIntent) return;
////        Uri originalUri = null;
//        switch (requestCode) {
//            case CHOOSE_CAMERA_RESULT:
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
//            case GALLERY_RESULT:
//                if (resultCode == RESULT_OK) {
//                    Uri originalUri = imageReturnedIntent.getData();
//                    //Uri is originalUri which can be converted to string
//                    if (originalUri != null) {
//                        Log.i(originalUri.toString(), "point bo165");
//                        StorageReference photoREf = mChatPhotosStorageReference.child(originalUri.getLastPathSegment());
////                              take last part of uri location link and make child of mChatPhotosStorageReference
//                        photoREf.putFile(originalUri).addOnSuccessListener(BookPostActivity.this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                            //                    upload file to firebase onsucess of upload
//                            @Override
//                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                Uri downloadUrl = taskSnapshot.getDownloadUrl();//url of uploaded image
//                                Log.i(downloadUrl.toString(), "standpoint L255");
//                                Log.i("sucess","point L170");
//                                Post post = new Post(1, downloadUrl, calculateTime(), bookName.getText().toString(), Main2Activity.mUserId, Main2Activity.mUser, filter1, filter2, true);
////                posts.add(post);
//                                PostFragment.mPostDatabaseReference.push().setValue(post);
//                                startActivity(new Intent(BookPostActivity.this, Main2Activity.class));
//                            }
//                        });
//                    }
//                }
//                break;
//        }


    public String calculateTime() {
        return android.text.format.DateFormat.format("MMM dd, yyyy hh:mm:ss aaa", new java.util.Date()).toString();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
        Spinner spin = (Spinner) parent;
        Spinner spin2 = (Spinner) parent;
        if (spin.getId() == R.id.s1) {
            TextView selectedTextView = (TextView) view;
            filter1 = selectedTextView.getText().toString();
        }
        if (spin2.getId() == R.id.s2) {
            TextView selectedTextView = (TextView) view;
            filter2 = selectedTextView.getText().toString();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}