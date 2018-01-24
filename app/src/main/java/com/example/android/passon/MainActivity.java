package com.example.android.passon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity {

    static boolean backupCalledAlready = false;

    private FirebaseDatabase mfirebaseDatabase;
    public static DatabaseReference mMessagesDatabaseReference;
    public static ChildEventListener mChildEventListener;//to listen the changes in db
    private FirebaseStorage mFirebaseStorage;
    public static StorageReference mChatPhotosStorageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!backupCalledAlready)
        {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            backupCalledAlready = true;
        }
        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mMessagesDatabaseReference = mfirebaseDatabase.getReference().child("input");
        mChatPhotosStorageReference = mFirebaseStorage.getReference("book_photos");

    }
}
