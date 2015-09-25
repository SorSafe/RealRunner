package com.example.acerth.realrunner;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by Admin PC on 8/9/2558.
 */
public class Tab_Profile extends AppCompatActivity {

    private ImageView mImageViewAddFriend;
    private ImageView mImageViewCalculate;
    private ImageView mImageViewSetting;
    private ImageView mImageViewProfile;
    private int SELECT_IMAGE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_profile);


        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear);
//        linearLayout.setBackgroundColor(Color.parseColor("#f62355"));

        mImageViewAddFriend = (ImageView) findViewById(R.id.add_friend);
        mImageViewAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Add_Friends.class));
            }
        });

        mImageViewCalculate = (ImageView) findViewById(R.id.calculate);
        mImageViewCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Calculate.class));
            }
        });

        mImageViewSetting = (ImageView) findViewById(R.id.setting);
        mImageViewSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Setting.class));
            }
        });

        mImageViewProfile = (ImageView) findViewById(R.id.pic_profile);
        mImageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(Tab_Profile.this);
                builder.setMessage("Do you want to change profile picture ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                        Toast.makeText(getApplicationContext(),
//                                "ขอบคุณครับ", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);//
                        getParent().startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE);

                    }
                });
                builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE) {
            if (resultCode == this.RESULT_OK) {
                if (data != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                        mImageViewProfile.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else if (resultCode == this.RESULT_CANCELED) {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
