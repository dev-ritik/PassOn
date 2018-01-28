package com.example.android.passon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class BookRequestActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private FirebaseDatabase mfirebaseDatabase;
    public static DatabaseReference mPostDatabaseReference;
    public static DatabaseReference mRequestDatabaseReference;
    public static DatabaseReference mUserDatabaseReference;
    private FirebaseStorage mFirebaseStorage;

    private EditText bookName;
    private Button requestButton;
    Spinner s1,s2;
    String filter1,filter2;
    private Boolean bookNameEnable = false, filter1Enable = false, filter2Enable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_request);

        bookName = (EditText) findViewById(R.id.edit4);
        filter1 = "";
        filter2 = "";
        s1 = (Spinner) findViewById(R.id.s3);
        s2 = (Spinner) findViewById(R.id.s4);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,R.array.subject_spinner, android.R.layout.simple_spinner_dropdown_item);
        s1.setAdapter(adapter);
        s1.setOnItemSelectedListener(this);
        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(this,R.array.class_spinner, android.R.layout.simple_spinner_dropdown_item);
        s2.setAdapter(adapter1);
        s2.setOnItemSelectedListener(this);
        requestButton = (Button) findViewById(R.id.requestButton);

        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mRequestDatabaseReference = mfirebaseDatabase.getReference().child("request");
        mUserDatabaseReference = mfirebaseDatabase.getReference().child("user");

        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(bookName.getText().toString().equals("")||filter1.equals("")||filter2.equals("")||filter1.equals("Select Subject")||filter2.equals("Select Class"))) {
                    //pust post object to database
                    Post post = new Post(1, null, calculateTime(), bookName.getText().toString(), Main2Activity.mUserId,Main2Activity.mUser, filter1, filter2, false);
//                requests.add(post);
                    mRequestDatabaseReference.push().setValue(post);
                    bookName.setText("");
                    filter1 = "";
                    filter2 = "";
                    Toast.makeText(BookRequestActivity.this, "Book added successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(BookRequestActivity.this, Main2Activity.class));
                }else{
                    Toast.makeText(BookRequestActivity.this,"Please fill all the fields",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public String calculateTime() {
        return android.text.format.DateFormat.format("MMM dd, yyyy hh:mm:ss aaa", new java.util.Date()).toString();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
        Spinner spin = (Spinner)parent;
        Spinner spin2 = (Spinner)parent;
        if(spin.getId() == R.id.s3)
        {
            TextView selectedTextView = (TextView) view;
            filter1 = selectedTextView.getText().toString();
        }
        if(spin2.getId() == R.id.s4)
        {
            TextView selectedTextView = (TextView) view;
            filter2 = selectedTextView.getText().toString();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
