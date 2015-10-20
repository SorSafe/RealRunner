package com.example.acerth.realrunner;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.StringRequest;
import com.example.acerth.app.AppConfig;
import com.example.acerth.app.AppController;
import com.example.acerth.helper.SQLiteHandler;
import com.example.acerth.helper.User;
import com.example.acerth.helper.CustomListAdapter;

import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.drive.internal.ad;

/**
 * Created by Admin PC on 8/9/2558.
 */
public class Add_Friends extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String url = AppConfig.URL_SHOWUSER;
    private ProgressDialog pDialog;
    private List<User> userList = new ArrayList<User>();
    private ListView listView;
    private CustomListAdapter adapter;
    //private SearchView searchView;
    private EditText search;
    private Button addFriend;
    private User user;
    private JSONObject obj;
    private SQLiteHandler db;
    private int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_add_friend);

        db = new SQLiteHandler(getApplicationContext());

        HashMap<String, String> user = db.getUserDetails();
        user_id = Integer.parseInt(user.get("user_id"));

        listView = (ListView) findViewById(R.id.listUser);
        search = (EditText) findViewById(R.id.search);
        addFriend = (Button) findViewById(R.id.addfriend);

        adapter = new CustomListAdapter(this,userList);
        listView.setAdapter(adapter);

        search = (EditText)findViewById(R.id.search);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence cs, int start, int before, int count) {
                // System.out.println("Text ["+cs+"]");
                Add_Friends.this.adapter.getFilter().filter(cs);

            }
        });

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        // Creating volley request obj
        JsonArrayRequest userReq = new JsonArrayRequest(url+"?user_id="+user_id,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                obj = response.getJSONObject(i);
                                final User user = new User();
                                user.setUser_id(obj.getInt("user_id"));
                                user.setThumbnailUrl(obj.getString("user_image_name"));
                                user.setUser_game_name(obj.getString("user_game_name"));

                                // adding movie to movies array
                                userList.add(user);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();

            }
        });

        AppController.getInstance().addToRequestQueue(userReq);
    }


    /*private void setupSearchView() {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(onQueryTextListener());
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
        searchView.setSearchableInfo(searchableInfo);
    }*/

    public void clickAddMethod(View v){
        int position = listView.getPositionForView(v);
        User u = userList.get(position);
        int friend_id = u.getUser_id();
        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();
        user_id = Integer.parseInt(user.get("user_id"));

        addMyFriend(friend_id,user_id);
    }

    public void addMyFriend(int friend_id, int user_id){
        String tag_string_req = "req_addFriend";

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_ADD_FRIEND+"?friend_id="+friend_id+"&user_id="+user_id, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hidePDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {

                        String msg = jObj.getString("msg");
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

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
                    hidePDialog();

                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(getApplicationContext(), "AuthFailureError.......", Toast.LENGTH_LONG).show();
                    hidePDialog();

                } else if (error instanceof ServerError) {
                    Toast.makeText(getApplicationContext(), "ServerError.......", Toast.LENGTH_LONG).show();
                    hidePDialog();

                } else if (error instanceof NetworkError) {
                    Toast.makeText(getApplicationContext(), "NetworkError.......", Toast.LENGTH_LONG).show();
                    hidePDialog();

                } else if (error instanceof ParseError) {
                    Toast.makeText(getApplicationContext(), "ParseError.......", Toast.LENGTH_LONG).show();
                    hidePDialog();

                }else if (error instanceof TimeoutError) {
                    Toast.makeText(getApplicationContext(), "TimeoutError.......", Toast.LENGTH_LONG).show();
                    hidePDialog();

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

    @Override
    public void onDestroy(){
        super.onDestroy();
        hidePDialog();
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
}
