package com.example.acerth.realrunner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.acerth.app.AppConfig;
import com.example.acerth.app.AppController;
import com.example.acerth.helper.CustomListUserAdapterRanking;
import com.example.acerth.helper.UserRanking;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Tab_Ranking_Overall_Global extends Activity{
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String url = AppConfig.URL_SHOW_RANKING;
    private ProgressDialog pDialog;
    private List<UserRanking> userList = new ArrayList<UserRanking>();
    private ListView listView;
    private CustomListUserAdapterRanking adapter;
    //private SearchView searchView;
    private UserRanking userRank;
    private JSONObject obj;
    private int numrow;
    private ViewGroup headerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ranking_ovarall_global);

        listView = (ListView) findViewById(R.id.listUserRank);
        headerView = (ViewGroup)getLayoutInflater().inflate(R.layout.header, listView, false);
        listView.addHeaderView(headerView);
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
                                numrow = i+1;
                                obj = response.getJSONObject(i);
                                userRank = new UserRanking();
                                userRank.setNum_row(numrow);
                                userRank.setUser_image_name(obj.getString("user_image_name"));
                                userRank.setUser_game_name(obj.getString("user_game_name"));
                                userRank.setSumDistance(obj.getDouble("SUM_DISTANCE"));

                                double sumDis = userRank.getSumDistance();
                                // adding movie to movies array
                                userList.add(userRank);
                                if(sumDis==0.0){
                                    numrow = i;
                                }

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

    public void goBack(View v){
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

