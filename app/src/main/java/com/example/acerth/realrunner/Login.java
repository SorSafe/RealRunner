package com.example.acerth.realrunner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.acerth.app.AppConfig;
import com.example.acerth.app.AppController;
import com.example.acerth.helper.SQLiteHandler;
import com.example.acerth.helper.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends Activity {
    private static final String TAG = Register.class.getSimpleName();
    private EditText inputUsername;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    private Button btnLogin;
    private ImageView mImageViewForgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        blindWidget();
//        setWidgetEventListener();

        inputUsername = (EditText) findViewById(R.id.usernameLog);
        inputPassword = (EditText) findViewById(R.id.passLog);
        btnLogin = (Button) findViewById(R.id.bLogin);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(Login.this, Welcome.class);
            startActivity(intent);
            finish();
        }

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String user_name = inputUsername.getText().toString();
                String user_password = inputPassword.getText().toString();

                // Check for empty data in the form
                if (user_name.isEmpty() && user_password.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Please enter username and password !!!!", Toast.LENGTH_LONG).show();
                }
                else if(user_name.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Please enter username !!!", Toast.LENGTH_LONG).show();
                }else if(user_password.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Please enter password !!!", Toast.LENGTH_LONG).show();
                }else {
                    checkLogin(user_name, user_password);
                    inputUsername.setText("");
                    inputPassword.setText("");
                }
            }

        });
    }

    private void checkLogin(final String user_name, final String user_password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in

                        // Create login session
                        session.setLogin(true);

                        JSONObject user = jObj.getJSONObject("user");

                        int user_id = user.getInt("user_id");
                        String user_name = user.getString("user_name");
                        String user_password = user.getString("user_password");
                        String user_email = user.getString("user_email");
                        String user_game_name = user.getString("user_game_name");
                        String user_image_name = user.getString("user_image_name");
                        String user_image_path = user.getString("user_image_path");
                        String level = user.getString("level");
                        String league = user.getString("league");
                        double score = user.getDouble("score");
                        double distance = user.getDouble("distance");
                        double calories = user.getDouble("calories");
                        String admin_id = user.getString("admin_id");

                        // Inserting row in users table
                        db.addUser(user_id, user_name, user_password, user_email, user_game_name, user_image_name, user_image_path,
                                level, league, score, distance, calories, admin_id);


                        String msg = jObj.getString("msg");
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        // Launch main activity
                        Intent intent = new Intent(Login.this, Welcome.class);
                        startActivity(intent);
                        finish();
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
                if (error instanceof NoConnectionError) {
                    Toast.makeText(getApplicationContext(), "NoConnectionError.......", Toast.LENGTH_LONG).show();
                    hideDialog();

                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(getApplicationContext(), "AuthFailureError.......", Toast.LENGTH_LONG).show();
                    hideDialog();

                } else if (error instanceof ServerError) {
                    Toast.makeText(getApplicationContext(), "ServerError.......", Toast.LENGTH_LONG).show();
                    hideDialog();

                } else if (error instanceof NetworkError) {
                    Toast.makeText(getApplicationContext(), "NetworkError.......", Toast.LENGTH_LONG).show();
                    hideDialog();

                } else if (error instanceof ParseError) {
                    Toast.makeText(getApplicationContext(), "ParseError.......", Toast.LENGTH_LONG).show();
                    hideDialog();

                }else if (error instanceof TimeoutError) {
                    Toast.makeText(getApplicationContext(), "TimeoutError.......", Toast.LENGTH_LONG).show();
                    hideDialog();

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
                params.put("user_name", user_name);
                params.put("user_password", user_password);

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

//    private void setWidgetEventListener() {
//        mImageViewForgot.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(),ForgotPassword.class));
//            }
//        });
//    }


//    private void blindWidget() {
//        mImageViewForgot = (ImageView)findViewById(R.id.forgot);
//    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/


}
