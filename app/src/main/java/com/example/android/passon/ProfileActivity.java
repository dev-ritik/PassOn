package com.example.android.passon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.TextView;

//import com.bumptech.glide.Glide;

//import com.makeramen.roundedimageview.RoundedTransformationBuilder;
//import com.squareup.picasso.Picasso;
//import com.squareup.picasso.Transformation;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.android.passon.Main2Activity.mUserId;

//import static com.example.android.passon.MainActivity.mUserId;

public class ProfileActivity extends AppCompatActivity {

    ArrayList<Post> feeds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("Profile");


        TextView userName = (TextView)findViewById(R.id.userName);
        ImageView profilePic = (ImageView)findViewById(R.id.profile_image);
        if (Main2Activity.mUser != null) {
            userName.setText(Main2Activity.mUser);
        } else {
            userName.setVisibility(View.GONE);
        }

        try {
            if (Main2Activity.mUserProfile != null) {
                Log.i(Main2Activity.mUserProfile.toString(), "standpoint pr60");
//            Glide.with(profilePic.getContext())
//                    .load(MainActivity.mUserProfile)
//                    .into(profilePic);

//                profilePic.setImageURI(MainActivity.mUserProfile);
                com.squareup.picasso.Transformation transformation = new RoundedTransformationBuilder()
                        .cornerRadiusDp(30)
                        .oval(false)
                        .build();
                Picasso.with(ProfileActivity.this)
                        .load(Main2Activity.mUserProfile.toString())
                        .transform(transformation)
                        .into(profilePic);
            } else {
                Log.i("profile pic=null", "standpoint pr75");

                profilePic.setImageResource(R.mipmap.icon_profile_empty);
            }
        } catch (Exception e) {
            profilePic.setImageResource(R.mipmap.icon_profile_empty);
        }

        TextView emailId = (TextView)findViewById(R.id.email);
        emailId.setText(email);
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("standpoint pr112");
//        detachDatabaseReadListener();

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}