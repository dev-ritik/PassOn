package com.example.android.passon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class BookRequestActivity extends AppCompatActivity {

    private FirebaseDatabase mfirebaseDatabase;
    public static DatabaseReference mPostDatabaseReference;
    public static DatabaseReference mRequestDatabaseReference;
    public static DatabaseReference mUserDatabaseReference;
    private FirebaseStorage mFirebaseStorage;

    private EditText bookName, filter1, filter2;
    private Button postButton,requestButton;
    private Boolean bookNameEnable = false, filter1Enable = false, filter2Enable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_request);

        bookName = (EditText) findViewById(R.id.edit4);
        filter1 = (EditText) findViewById(R.id.edit5);
        filter2 = (EditText) findViewById(R.id.edit6);
        requestButton = (Button) findViewById(R.id.requestButton);
        requestButton.setEnabled(false);

        mfirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        mRequestDatabaseReference = mfirebaseDatabase.getReference().child("request");
        mUserDatabaseReference = mfirebaseDatabase.getReference().child("user");

        bookName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    bookNameEnable = true;
                } else {
                    bookNameEnable = false;
                }
                if (bookNameEnable && filter1Enable && filter2Enable) {
                    requestButton.setEnabled(true);
                } else {
                    requestButton.setEnabled(false);
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        filter1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    filter1Enable = true;

                } else {
                    filter1Enable = false;
                }
                if (bookNameEnable && filter1Enable && filter2Enable) {
                    requestButton.setEnabled(true);
                } else {
                    requestButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        filter2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    filter2Enable = true;
                } else {
                    filter2Enable = false;
                }
                if (bookNameEnable && filter1Enable && filter2Enable) {
                    requestButton.setEnabled(true);
                } else {
                    requestButton.setEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pust post object to database
                Post post = new Post(1, "a", calculateTime(), bookName.getText().toString(), "d","k", filter1.getText().toString(), filter2.getText().toString(),false);
//                requests.add(post);
                mRequestDatabaseReference.push().setValue(post);
                bookName.setText("");
                filter1.setText("");
                filter2.setText("");
                Toast.makeText(BookRequestActivity.this, "Book added successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(BookRequestActivity.this,Main2Activity.class));
            }
        });

    }

    public String calculateTime() {
        return android.text.format.DateFormat.format("MMM dd, yyyy hh:mm:ss aaa", new java.util.Date()).toString();

    }
}
