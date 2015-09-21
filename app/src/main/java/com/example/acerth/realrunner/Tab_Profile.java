package com.example.acerth.realrunner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Admin PC on 8/9/2558.
 */
public class Tab_Profile extends Activity {

    private ImageView mImageViewAddFriend;
    private ImageView mImageViewCalculate;
    private ImageView mImageViewSetting;
    private ImageView mImageViewProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_profile);
        bindWidget();
        setWidgetEventListener();

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear);
//        linearLayout.setBackgroundColor(Color.parseColor("#f62355"));
    }

    private void setWidgetEventListener() {
        mImageViewAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Add_Friends.class));
            }
        });
        mImageViewCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Calculate.class));
            }
        });
        mImageViewSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Setting.class));
            }
        });

    }

    private void bindWidget() {
        mImageViewAddFriend = (ImageView)findViewById(R.id.add_friend);
        mImageViewCalculate = (ImageView)findViewById(R.id.calculate);
        mImageViewSetting = (ImageView)findViewById(R.id.setting);
        mImageViewProfile = (ImageView)findViewById(R.id.pic_profile);


    }

}
