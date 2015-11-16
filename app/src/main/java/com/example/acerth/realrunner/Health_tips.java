package com.example.acerth.realrunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.acerth.app.AppConfig;
import com.example.acerth.app.AppController;
import com.example.acerth.helper.CustomListUserAdapterRanking;
import com.example.acerth.helper.HealthListAdapter;
import com.example.acerth.helper.HelperHealth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Health_tips extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String url = AppConfig.URL_GETHEALTHTIPSDATA;
    private ProgressDialog pDialog;
    private List<HelperHealth> healthList = new ArrayList<HelperHealth>();
    private ListView listView;
    private HealthListAdapter adapter;
    //private SearchView searchView;
    private HelperHealth health;
    private JSONObject obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_adapter);

        //       LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear);
//        linearLayout.setBackgroundColor(Color.parseColor("#3982d9"));
        listView = (ListView) findViewById(R.id.listHealth);
        adapter = new HealthListAdapter(this, healthList);
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
                                health = new HelperHealth();
                                health.setMem_pic_path(obj.getString("mem_pic_path"));
                                health.setMem_topic(obj.getString("mem_topic"));
                                health.setMem_description(obj.getString("mem_description"));

                                // adding to array
                                healthList.add(health);

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



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
                AlertDialog.Builder alert ;
                switch(position){
                    case 0: alert = new AlertDialog.Builder(Health_tips.this);
                        alert.setTitle("5 วิธีเจ๋ง! กินมื้อเย็นอย่างไรไม่ให้อ้วน");
                        alert.setMessage("1.ไม่ควรงด แต่ควรลดอาหารมื้อเย็น เพราะการงดอาหารจะทำให้น้ำย่อยที่ออกมากัดกระเพาะ และจะทำให้ร่างกายปรับตัวเผาผลาญพลังงานลดลง ประสิทธิภาพของระบบเผาผลาญก็ลดตามลงไป ฉะนั้นจึงควรทานอาหารเย็นทุกวัน และให้เน้นวิธีการลดปริมาณอาหารลง\n" +"\n"+
                                "2.เลือกเวลาให้เหมาะสม ควรทานมื้อเย็น ช่วงเวลา 18.00-19.00 น. หรือก่อนเข้านอน 4-6 ชั่วโมง เพราะโปรตีนต้องใช้เวลาย่อยและดูดซึมถึง 4 ชั่วโมง และไม่ควรรับประทานอาหารแล้วนอนเลยเพราะอาจให้เกิดภาวะกรดไหลย้อนได้ \n"+"\n"+
                                "3.เลือกผัก แนะนำว่าให้ทานผักหรือผลไม้ในมื้อเย็น เพราะมีใยอาหารสูง พลังงานต่ำ ทำให้อิ่ม อยู่ท้อง เช่นข้าวซ้อมมือ ผักลวก ผักต้ม เนื้อปลา ตบท้ายด้วยผลไม้รสไม่หวาน น้ำตาลน้อย และควรเลือกให้หลากหลาย เพื่อให้ได้รับวิตามินและแร่ธาตุไม่ซ้ำกัน\n"+"\n"+
                                "4.เลี่ยงของทอด อาหารประเภทของมันและของทอดควรงดอย่างเด็ดขาด ถ้าเมนูที่มีเนื้อสัตว์ควรเลือกชนิดที่ย่อยง่าย เช่น เนื้อปลา \n"+"\n"+
                                "5.เดินย่อยดีกว่าออกกำลังกายทันที หลังทานมื้อเย็นการรีบไปออกกำลังกายต่อทันทีเป็นสิ่งที่ไม่ควรทำอย่างยิ่ง เพราะอาจทำให้จุกได้ ทางที่ดีควรใช้การเดินเรื่อยๆแทน เพราะเวลาเดินจะทำลำไส้จะขยับตัว ช่วยให้ย่อยง่ายขึ้น และยังได้ใช้พลังงานไปในตัวด้วย \n"+"\n"+
                                "\n" +
                                "ข้อมูลจาก: สสส.(25 สิงหาคม 2558)");
                        alert.setPositiveButton("Ok", null);
                        alert.show();
                        break;

                    case 1: alert = new AlertDialog.Builder(Health_tips.this);
                        alert.setTitle("พิชิตสุขภาพง่าย ๆ ด้วยหลัก 3 อ.");
                        alert.setMessage("1.อาหาร\n"+"เลือกรับประทานอาหารที่มีสารอาหารครบทั้ง 5 หมู่ เน้นผักผลไม้ที่มีกากใยสูงๆ รับประทานอาหารปรุงสุกใหม่ๆ เลี่ยงอาหารสุกๆดิบๆแ อาหารรสจัด และดื่มน้ำที่สะอาด\n"+"\n"+
                                "2.ออกกำลังกาย\n" + "ควรออกกำลังกายให้ร่างกายแข็งแรงอย่างน้อยสัปดาห์ละ 3 วันๆ ละ 30 นาที \n" + "\n" +
                                "3.อารมณ์\n" + "ทำจิตใจให้แจ่มใสอยู่เสมอ หาวิธีคลายเครียดหรือทำกิจกรรมร่วมกับครอบครัว เช่น ปลูกต้นไม้ เล่นดนตรี วาดภาพ ท่องเที่ยว เป็นต้น รวมทั้งควรพักผ่อนให้พอเพียงวันละ 6-8 ชั่วโมง \n" + "\n" +
                                "ข้อมูลจาก: สสส. (10 กรกฏาคม 2557)");
                        alert.setPositiveButton("Ok", null);
                        alert.show();
                        break;

                    case 2: alert = new AlertDialog.Builder(Health_tips.this);
                        alert.setTitle("วิธีการลงเท้าในการวิ่ง");
                        alert.setMessage("การลงเท้า มี 3 วิธี คือ\n" +
                                        "1. ลงส้นเท้าก่อนปลายเท้า\n" +
                                        "เป็นท่าที่เหมาะสำหรับนักวิ่งเพื่อสุขภาพ โดยส้นเท้าจะสัมผัสพื้นก่อนแล้วทั้งฝ่าเท้าจะตามมา และเมื่อปลายเท้าลงมาแตะพื้นก็เป็นจังหวะที่ส้นเท้ายกขึ้น เข่าไม่ยกสูงมากและไม่เหยียดสุด ปลายเท้าจะดันตัวไปข้างหน้า\n" +
                                        "\n" +
                                        "2. ลงเต็มฝ่าเท้า\n" +
                                        "ฝ่าเท้าจะสัมผัสพื้นพร้อมกันแล้วจึงใช้ปลายเท้าดันต่อไปข้างหน้า ซึ่งท่านี้จะสามารถลดแรงกระแทกของเท้าขณะลงพื้นได้ดี แต่เป็นท่าที่เมื่อยมากถ้าวิ่งไปไกลๆ\n" +"\n" +
                                        "3. ลงปลายเท้า\n" +
                                        "เหมาะสำหรับการวิ่งเพื่อแข่งขัน โดยจะลงพื้นด้วยปลายเท้าซึ่งทำให้มีพลังและความเร็วเพิ่มขึ้นมาก แต่เพิ่มแรงเครียดให้กล้ามเนื้อ น่องตึง และเอ็นร้อยหวายอักเสบได้ถ้หากวิ่งทางไกล"+"\n" +
                                        "\nข้อมูลจาก: เส้นทางสุขภาพดีเว็บกระปุก(2 กรกฎาคม 2558)");
                        alert.setPositiveButton("Ok", null);
                        alert.show();
                        break;

                    case 3:
                        alert = new AlertDialog.Builder(Health_tips.this);
                        alert.setTitle("สับปะรดดีต่อสุภาพสตรีและผู้ป่วย");
                        alert.setMessage("สำหรับสุภาพสตรีที่มีอาการปวดประจำเดือน อาการอักเสบจากริดสีดวงทวาร หรือผู้ป่วยอาการที่เกี่ยวข้องกับเส้นเลือดดำ โรคกระดูก ข้ออักเสบ รูมาตอยด์ เก๊าท์ หากรับประทานสับปะรดเป็นประจำ จะช่วยบรรเทาอาการต่างๆเหล่านี้ได้ รวมไปถึงสมานแผลให้ทุเลาได้เร็วขึ้นด้วย\n" + "\n" +
                                "ข้อมูลจาก: สุขภาพไทย (19 กุมภาพันธ์ 2013)");
                        alert.setPositiveButton("Ok", null);
                        alert.show();
                        break;

                    case 4:
                        alert = new AlertDialog.Builder(Health_tips.this);
                        alert.setTitle("4 พฤติกรรมบั่นทอนอายุ ที่ไม่ควรทำเป็นอันขาด");
                        alert.setMessage("1.กินไขมันเป็นประจำ\n" + "ไขมันเป็นตัวการสำคัญที่ส่งผลให้เซลล์ในร่างกายเกิดการเสื่อมสภาพ และกระตุ้นให้เซลล์มะเร็งเกิดการเจริญเติบโตอย่างรวดเร็ว อีกทั้งการที่ไขมันเข้าไปสะสมตามส่วนต่างๆของร่างกายจนทำให้หุ่นของเราดูอวบอ้วน จะส่งผลทำให้ผิวหนังหย่อนคล้อยไวมากขึ้นอีกด้วย\n" + "\n" +
                                "2.เครียดสะสมจนเคยชิน\n" + "ความเครียดเป็นตัวกระตุ้นให้ร่างกายของเราทำงานผิดปกติ ทำให้เสี่ยงต่อการเกิดโรคมะเร็ง และเซลล์ต่างๆเกิดการเสื่อมสภาพลงอย่างรวดเร็ว ทำให้เกิดริ้วรอยบนใบหน้าจนแลดูแก่ก่อนวัยขึ้นมาได้\n" + "\n" +
                                "3.กินอาหารที่ร้อนจัด\n" + "การกินอาหารที่มีความร้อนมากจนเกินไปก็ส่งผลเสียต่อเซลล์ภายในร่างกาย เพราะว่าเมื่อความร้อนสัมผัสเข้ากับเซลล์ผิวหนังของเราบ่อยๆ เข้า ตั้งแต่ลิ้นไปจนถึงหลอดอาหาร ก็อาจจะส่งผลให้เกิดการอักเสบและอาจจะแปรสภาพกลายเป็นเซลล์มะเร็งตามมาได้ ดังนั้นทางที่ดีเราควรกินอาหารและดื่มเครื่องดื่มในขณะที่อุ่นๆ จะดีกว่า\n" + "\n" +
                                "4.กินอาหารรสจัดจ้านเป็นประจำ\n" + "การกินอาหารรสจัดจะไปกระตุ้นให้ระบบการทำงานของร่างกายต้องทำงานหนัก โดยเฉพาะอาหารรสเค็มจัด จะทำให้ร่างกายรู้สึกขาดน้ำ ส่งผลให้ไตทำงานหนักเพื่อกำจัดความเค็มส่วนเกินออกไป และยังเสี่ยงต่อการเกิดมะเร็งกระเพาะอาหารจากการระคายเคืองอยู่บ่อยครั้ง รวมไปถึงระบบร่างกายถูกกระตุ้นจนทำให้เกิดสิวในปริมาณมากบนใบหน้าก็อาจจะส่งผลให้ใบหน้าหมองค้ำและไม่เรียบเนียนตามมา\n" + "\n" +
                                "ข้อมูลจาก: สาระเพื่อสุขภาพและชีวิต (21 ตุลาคม 2557)");
                        alert.setPositiveButton("Ok", null);
                        alert.show();
                        break;

                    case 5: alert = new AlertDialog.Builder(Health_tips.this);
                        alert.setTitle(health.getMem_topic());
                        alert.setMessage(health.getMem_description());
                        alert.setPositiveButton("Ok", null);
                        alert.show();
                        break;

                    case 6: alert = new AlertDialog.Builder(Health_tips.this);
                        alert.setTitle(health.getMem_topic());
                        alert.setMessage(health.getMem_description());
                        alert.setPositiveButton("Ok", null);
                        alert.show();
                        break;

                    case 7: alert = new AlertDialog.Builder(Health_tips.this);
                        alert.setTitle(health.getMem_topic());
                        alert.setMessage(health.getMem_description());
                        alert.setPositiveButton("Ok", null);
                        alert.show();
                        break;

                    case 8: alert = new AlertDialog.Builder(Health_tips.this);
                        alert.setTitle(health.getMem_topic());
                        alert.setMessage(health.getMem_description());
                        alert.setPositiveButton("Ok", null);
                        alert.show();
                        break;

                    case 9: alert = new AlertDialog.Builder(Health_tips.this);
                        alert.setTitle(health.getMem_topic());
                        alert.setMessage(health.getMem_description());
                        alert.setPositiveButton("Ok", null);
                        alert.show();
                        break;

                    case 10: alert = new AlertDialog.Builder(Health_tips.this);
                        alert.setTitle(health.getMem_topic());
                        alert.setMessage(health.getMem_description());
                        alert.setPositiveButton("Ok", null);
                        alert.show();
                        break;
                }

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