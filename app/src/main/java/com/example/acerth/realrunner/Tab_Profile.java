package com.example.acerth.realrunner;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.acerth.app.AppConfig;
import com.example.acerth.app.AppController;
import com.example.acerth.helper.SQLiteHandler;
import com.example.acerth.helper.SessionManager;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

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
    private ImageView mImageViewStart;
    private ImageView mImageViewProfile;
    private ImageView mImageViewEditName;
    private ImageView mImageViewMap;
    private int SELECT_IMAGE = 2;

    private SQLiteHandler db;

    private TextView nameField;
    private TextView caloriesField;
    private TextView distanceField;
    private TextView idField;
    private String user_id;
    private String user_game_name;
    private String user_image_name;
    private String caloriesStr;
    private String distanceStr;
    private SessionManager session;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_profile);
        bindWidget();
        setWidgetEventListener();

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear);
//        linearLayout.setBackgroundColor(Color.parseColor("#f62355"));

        idField = (TextView) findViewById(R.id.u_id);
        nameField = (TextView) findViewById(R.id.name);
        caloriesField = (TextView) findViewById(R.id.cal);
        distanceField = (TextView) findViewById(R.id.dis);
        btnLogout = (Button) findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        HashMap<String, String> user = db.getUserDetails();
        user_id = user.get("user_id");
        user_game_name = user.get("user_game_name");
        user_image_name = user.get("user_image_name");

        showSumCaloriesAndDistance(user_id);

        // Displaying the user details on the screen
        idField.setText(user_id);
        nameField.setText(user_game_name);

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

        mImageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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



                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String user_old_game_name = old_name.getText().toString();
                        String user_new_game_name = new_name.getText().toString();

                        Log.d("PreonClickkkkkkkkk : ", user_id + " : " + user_old_game_name + " : " + user_new_game_name);
                        checkUpdate(user_id, user_old_game_name, user_new_game_name);
                        Log.d("onClickkkkkkkkk : ", user_id + " : " + user_old_game_name + " : " + user_new_game_name);
                        nameField.setText(user_new_game_name);

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

    public void checkUpdate(final String user_id, final String user_old_game_name, final String user_new_game_name){
        // Tag used to cancel the request
        final String tag_string_req = "req_update_name";
        Log.d("Check in Update: ", user_id + user_old_game_name + user_new_game_name);
        //pDialog.setMessage("Updating your name ...");
        //showDialog();

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_UPDATE_NAME+"?user_id="+user_id+"&user_old_game_name="+user_old_game_name+"&user_new_game_name="+user_new_game_name, new Response.Listener<String>()
        {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Update Name Response: " + response.toString());
                Log.d("Check in Update: ", user_id + user_old_game_name + user_new_game_name);
                //hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user is found
                        JSONObject user = jObj.getJSONObject("user");

                        int user_id = user.getInt("user_id");
                        String oldName = user.getString("user_old_game_name");
                        String newName = user.getString("user_new_game_name");

                        // Inserting row in users table
                        db.updateGamename(user_id,oldName,newName);

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
                //hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_game_name",user_new_game_name);

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

    public void showSumCaloriesAndDistance(String user_id){
        String tag_string_req = "req_showSumCaloriesAndDistance";

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_SHOW_SUM_CALORIES_AND_DISTANCE+"?user_id="+user_id, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {

                        JSONObject userplaymap = jObj.getJSONObject("userplaymap");

                        double calories = userplaymap.getDouble("calories");
                        double distance = userplaymap.getDouble("distance");

                        distanceStr = distance+"";
                        caloriesStr = calories+"";

                        caloriesField.setText(caloriesStr);
                        distanceField.setText(distanceStr);


                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: You have added this friend Already !!!", Toast.LENGTH_LONG).show();
                }
            }

    }, new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {
            if (error instanceof NoConnectionError) {
                Toast.makeText(getApplicationContext(), "NoConnectionError.......", Toast.LENGTH_LONG).show();

            } else if (error instanceof AuthFailureError) {
                Toast.makeText(getApplicationContext(), "AuthFailureError.......", Toast.LENGTH_LONG).show();

            } else if (error instanceof ServerError) {
                Toast.makeText(getApplicationContext(), "ServerError.......", Toast.LENGTH_LONG).show();

            } else if (error instanceof NetworkError) {
                Toast.makeText(getApplicationContext(), "NetworkError.......", Toast.LENGTH_LONG).show();

            } else if (error instanceof ParseError) {
                Toast.makeText(getApplicationContext(), "ParseError.......", Toast.LENGTH_LONG).show();

            }else if (error instanceof TimeoutError) {
                Toast.makeText(getApplicationContext(), "TimeoutError.......", Toast.LENGTH_LONG).show();
            }
            //Log.e(TAG, "Login Error: " + error.getMessage());
            //Toast.makeText(getApplicationContext(),
            //        error.getMessage(), Toast.LENGTH_LONG).show();
            //hideDialog();
        }
    }) {

        @Override
        protected Map<String, String> getParams() {
            // Posting parameters to login url
            Map<String, String> params = new HashMap<String, String>();
            // params.put("friend_id", friend_id);

            return params;
        }

    };

    // Adding request to request queue
    AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
}



    private void bindWidget() {
        mImageViewAddFriend = (ImageView) findViewById(R.id.add_friend);
        mImageViewCalculate = (ImageView) findViewById(R.id.calculate);
        mImageViewProfile = (ImageView) findViewById(R.id.pic_profile);
        mImageViewEditName = (ImageView) findViewById(R.id.edit_name);
        mImageViewMap = (ImageView) findViewById(R.id.map);
    }

    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(Tab_Profile.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
