package com.example.acerth.realrunner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.acerth.app.AppConfig;
import com.example.acerth.app.AppController;
import com.example.acerth.helper.CustomListAdapterHistoryOfUser;
import com.example.acerth.helper.HistoryOfUser;
import com.example.acerth.helper.SQLiteHandler;
import com.yalantis.phoenix.PullToRefreshView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Admin PC on 18/10/2558.
 */
public class HistoryOfUser_Page extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String url = AppConfig.URL_SHOW_HISTORY;
    private ProgressDialog pDialog;
    private List<HistoryOfUser> hisList = new ArrayList<HistoryOfUser>();
    private ListView listView;
    private CustomListAdapterHistoryOfUser adapter;
    //private SearchView searchView;
    private HistoryOfUser his;
    private JSONObject obj;
    private SQLiteHandler db;
    private int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_history);

        //       LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear);
//        linearLayout.setBackgroundColor(Color.parseColor("#3982d9"));

        db = new SQLiteHandler(getApplicationContext());

        HashMap<String, String> user = db.getUserDetails();
        user_id = Integer.parseInt(user.get("user_id"));

        listView = (ListView) findViewById(R.id.listHistory);
        adapter = new CustomListAdapterHistoryOfUser(this, hisList);
        listView.setAdapter(adapter);

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
                                his = new HistoryOfUser();
                                his.setDistance(Float.parseFloat(obj.getString("distance")));
                                his.setCalories(obj.getInt("calories"));
                                his.setStep(obj.getInt("step"));
                                his.setElapsedTime(obj.getString("elapsedTime"));
                                his.setTime_start(obj.getString("time_start"));
                                his.setTime_stop(obj.getString("time_stop"));

                                // adding movie to movies array
                                hisList.add(his);

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

        final PullToRefreshView mPullToRefreshView = (PullToRefreshView) findViewById(R.id.refresh_layout);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("hiii","Do in pull request ");
                JsonArrayRequest userReq = new JsonArrayRequest(url+"?user_id="+user_id,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                Log.d("hiii","dddd "+response);
                                // Parsing json
                                for (int i = 0; i < response.length(); i++) {
                                    try {

                                        obj = response.getJSONObject(i);
                                        his = new HistoryOfUser();
                                        his.setDistance(Float.parseFloat(obj.getString("distance")));
                                        his.setCalories(obj.getInt("calories"));
                                        his.setStep(obj.getInt("step"));
                                        his.setElapsedTime(obj.getString("elapsedTime"));
                                        his.setTime_start(obj.getString("time_start"));
                                        his.setTime_stop(obj.getString("time_stop"));

                                        // adding movie to movies array
                                        hisList.add(his);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                                // notifying list adapter about data changes
                                // so that it renders the list view with updated data
                                adapter.notifyDataSetChanged();
                                mPullToRefreshView.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mPullToRefreshView.setRefreshing(false);
                                    }
                                }, 1000);

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //VolleyLog.d(TAG, "Error: " + error.getMessage());
                        Log.d("hiii","On Error Request");
                        mPullToRefreshView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mPullToRefreshView.setRefreshing(false);
                            }
                        }, 1000);

                    }
                });
                AppController.getInstance().addToRequestQueue(userReq);

            }
        });
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
