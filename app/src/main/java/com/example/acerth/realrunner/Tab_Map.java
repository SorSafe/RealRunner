package com.example.acerth.realrunner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.pedometer.source.PedometerSettings;
import com.android.pedometer.source.StepService;
import com.android.pedometer.source.StopwatchService;
import com.android.pedometer.source.Utils;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.acerth.app.AppConfig;
import com.example.acerth.app.AppController;
import com.example.acerth.helper.SQLiteHandler;
import com.example.acerth.helper.SessionManager;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SuppressLint("HandlerLeak")
public class Tab_Map extends Activity {

    private static final String TAG = "Pedometer";
    private SharedPreferences mSettings;
    private PedometerSettings mPedometerSettings;
    private Utils mUtils;
    ImageView mImageView;

    private TextView m_elapsedTime;
    private TextView mStepValueView;
    private TextView mPaceValueView;
    private TextView mDistanceValueView;
    private TextView mSpeedValueView;
    private TextView mCaloriesValueView;
    private TextView txtStatus;
    private ImageView button_start;
    private ImageView button_pause;
    private ImageView button_stop;
    TextView mDesiredPaceView;
    private int mStepValue;
    private int mPaceValue;
    private float mDistanceValue;
    private float mSpeedValue;
    private int mCaloriesValue;
    private float mDesiredPaceOrSpeed;
    private int mMaintain;
    private boolean mIsMetric;
    private float mMaintainInc;
    private boolean mQuitting = false; // Set when user selected Quit from menu,
    // can be used by onPause, onStop,
    // onDestroy

    // ---------------Stop Timer ------------
    // Timer to update the elapsedTime display
    private final long mFrequency = 100; // milliseconds
    private final int TICK_WHAT = 2;
    // ---------------Stop Timer ------------
    private SessionManager session;
    private SQLiteHandler db;
    private int user_id ;
    private static final String url = AppConfig.URL_USERPLAYMAP;
    private ProgressDialog pDialog;
    private String time_start;
    private String time_stop;

