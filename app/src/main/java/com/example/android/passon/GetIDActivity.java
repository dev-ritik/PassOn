package com.example.android.passon;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GetIDActivity extends AppCompatActivity {

    EditText mobNo;
    Button galleryIntent, cameraIntent;
    private static final int CHOOSE_CAMERA_RESULT1 = 1;
    private static final int GALLERY_RESULT2 = 2;
    Uri tempuri;
    public static File file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_id);

        mobNo=(EditText)findViewById(R.id.mobile_no);
        galleryIntent=(Button)findViewById(R.id.gallery_intent1);
        cameraIntent=(Button)findViewById(R.id.camera_intent1);

        cameraIntent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(mobNo.getText().toString().equals(""))) {
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                    file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "IMG_" + timeStamp + ".jpg");
                    tempuri = Uri.fromFile(file);
                    i.putExtra(MediaStore.EXTRA_OUTPUT, tempuri);
                    i.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                    startActivityForResult(i, CHOOSE_CAMERA_RESULT1);
                }else{
                    Toast.makeText(GetIDActivity.this,"Please update mobile number first",Toast.LENGTH_SHORT).show();
                }
            }
        });

        galleryIntent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(mobNo.getText().toString().equals(""))) {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/jpeg");
                    startActivityForResult(intent, GALLERY_RESULT2);
                }else{
                    Toast.makeText(GetIDActivity.this,"Please update mobile number first",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (null == imageReturnedIntent) return;
        Uri originalUri = null;
        switch(requestCode) {
            case CHOOSE_CAMERA_RESULT1:
                if(resultCode == RESULT_OK){
                    if(file.exists()){
                        Toast.makeText(this,"The image was saved at "+file.getAbsolutePath(),Toast.LENGTH_LONG).show();;
                    }
                    //Uri is at variable tempuri which can be converted to string

                    // code for inserting in database
                }

                break;
            case GALLERY_RESULT2:
                if(resultCode == RESULT_OK){
                    originalUri = imageReturnedIntent.getData();
                    final int takeFlags = imageReturnedIntent.getFlags()
                            & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                            | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    getContentResolver().takePersistableUriPermission(originalUri, takeFlags);

                    //Uri is originalUri which can be converted to string

                    // code for inserting in database
                }
                break;
        }

    }
}
