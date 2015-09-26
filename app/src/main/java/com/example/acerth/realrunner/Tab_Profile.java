package com.example.acerth.realrunner;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.acerth.helper.SQLiteHandler;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Admin PC on 8/9/2558.
 */
public class Tab_Profile extends AppCompatActivity {

    private ImageView mImageViewAddFriend;
    private ImageView mImageViewCalculate;
    private ImageView mImageViewMemoryBook;
    private ImageView mImageViewProfile;
    private ImageView mImageViewEditName;
    private int SELECT_IMAGE = 2;

    private SQLiteHandler db;

    private TextView nameField;
    private TextView caloriesField;
    private TextView distanceField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_profile);
        bindWidget();
        setWidgetEventListener();

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear);
//        linearLayout.setBackgroundColor(Color.parseColor("#f62355"));

        nameField = (TextView) findViewById(R.id.name);
        caloriesField = (TextView) findViewById(R.id.cal);
        distanceField = (TextView) findViewById(R.id.dis);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        HashMap<String, String> user = db.getUserDetails();

        String user_game_name = user.get("user_game_name");
        String calories = user.get("calories");
        String distance = user.get("distance");

        String calStr = calories+"";
        String disStr = distance+"";
        String text = "";

        // Displaying the user details on the screen
        nameField.setText(user_game_name);
        caloriesField.setText(calories);
        distanceField.setText(distance);
    }

    private void setWidgetEventListener() {
        mImageViewAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Add_Friends.class));
            }
        });
        mImageViewCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Calculate.class));
            }
        });
        mImageViewMemoryBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Health_tips.class));
            }
        });
        mImageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(Tab_Profile.this);
                builder.setMessage("Do you want to change profile picture ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                        Toast.makeText(getApplicationContext(),
//                                "hello", Toast.LENGTH_SHORT).show();

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
        mImageViewEditName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(Tab_Profile.this);
                LayoutInflater inflater = getLayoutInflater();

                view = inflater.inflate(R.layout.activity_edit_name, null);
                builder.setView(view);

                final TextView old_name = (TextView) view.findViewById(R.id.old_name);
                final EditText new_name = (EditText) view.findViewById(R.id.new_name);

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Check username password
                        if (old_name.getText().equals("demo@example.com") &&
                                new_name.getText().equals("demo")) {
                            Toast.makeText(getApplicationContext(), "Edit Failed",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Edit success!!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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


    private void bindWidget() {
        mImageViewAddFriend = (ImageView) findViewById(R.id.add_friend);
        mImageViewCalculate = (ImageView) findViewById(R.id.calculate);
        mImageViewMemoryBook = (ImageView) findViewById(R.id.book);
        mImageViewProfile = (ImageView) findViewById(R.id.pic_profile);
        mImageViewEditName = (ImageView) findViewById(R.id.edit_name);
    }

}