    /**
     * True, when service is running.
     */
    private boolean mIsRunning;
    private String user_image_name;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "[ACTIVITY] onCreate");
        super.onCreate(savedInstanceState);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        HashMap<String, String> user = db.getUserDetails();
        user_id = Integer.parseInt(user.get("user_id"));
        user_image_name = user.get("user_image_name");

        mStepValue = 0;
        mPaceValue = 0;

        setContentView(R.layout.activity_distance);

        mUtils = Utils.getInstance();

        mImageView = (ImageView) findViewById(R.id.usermap);
        Picasso.with(this).load(user_image_name).into(mImageView);
    }

    @Override
    protected void onStart() {
        Log.i(TAG, "[ACTIVITY] onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "[ACTIVITY] onResume");
        super.onResume();

        mSettings = PreferenceManager.getDefaultSharedPreferences(this);
        mPedometerSettings = new PedometerSettings(mSettings);

        // Read from preferences if the service was running on the last onPause
        mIsRunning = mPedometerSettings.isServiceRunning();

        // Start the service if this is considered to be an application start
        // (last onPause was long ago)
        if (mIsRunning && mPedometerSettings.isNewStart()) {
            startStepService();
            bindStepService();
        } else if (!mIsRunning) {
            bindService(new Intent(this, StopwatchService.class),
                    m_stopwatchServiceConn, Context.BIND_AUTO_CREATE);
        }

        mPedometerSettings.clearServiceRunning();

        mStepValueView = (TextView) findViewById(R.id.step_value);
        mPaceValueView = (TextView) findViewById(R.id.pace_value);
        mDistanceValueView = (TextView) findViewById(R.id.distance_value);
        mSpeedValueView = (TextView) findViewById(R.id.speed_value);
        mCaloriesValueView = (TextView) findViewById(R.id.calories_value);
        mDesiredPaceView = (TextView) findViewById(R.id.desired_pace_value);
        m_elapsedTime = (TextView) findViewById(R.id.ElapsedTime);
        txtStatus = (TextView) findViewById(R.id.txtStatus);
        button_start = (ImageView) findViewById(R.id.button_start);
        button_pause = (ImageView) findViewById(R.id.button_pause);
        button_stop = (ImageView) findViewById(R.id.button_stop);
        mHandler.sendMessageDelayed(Message.obtain(mHandler, TICK_WHAT), mFrequency);


        mMaintain = mPedometerSettings.getMaintainOption();

        if (mMaintain == PedometerSettings.M_PACE) {
            mMaintainInc = 5f;
            mDesiredPaceOrSpeed = (float) mPedometerSettings.getDesiredPace();
        } else if (mMaintain == PedometerSettings.M_SPEED) {
            mDesiredPaceOrSpeed = mPedometerSettings.getDesiredSpeed();
            mMaintainInc = 0.1f;
        }

        button_start.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                startStepService();
                bindStepService();
                Calendar c = Calendar.getInstance();
                Date timestamp = new Date(c.get(Calendar.YEAR)-1900,c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH),c.get(Calendar.HOUR),c.get(Calendar.MINUTE),c.get(Calendar.SECOND));
                DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                time_start = dateFormatter.format(timestamp);
                System.out.println("Time Start "+time_start);
                m_stopwatchService.start();
            }
        });

        button_pause.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsRunning) {
                    Toast.makeText(getApplicationContext(), "Pause", Toast.LENGTH_SHORT).show();
                    unbindStepService();
                    stopStepService();
                    m_stopwatchService.pause();
                    txtStatus.setText("Pause");
                }
            }
        });

        button_stop.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, String> user = db.getUserDetails();
                user_id = Integer.parseInt(user.get("user_id"));
                float distance = mDistanceValue;
                int calories =mCaloriesValue;
                int step = mStepValue;
                String elapsedTime = m_elapsedTime.getText().toString();

                Calendar c = Calendar.getInstance();
                Date timestamp = new Date(c.get(Calendar.YEAR)-1900,c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH),c.get(Calendar.HOUR),c.get(Calendar.MINUTE),c.get(Calendar.SECOND));
                DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                time_stop = dateFormatter.format(timestamp);
                System.out.println("Time Stop " + time_stop);

                insertPlayData(user_id, distance, calories, step, elapsedTime, time_start, time_stop);
                Log.d("INSERT user id : ", String.valueOf(user_id));
                Log.d("INSERT distance : ", String.valueOf(distance));
                Log.d("INSERT calories  : ", String.valueOf(calories));
                Log.d("INSERT step : ", String.valueOf(step));
                Log.d("INSERT elapsedTime : ", elapsedTime);
                Log.d("INSERT time start : ", time_start);
                Log.d("INSERT time stop : ", time_stop);
                resetValues(true);
                unbindStepService();
                stopStepService();
                m_stopwatchService.reset();
                getData();
                txtStatus.setText("Stop");

            }
        });
        displayDesiredPaceOrSpeed();
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        resetValues(false);
        unbindStepService();
        stopStepService();
        mQuitting = true;
        finish();
        super.onBackPressed();
    }

    private void displayDesiredPaceOrSpeed() {
        if (mMaintain == PedometerSettings.M_PACE) {
            mDesiredPaceView.setText("" + (int) mDesiredPaceOrSpeed);
        } else {
            mDesiredPaceView.setText("" + mDesiredPaceOrSpeed);
        }
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "[ACTIVITY] onPause");
        if (mIsRunning) {
            unbindStepService();
        }
        if (mQuitting) {
            mPedometerSettings.saveServiceRunningWithNullTimestamp(mIsRunning);
        } else {
            mPedometerSettings.saveServiceRunningWithTimestamp(mIsRunning);
        }

        super.onPause();
        savePaceSetting();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "[ACTIVITY] onStop");
        super.onStop();
    }

    protected void onDestroy() {
        Log.i(TAG, "[ACTIVITY] onDestroy");

        super.onDestroy();
    }

    private void setDesiredPaceOrSpeed(float desiredPaceOrSpeed) {
        if (mService != null) {
            if (mMaintain == PedometerSettings.M_PACE) {
                mService.setDesiredPace((int) desiredPaceOrSpeed);
            } else if (mMaintain == PedometerSettings.M_SPEED) {
                mService.setDesiredSpeed(desiredPaceOrSpeed);
            }
        }
    }

    private void savePaceSetting() {
        mPedometerSettings.savePaceOrSpeedSetting(mMaintain,
                mDesiredPaceOrSpeed);
    }

    // Connection to the background StopwatchService
    private StopwatchService m_stopwatchService;
    private ServiceConnection m_stopwatchServiceConn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            m_stopwatchService = ((StopwatchService.LocalBinder) service)
                    .getService();
            // showCorrectButtons();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            m_stopwatchService = null;
        }

    };

    private StepService mService;

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            Tab_Map.this.mService = ((StepService.StepBinder) service).getService();
            Tab_Map.this.mService.registerCallback(mCallback);
            Tab_Map.this.mService.reloadSettings();
        }
        public void onServiceDisconnected(ComponentName className) {
            mService = null;
        }
    };

    private void startStepService() {
        if (!mIsRunning) {
            Toast.makeText(getApplicationContext(), "[SERVICE] Start",
                    Toast.LENGTH_SHORT).show();
            Log.i(TAG, "[SERVICE] Start");
            mIsRunning = true;
            startService(new Intent(Tab_Map.this, StepService.class));
            startService(new Intent(this, StopwatchService.class));
            // bindStopwatchService();
        }
    }

    private void bindStepService() {
        Log.i(TAG, "[SERVICE] Bind");
        Toast.makeText(getApplicationContext(), "[SERVICE] Bind",
                Toast.LENGTH_SHORT).show();
        bindService(new Intent(Tab_Map.this, StepService.class), mConnection,
                Context.BIND_AUTO_CREATE + Context.BIND_DEBUG_UNBIND);
        bindService(new Intent(this, StopwatchService.class),
                m_stopwatchServiceConn, Context.BIND_AUTO_CREATE);
    }

    private void unbindStepService() {
        if (mIsRunning) {
            Toast.makeText(getApplicationContext(), "[SERVICE] Unbind",
                    Toast.LENGTH_SHORT).show();
            Log.i(TAG, "[SERVICE] Unbind");
            if (m_stopwatchService != null) {
                unbindService(m_stopwatchServiceConn);
                unbindService(mConnection);
            }
        }
    }

    private void stopStepService() {
        Log.i(TAG, "[SERVICE] Stop");
        Toast.makeText(getApplicationContext(), "[SERVICE] Stop",
                Toast.LENGTH_SHORT).show();
        if (mService != null) {
            Log.i(TAG, "[SERVICE] stopService");
            stopService(new Intent(Tab_Map.this, StepService.class));
            getData();
        }
        mIsRunning = false;
    }

    private void resetValues(boolean updateDisplay) {
        if (mService != null && mIsRunning) {
            mService.resetValues();
        } else {
            mStepValueView.setText("0");
            mPaceValueView.setText("0");
            mDistanceValueView.setText("0.00 km");
            mSpeedValueView.setText("0");
            mCaloriesValueView.setText("0 kcal");
            SharedPreferences state = getSharedPreferences("state", 0);
            SharedPreferences.Editor stateEditor = state.edit();

            if (updateDisplay) {
                stateEditor.putInt("steps", 0);
                stateEditor.putInt("pace", 0);
                stateEditor.putFloat("distance", 0);
                stateEditor.putFloat("speed", 0);
                stateEditor.putFloat("calories", 0);
                stateEditor.commit();
            }
        }
    }

    // TODO: unite all into 1 type of message
    private StepService.ICallback mCallback = new StepService.ICallback() {
        public void stepsChanged(int value) {
            mHandler.sendMessage(mHandler.obtainMessage(STEPS_MSG, value, 0));
        }

        public void paceChanged(int value) {
            mHandler.sendMessage(mHandler.obtainMessage(PACE_MSG, value, 0));
        }

        public void distanceChanged(float value) {
            mHandler.sendMessage(mHandler.obtainMessage(DISTANCE_MSG,
                    (int) (value * 1000), 0));
        }

        public void speedChanged(float value) {
            mHandler.sendMessage(mHandler.obtainMessage(SPEED_MSG,
                    (int) (value * 1000), 0));
        }

        public void caloriesChanged(float value) {
            mHandler.sendMessage(mHandler.obtainMessage(CALORIES_MSG,
                    (int) (value), 0));
        }
    };

    private static final int STEPS_MSG = 1;
    private static final int PACE_MSG = 2;
    private static final int DISTANCE_MSG = 3;
    private static final int SPEED_MSG = 4;
    private static final int CALORIES_MSG = 5;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            updateElapsedTime();
            sendMessageDelayed(Message.obtain(this, TICK_WHAT), mFrequency);

            switch (msg.what) {
                case STEPS_MSG:
                    mStepValue = (int) msg.arg1;
                    mStepValueView.setText("" + mStepValue);
                    break;
                case PACE_MSG:
                    mPaceValue = msg.arg1;
                    if (mPaceValue <= 0) {
                        mPaceValueView.setText("0");
                    } else {
                        mPaceValueView.setText("" + (int) mPaceValue);
                    }
                    break;
                case DISTANCE_MSG:
                    mDistanceValue = ((int) msg.arg1) / 1000f;
                    if (mDistanceValue <= 0) {
                        mDistanceValueView.setText("0.00 km");
                    } else {
                        mDistanceValueView
                                .setText(("" + (mDistanceValue + 0.000001f))
                                        .substring(0, 4)+" km");
                        checkpoint();

                    }
                    break;
                case SPEED_MSG:
                    mSpeedValue = ((int) msg.arg1) / 1000f;
                    if (mSpeedValue <= 0) {
                        mSpeedValueView.setText("0");
                    } else {
                        mSpeedValueView.setText(("" + (mSpeedValue + 0.000001f))
                                .substring(0, 4));
                    }
                    break;
                case CALORIES_MSG:
                    mCaloriesValue = msg.arg1;
                    if (mCaloriesValue <= 0) {
                        mCaloriesValueView.setText("0 kcal");
                    } else {
                        mCaloriesValueView.setText("" + (int) mCaloriesValue+" kcal");
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    public void checkpoint() {
        LinearLayout.LayoutParams params;
        if (mStepValue >= 370) {
            mImageView = (ImageView) findViewById(R.id.usermap);
            mImageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            Picasso.with(this).load(user_image_name).into(mImageView);
            params = new LinearLayout.LayoutParams(190, 190);
            params.leftMargin = 87*2*3;
            params.topMargin = -20*2*3;
            mImageView.setLayoutParams(params);
        }
        else if (mStepValue >= 290) {
            mImageView = (ImageView) findViewById(R.id.usermap);
            mImageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            Picasso.with(this).load(user_image_name).into(mImageView);
            Log.d("mappp", "pic_profile");
            params = new LinearLayout.LayoutParams(190, 190);
            params.leftMargin = 70* 2 * 3;
            params.topMargin = 8* 2 * 3;
            mImageView.setLayoutParams(params);
        }
        else if (mStepValue >= 220) {
            mImageView = (ImageView) findViewById(R.id.usermap);
            mImageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            Picasso.with(this).load(user_image_name).into(mImageView);
            Log.d("mappp", "pic_profile");
            params = new LinearLayout.LayoutParams(190, 190);
            params.leftMargin = 40*2*3;
            params.topMargin = 40*2*3;
            mImageView.setLayoutParams(params);
        }
        else if (mStepValue >= 160) {
            mImageView = (ImageView) findViewById(R.id.usermap);
            mImageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            Picasso.with(this).load(user_image_name).into(mImageView);
            Log.d("mappp", "pic_profile");
            params = new LinearLayout.LayoutParams(190, 190);
            params.leftMargin = 1*2*3;
            params.topMargin = 80*2*3;
            mImageView.setLayoutParams(params);
        }
        else if (mStepValue >= 110) {
            mImageView = (ImageView) findViewById(R.id.usermap);
            mImageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            Picasso.with(this).load(user_image_name).into(mImageView);
            Log.d("mappp", "pic_profile");
            params = new LinearLayout.LayoutParams(190, 190);
            params.leftMargin = 117*2*3;
            params.topMargin = 100*2*3;
            mImageView.setLayoutParams(params);
        }
        else if (mStepValue >= 70) {
            mImageView = (ImageView) findViewById(R.id.usermap);
            mImageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            Picasso.with(this).load(user_image_name).into(mImageView);
            Log.d("mappp", "pic_profile");
            params = new LinearLayout.LayoutParams(190, 190);
            params.leftMargin = 125*2*3;
            params.topMargin = 130*2*3;
            mImageView.setLayoutParams(params);
        }
        else if (mStepValue >= 40) {
            mImageView = (ImageView) findViewById(R.id.usermap);
            mImageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            Picasso.with(this).load(user_image_name).into(mImageView);
            Log.d("mappp", "pic_profile");
            params = new LinearLayout.LayoutParams(190, 190);
            params.leftMargin = 90*2*3;
            params.topMargin = 150*2*3;
            mImageView.setLayoutParams(params);
        }
        else if (mStepValue >= 20) {
            mImageView = (ImageView) findViewById(R.id.usermap);
            mImageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            Picasso.with(this).load(user_image_name).into(mImageView);
            Log.d("mappp", "pic_profile");
            params = new LinearLayout.LayoutParams(190, 190);
            params.leftMargin = 98*2*3;
            params.topMargin = 180*2*3;
            mImageView.setLayoutParams(params);
            Log.d("point", "point 2 km!!!!!");
        } else {
            mImageView = (ImageView) findViewById(R.id.usermap);
            mImageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            Picasso.with(this).load(user_image_name).into(mImageView);
            Log.d("mappp", "pic_profile");
            params = new LinearLayout.LayoutParams(190, 190);
            params.leftMargin = 85*2*3;
            params.topMargin = 200*2*3;
            mImageView.setLayoutParams(params);
            Log.d("point", "pointPPPPPPPPPPPPPPPPPPPPPPPP");
        }
    }

    public void updateElapsedTime() {
        if (m_stopwatchService != null)
            m_elapsedTime.setText(m_stopwatchService.getFormattedElapsedTime());
    }

    private void getData() {
        String _step = mStepValueView.getText().toString().trim();
        String _pace = mPaceValueView.getText().toString().trim();
        String _distance = mDistanceValueView.getText().toString().trim();
        String _speed = mSpeedValueView.getText().toString().trim();
        String _calories = mCaloriesValueView.getText().toString().trim();
        String _time = m_elapsedTime.getText().toString().trim();
    }

    private void insertPlayData(final int user_id, final float distance, final int calories, final int step, final String elapsedTime, final String time_start, final  String time_stop) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_USERPLAYMAP, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Insert Data Response: " + response.toString());
//                hideDialog();

               /* try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        JSONObject user = jObj.getJSONObject("user");

                        int user_id = user.getInt("user_id");
                        float distance = Float.parseFloat(user.getString("distance"));
                        int calories = user.getInt("calories");
                        int step = user.getInt("step");
                        String elapsedTime = user.getString("elapsedTime");
                        String time_start = user.getString("time_start");
                        String time_stop = user.getString("time_stop");

                        // Inserting row in users table
                        db.addplayData(user_id, distance, calories, step, elapsedTime,time_start,time_stop);

                        String msg = jObj.getString("msg");
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }*/
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Insert Data Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
//                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", String.valueOf(user_id));
                params.put("distance", String.valueOf(distance));
                params.put("calories", String.valueOf(calories));
                params.put("step", String.valueOf(step));
                params.put("elapsedTime", elapsedTime);
                params.put("time_start", time_start);
                params.put("time_stop", time_stop);

                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    /*private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }*/

}
