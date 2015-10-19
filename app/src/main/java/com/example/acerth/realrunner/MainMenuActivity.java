package com.example.acerth.realrunner;

import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.Toast;

import com.example.acerth.app.AppConfig;
import com.example.acerth.helper.SQLiteHandler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.HashMap;

public class MainMenuActivity extends AppCompatActivity {

    private LinearLayout lProfile;
    LocalActivityManager mLocalActivityManager;
    //    ImageView mImageView;
    private int SELECT_IMAGE = 2;
    TabHost tabHost;

    private SQLiteHandler db;
    private String user_id;
    private String user_image_name;
    private ImageView profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        mLocalActivityManager = new LocalActivityManager(this, false);
        mLocalActivityManager.dispatchCreate(savedInstanceState);

        profile = (ImageView)findViewById(R.id.pic_profile);

        db = new SQLiteHandler(getApplicationContext());

        HashMap<String, String> user = db.getUserDetails();
        // Displaying the user details on the screen
        user_id = user.get("user_id");
        user_image_name = user.get("user_image_name");

        tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup(mLocalActivityManager);

        TabHost.TabSpec tabSpec1 = tabHost.newTabSpec("tab1")
                .setIndicator("Profile")
                .setContent(new Intent(this, Tab_Profile.class));
        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec("tab2")
                .setIndicator("Ranking")
                .setContent(new Intent(this, Tab_Ranking.class));
        TabHost.TabSpec tabSpec3 = tabHost.newTabSpec("tab3")
                .setIndicator("History")
                .setContent(new Intent(this, HistoryOfUser_Page.class));
        TabHost.TabSpec tabSpec4 = tabHost.newTabSpec("tab4")
                .setIndicator("Health Tips")
                .setContent(new Intent(this, Health_tips.class));

        tabHost.addTab(tabSpec1);
        tabHost.addTab(tabSpec2);
        tabHost.addTab(tabSpec3);
        tabHost.addTab(tabSpec4);


    }

//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Log.d("mainmenujaaaaaaaaaaaa", "onactivity called");
//    }



//    @Override
//    protected void onPause() {
//        super.onPause();
//        mLocalActivityManager.dispatchPause(!isFinishing());
//
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mLocalActivityManager.dispatchResume();
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
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
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        ImageView mImageViewProfile = (ImageView)tabHost.getCurrentView().findViewById(R.id.pic_profile);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE) {
            if (resultCode == this.RESULT_OK) {
                if (data != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                        mImageViewProfile.setImageBitmap(bitmap);
                        Log.d("HTTP RESULT", "Hey222");
                        File imageFile = new File(getCacheDir(),user_id+".png");

                        FileOutputStream fos = new FileOutputStream(imageFile);

                        bitmap.compress(Bitmap.CompressFormat.PNG, 0, fos);
                        fos.flush();
                        fos.close();

                        UploadTask task = new UploadTask(MainMenuActivity.this, imageFile);
                        Log.d("URL CALL", AppConfig.URL_UPLOAD_IMAGE+"?user_id="+user_id);
                        task.execute(AppConfig.URL_UPLOAD_IMAGE + "?user_id=" + user_id);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else if (resultCode == this.RESULT_CANCELED) {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public class UploadTask extends AsyncTask<String, Void, String> {
        private Context mContext;
        private File mFile;

        public UploadTask(Context context, File file) {
            mContext = context;
            mFile = file;
        }

        @Override
        protected String doInBackground(String... urls) {
            return uploadFile(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {

            if(result.trim().startsWith("<html>")){
                Toast.makeText(MainMenuActivity.this,"Upload Succes !",Toast.LENGTH_LONG).show();;
            }else {
                Toast.makeText(MainMenuActivity.this, "Upload Fail !", Toast.LENGTH_LONG).show();
            }

        }

        private String uploadFile(String strUrl) {

            String charset = Charset.defaultCharset().displayName();
            String boundary = Long.toHexString(System.currentTimeMillis());
            String strResult = "";

            try {
                URL url = new URL(strUrl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setDoOutput(true);
                con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                writeMultipart(boundary, charset, bos, false);
                byte[] extra = bos.toByteArray();
                int contentLength = extra.length;
                contentLength += mFile.length();

                con.setFixedLengthStreamingMode(contentLength);

                OutputStream out = con.getOutputStream();
                writeMultipart(boundary, charset, out, true);

                strResult = readStream(con.getInputStream());
                Log.d("HTTP RESULT",strResult);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return strResult;
        }

        private void writeMultipart(String boundary, String charset, OutputStream output, boolean writeContent) throws  IOException {
            BufferedWriter writer = null;

            try {
                writer = new BufferedWriter(new OutputStreamWriter(output, Charset.forName(charset)), 8192);
                writer.write("--" + boundary);
                writer.write("\r\n");
                writer.write("Content-Disposition: form-data; "
                        + "name=\"filUpload\"; "
                        + "filename=\"" + mFile.getName() + "\"");
                writer.write("\r\n");
                writer.write("Content-Type: " + URLConnection.guessContentTypeFromName(mFile.getName()));
                writer.write("\r\n");
                writer.write("\r\n");
                writer.flush();

                if (writeContent) {
                    FileInputStream fis = null;
                    try {
                        fis = new FileInputStream(mFile);
                        byte[] buffer = new byte[1024];
                        for (int len = 0; (len = fis.read(buffer)) > 0;) {
                            output.write(buffer, 0, len);
                        }
                        output.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (fis != null){
                            try {
                                fis.close();
                            } catch (IOException e) {
                            }
                        }
                    }
                }
                writer.write("\r\n");
                writer.flush();

                writer.write("--" + boundary + "--");
                writer.write("\r\n");
                writer.flush();
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
        }

        private String readStream(InputStream in) {
            BufferedReader reader = null;
            StringBuilder sb = new StringBuilder();

            try {
                reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return sb.toString();
        }
    }


}

