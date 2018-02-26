package com.example.android.passon;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TestActivity extends AppCompatActivity {
    FrameLayout l1;
    TextView t1;
    private boolean ref = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        l1 = (FrameLayout) findViewById(R.id.layout1);
        LinearLayout b1 = (LinearLayout) findViewById(R.id.buttonLayout);

        t1 = (TextView) findViewById(R.id.text1);
        TextView t2 = (TextView) findViewById(R.id.text2);
        Button button = (Button) findViewById(R.id.button);
        Button button2 = (Button) findViewById(R.id.button2);
        Button button3 = (Button) findViewById(R.id.button3);

        // Use afterDescendants to keep ListView from getting focus
//        if (!l1.isFocused()) {
//            // Use beforeDescendants so that previous selections don't re-take focus
//
//        }

        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("point ta40", "t1 clicked");

            }
        });
        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("point ta47", "t2 clicked");

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("point ta54", "after clicked");
//                l1.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
                ref = true;
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("point ta62", "before clicked");
//                l1.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
//                l1.requestFocus();
                ref = false;
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("point ta62", "before clicked");
//                l1.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
//                l1.requestFocus();
            }
        });
        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("point ta69", "frame clicked");
            }
        });

//        l1.onInterceptTouchEvent()


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {


//        return false;
//        Log.i("point ta97", super.dispatchTouchEvent(ev) + "");
        Log.i("point ta98 ref", ref + "");

        return super.dispatchTouchEvent(ev);
//        return ref;
    }
    private class Container extends FrameLayout {

        public Container(Context context) {
            super(context);
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            Log.i("point ta115", "onInterceptTouchEvent");
            int action = ev.getActionMasked();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    Log.i("point ta119", "onInterceptTouchEvent.ACTION_DOWN");
                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.i("point ta122", "onInterceptTouchEvent.ACTION_MOVE");
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    Log.i("point ta126", "onInterceptTouchEvent.ACTION_UP");
                    break;
            }
            return super.onInterceptTouchEvent(ev);
        }
    }
}

