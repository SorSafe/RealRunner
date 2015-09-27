package com.example.acerth.realrunner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;


public class Health_tips extends Activity {
    // Array of strings storing country names
    String[] topics = new String[] {
            "วิธีการลงเท้าในการวิ่ง",
            "5 วิธีเจ๋ง! กินมื้อเย็นอย่างไรไม่ให้อ้วน",
            "พิชิตสุขภาพง่าย ๆ ด้วยหลัก 3 อ.",
            "สับปะรดดีต่อสุภาพสตรีและผู้ป่วย",
            "4 พฤติกรรมบั่นทอนอายุ ที่ไม่ควรทำเป็นอันขาด"
    };

    // Array of integers points to images stored in /res/drawable-ldpi/
    int[] imgs = new int[]{
            R.drawable.img1,
            R.drawable.img2,
            R.drawable.img3,
            R.drawable.img4,
            R.drawable.img5,
    };

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.health_adapter);

        // Each row in the list stores country name, description and img
        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < 5; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("topic",topics[i]);
            //hm.put("description","description : " + description[i]);
            hm.put("img", Integer.toString(imgs[i]));
            aList.add(hm);
        }

        // Keys used in Hashmap
        String[] from = {"img", "topic"};

        // Ids of views in listview_layout
        int[] to = {R.id.img, R.id.topic};

        // Instantiating an adapter to store each items
        // R.layout.health_item defines the layout of each item
        SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.health_item, from, to);

        // Getting a reference to listview of main.xml layout file
        final ListView listView = (ListView) findViewById(R.id.listview);

        // Setting the adapter to the listView
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
                AlertDialog.Builder alert ;
                switch(position){
                    case 0: alert = new AlertDialog.Builder(
                            Health_tips.this);
                        alert.setTitle("วิธีการลงเท้าในการวิ่ง");
                        alert.setMessage("การลงเท้า มี 3 วิธี คือ\n" +
                                        "1. ลงส้นเท้าก่อนปลายเท้า\n" +
                                        "เป็นท่าที่เหมาะสำหรับนักวิ่งเพื่อสุขภาพ โดยส้นเท้าจะสัมผัสพื้นก่อนแล้วทั้งฝ่าเท้าจะตามมา และเมื่อปลายเท้าลงมาแตะพื้นก็เป็นจังหวะที่ส้นเท้ายกขึ้น เข่าไม่ยกสูงมากและไม่เหยียดสุด ปลายเท้าจะดันตัวไปข้างหน้า\n" +
                                        "\n" +
                                        "2. ลงเต็มฝ่าเท้า\n" +
                                        "ฝ่าเท้าจะสัมผัสพื้นพร้อมกันแล้วจึงใช้ปลายเท้าดันต่อไปข้างหน้า ซึ่งท่านี้จะสามารถลดแรงกระแทกของเท้าขณะลงพื้นได้ดี แต่เป็นท่าที่เมื่อยมากถ้าวิ่งไปไกลๆ\n" +"\n" +
                                        "3. ลงปลายเท้า\n" +
                                        "เหมาะสำหรับการวิ่งเพื่อแข่งขัน โดยจะลงพื้นด้วยปลายเท้าซึ่งทำให้มีพลังและความเร็วเพิ่มขึ้นมาก แต่เพิ่มแรงเครียดให้กล้ามเนื้อ น่องตึง และเอ็นร้อยหวายอักเสบได้ถ้หากวิ่งทางไกล"+"\n" +
                                        "\nข้อมูลจาก: สสส."
                        );
                        alert.setPositiveButton("Ok", null);
                        alert.show();
                        break;
                    case 1: alert = new AlertDialog.Builder(
                            Health_tips.this);
                        alert.setTitle("5 วิธีเจ๋ง! กินมื้อเย็นอย่างไรไม่ให้อ้วน");
                        alert.setMessage("1.ไม่ควรงด แต่ควรลดอาหารมื้อเย็น เพราะการงดอาหารจะทำให้น้ำย่อยที่ออกมากัดกระเพาะ และจะทำให้ร่างกายปรับตัวเผาผลาญพลังงานลดลง ประสิทธิภาพของระบบเผาผลาญก็ลดตามลงไป ฉะนั้นจึงควรทานอาหารเย็นทุกวัน และให้เน้นวิธีการลดปริมาณอาหารลง\n" +"\n"+
                                "2.เลือกเวลาให้เหมาะสม ควรทานมื้อเย็น ช่วงเวลา 18.00-19.00 น. หรือก่อนเข้านอน 4-6 ชั่วโมง เพราะโปรตีนต้องใช้เวลาย่อยและดูดซึมถึง 4 ชั่วโมง และไม่ควรรับประทานอาหารแล้วนอนเลยเพราะอาจให้เกิดภาวะกรดไหลย้อนได้ \n"+"\n"+
                                "3.เลือกผัก แนะนำว่าให้ทานผักหรือผลไม้ในมื้อเย็น เพราะมีใยอาหารสูง พลังงานต่ำ ทำให้อิ่ม อยู่ท้อง เช่นข้าวซ้อมมือ ผักลวก ผักต้ม เนื้อปลา ตบท้ายด้วยผลไม้รสไม่หวาน น้ำตาลน้อย และควรเลือกให้หลากหลาย เพื่อให้ได้รับวิตามินและแร่ธาตุไม่ซ้ำกัน\n"+"\n"+
                                "4.เลี่ยงของทอด อาหารประเภทของมันและของทอดควรงดอย่างเด็ดขาด ถ้าเมนูที่มีเนื้อสัตว์ควรเลือกชนิดที่ย่อยง่าย เช่น เนื้อปลา \n"+"\n"+
                                "5.เดินย่อยดีกว่าออกกำลังกายทันที หลังทานมื้อเย็นการรีบไปออกกำลังกายต่อทันทีเป็นสิ่งที่ไม่ควรทำอย่างยิ่ง เพราะอาจทำให้จุกได้ ทางที่ดีควรใช้การเดินเรื่อยๆแทน เพราะเวลาเดินจะทำลำไส้จะขยับตัว ช่วยให้ย่อยง่ายขึ้น และยังได้ใช้พลังงานไปในตัวด้วย \n"+"\n"+
                                "ข้อมูลจาก: สสส.");
                        alert.setPositiveButton("Ok", null);
                        alert.show();
                        break;
                    case 2: alert = new AlertDialog.Builder(
                            Health_tips.this);
                        alert.setTitle("พิชิตสุขภาพง่าย ๆ ด้วยหลัก 3 อ.");
                        alert.setMessage("1.อาหาร\n"+"เลือกรับประทานอาหารที่มีสารอาหารครบทั้ง 5 หมู่ เน้นผักผลไม้ที่มีกากใยสูงๆ รับประทานอาหารปรุงสุกใหม่ๆ เลี่ยงอาหารสุกๆดิบๆแ อาหารรสจัด และดื่มน้ำที่สะอาด\n"+"\n"+
                                "2.ออกกำลังกาย\n"+"ควรออกกำลังกายให้ร่างกายแข็งแรงอย่างน้อยสัปดาห์ละ 3 วันๆ ละ 30 นาที \n"+"\n"+
                                "3.อารมณ์\n"+"ทำจิตใจให้แจ่มใสอยู่เสมอ หาวิธีคลายเครียดหรือทำกิจกรรมร่วมกับครอบครัว เช่น ปลูกต้นไม้ เล่นดนตรี วาดภาพ ท่องเที่ยว เป็นต้น รวมทั้งควรพักผ่อนให้พอเพียงวันละ 6-8 ชั่วโมง \n"+"\n"+
                                "ข้อมูลจาก: สสส.");
                        alert.setPositiveButton("Ok", null);
                        alert.show();
                        break;
                    case 3: alert = new AlertDialog.Builder(
                            Health_tips.this);
                        alert.setTitle("สับปะรดดีต่อสุภาพสตรีและผู้ป่วย");
                        alert.setMessage("สำหรับสุภาพสตรีที่มีอาการปวดประจำเดือน อาการอักเสบจากริดสีดวงทวาร หรือผู้ป่วยอาการที่เกี่ยวข้องกับเส้นเลือดดำ โรคกระดูก ข้ออักเสบ รูมาตอยด์ เก๊าท์ หากรับประทานสับปะรดเป็นประจำ จะช่วยบรรเทาอาการต่างๆเหล่านี้ได้ รวมไปถึงสมานแผลให้ทุเลาได้เร็วขึ้นด้วย\n" + "\n" +
                                "ข้อมูลจาก: สสส.");
                        alert.setPositiveButton("Ok", null);
                        alert.show();
                        break;
                    case 4: alert = new AlertDialog.Builder(
                            Health_tips.this);
                        alert.setTitle("4 พฤติกรรมบั่นทอนอายุ ที่ไม่ควรทำเป็นอันขาด");
                        alert.setMessage("1.กินไขมันเป็นประจำ\n" + "ไขมันเป็นตัวการสำคัญที่ส่งผลให้เซลล์ในร่างกายเกิดการเสื่อมสภาพ และกระตุ้นให้เซลล์มะเร็งเกิดการเจริญเติบโตอย่างรวดเร็ว อีกทั้งการที่ไขมันเข้าไปสะสมตามส่วนต่างๆของร่างกายจนทำให้หุ่นของเราดูอวบอ้วน จะส่งผลทำให้ผิวหนังหย่อนคล้อยไวมากขึ้นอีกด้วย\n" + "\n" +
                                "2.เครียดสะสมจนเคยชิน\n" + "ความเครียดเป็นตัวกระตุ้นให้ร่างกายของเราทำงานผิดปกติ ทำให้เสี่ยงต่อการเกิดโรคมะเร็ง และเซลล์ต่างๆเกิดการเสื่อมสภาพลงอย่างรวดเร็ว ทำให้เกิดริ้วรอยบนใบหน้าจนแลดูแก่ก่อนวัยขึ้นมาได้\n" + "\n" +
                                "3.กินอาหารที่ร้อนจัด\n" + "การกินอาหารที่มีความร้อนมากจนเกินไปก็ส่งผลเสียต่อเซลล์ภายในร่างกาย เพราะว่าเมื่อความร้อนสัมผัสเข้ากับเซลล์ผิวหนังของเราบ่อยๆ เข้า ตั้งแต่ลิ้นไปจนถึงหลอดอาหาร ก็อาจจะส่งผลให้เกิดการอักเสบและอาจจะแปรสภาพกลายเป็นเซลล์มะเร็งตามมาได้ ดังนั้นทางที่ดีเราควรกินอาหารและดื่มเครื่องดื่มในขณะที่อุ่นๆ จะดีกว่า\n" + "\n" +
                                "4.กินอาหารรสจัดจ้านเป็นประจำ\n" + "การกินอาหารรสจัดจะไปกระตุ้นให้ระบบการทำงานของร่างกายต้องทำงานหนัก โดยเฉพาะอาหารรสเค็มจัด จะทำให้ร่างกายรู้สึกขาดน้ำ ส่งผลให้ไตทำงานหนักเพื่อกำจัดความเค็มส่วนเกินออกไป และยังเสี่ยงต่อการเกิดมะเร็งกระเพาะอาหารจากการระคายเคืองอยู่บ่อยครั้ง รวมไปถึงระบบร่างกายถูกกระตุ้นจนทำให้เกิดสิวในปริมาณมากบนใบหน้าก็อาจจะส่งผลให้ใบหน้าหมองค้ำและไม่เรียบเนียนตามมา\n" + "\n" +
                                "ข้อมูลจาก: สาระเพื่อสุขภาพและชีวิต");
                        alert.setPositiveButton("Ok", null);
                        alert.show();
                        break;
                }

            }
        });
    }
}