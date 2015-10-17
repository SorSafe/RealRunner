package com.example.acerth.realrunner;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.acerth.app.AppConfig;
import com.example.acerth.app.AppController;
import com.example.acerth.helper.SQLiteHandler;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin PC on 8/9/2558.
 */
public class Tab_Profile extends AppCompatActivity {

    private static final String TAG = Register.class.getSimpleName();
    private ProgressDialog pDialog;

    private ImageView mImageViewAddFriend;
    private ImageView mImageViewCalculate;
    private ImageView mImageViewMemoryBook;
    private ImageView mImageViewProfile;
    private ImageView mImageViewEditName;
    private ImageView mImageViewMap;
    private int SELECT_IMAGE = 2;

    private SQLiteHandler db;

    private TextView nameField;
    private TextView caloriesField;
    private TextView distanceField;
    private String user_id;
    private String user_game_name;
    private String user_image_name;
    private String calories;
    private String distance;

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
        user_id = user.get("user_id");
        user_game_name = user.get("user_game_name");
        user_image_name = user.get("user_image_name");
        calories = user.get("calories");
        distance = user.get("distance");

        // Displaying the user details on the screen
        nameField.setText(user_game_name);
        caloriesField.setText(calories);
        distanceField.setText(distance);

        Picasso.with(this).load(user_image_name).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).into(mImageViewProfile);

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
//                AlertDialog.Builder builder =
//                        new AlertDialog.Builder(Tab_Profile.this);
//                builder.setMessage("Do you want to change profile picture ?");
//                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        Toast.makeText(getApplicationContext(),
//                                "hello", Toast.LENGTH_SHORT).show();

                ImageView pick = (ImageView) findViewById(R.id.pic_profile);
                pick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);//
                        getParent().startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE);
                    }
                });
//                builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//                builder.show();
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

                old_name.setText(user_game_name);

                final String old_Name = old_name.getText().toString();
                final String new_Name = new_name.getText().toString();

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Check username password
                        //if (old_name.getText().equals("demo@example.com") &&
                        //       new_name.getText().equals("demo")) {
                        //   Toast.makeText(getApplicationContext(), "Edit Failed",
                        //          Toast.LENGTH_SHORT).show();
                        //  } else {
                        checkUpdate(old_Name, new_Name);
                        nameField.setText(new_Name);
                        // }
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

        mImageViewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Tab_Map.class));
            }
        });

    }

    public void checkUpdate(final String old_Name,final String new_Name){
        // Tag used to cancel the request
        String tag_string_req = "req_update_name";

        pDialog.setMessage("Updating your name ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_UPDATE_NAME, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Update Name Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user is found
                        JSONObject user = jObj.getJSONObject("user");

                        int user_id = user.getInt("user_id");
                        String user_old_game_name = user.getString("user_old_game_name");
                        String user_new_game_name = user.getString("user_new_game_name");

                        // Inserting row in users table
                        db.updateGamename(user_id,user_old_game_name,user_new_game_name);

                        String msg = jObj.getString("msg");
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_game_name", new_Name);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == SELECT_IMAGE) {
//            if (resultCode == this.RESULT_OK) {
//                if (data != null) {
//                    try {
//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
//                        mImageViewProfile.setImageBitmap(bitmap);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                } else if (resultCode == this.RESULT_CANCELED) {
//                    Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//    }


    private void bindWidget() {
        mImageViewAddFriend = (ImageView) findViewById(R.id.add_friend);
        mImageViewCalculate = (ImageView) findViewById(R.id.calculate);
        mImageViewMemoryBook = (ImageView) findViewById(R.id.book);
        mImageViewProfile = (ImageView) findViewById(R.id.pic_profile);
        mImageViewEditName = (ImageView) findViewById(R.id.edit_name);
        mImageViewMap = (ImageView) findViewById(R.id.map);
    }

}
