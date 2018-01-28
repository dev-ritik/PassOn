package com.example.android.passon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SendFeedbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_feedback);

        final EditText feedback = (EditText) findViewById(R.id.feedback);
        Button feedbackButton=(Button) findViewById(R.id.feedback_button);
        final TextView thanks=(TextView)findViewById(R.id.thanks_feedback);

        feedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                feedback.setText("");
                thanks.setVisibility(View.VISIBLE);

            }
        });

    }
}
