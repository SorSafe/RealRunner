package com.example.acerth.realrunner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.acerth.helper.SessionManager;


public class Tab_Ranking extends Activity {

    private ImageButton btnOverall;
    private ImageButton btnoverallFriend;
    private ImageButton btnWeekly;
    private ImageButton btnWeeklyFriend;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ranking);

        // session manager
        session = new SessionManager(getApplicationContext());

        btnOverall = (ImageButton) findViewById(R.id.overall);
        btnoverallFriend = (ImageButton) findViewById(R.id.overallFriend);
        btnWeekly = (ImageButton) findViewById(R.id.weekly);
        btnWeeklyFriend = (ImageButton) findViewById(R.id.weeklyFriend);

        btnOverall.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                session.setLogin(true);

                Intent intent = new Intent(Tab_Ranking.this, Tab_Ranking_Overall_Global.class);
                startActivity(intent);
                finish();
            }
        });

        btnoverallFriend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                session.setLogin(true);

                Intent intent = new Intent(Tab_Ranking.this, Tab_Ranking_Overall_Friends.class);
                startActivity(intent);
                finish();
            }
        });

        btnWeekly.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                session.setLogin(true);

                Intent intent = new Intent(Tab_Ranking.this, Tab_Ranking_Weekly_Global.class);
                startActivity(intent);
                finish();
            }
        });

        btnWeeklyFriend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                session.setLogin(true);

                Intent intent = new Intent(Tab_Ranking.this, Tab_Ranking_Weekly_Friends.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
