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
import com.example.acerth.helper.CustomListUserAdapterRanking;
import com.example.acerth.helper.User;
import com.example.acerth.helper.CustomListAdapter;

import android.app.Activity;
import android.os.Bundle;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.util.Log;

import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

public class Tab_Ranking extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String url = AppConfig.URL_SHOW_RANKING;
    private ProgressDialog pDialog;
    private List<User> userList = new ArrayList<User>();
    private ListView listView;
    private CustomListUserAdapterRanking adapter;
    //private SearchView searchView;
    private User user;
    private JSONObject obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ranking);

 //       LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear);
//        linearLayout.setBackgroundColor(Color.parseColor("#3982d9"));

        listView = (ListView) findViewById(R.id.listUserRank);
        adapter = new CustomListUserAdapterRanking(this, userList);
        listView.setAdapter(adapter);

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        // Creating volley request obj
        JsonArrayRequest userReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                obj = response.getJSONObject(i);
                                user = new User();
                                user.setThumbnailUrl(obj.getString("user_image_name"));
                                user.setUser_game_name(obj.getString("user_game_name"));
                                user.setUser_score(obj.getDouble("score"));

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
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(userReq);
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
