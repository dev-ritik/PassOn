package com.example.android.passon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/*
This class handles deleting infinite firebase nodes
 */
public class MainActivity extends AppCompatActivity {
    public static DatabaseReference mUserDatabaseReference;
    private FirebaseDatabase mfirebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mfirebaseDatabase = FirebaseDatabase.getInstance();

        mUserDatabaseReference = mfirebaseDatabase.getReference().child("user1");
        Log.i("point ma21","attached");
        mUserDatabaseReference.removeValue();
    }
}
