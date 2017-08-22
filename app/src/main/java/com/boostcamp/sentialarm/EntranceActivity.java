package com.boostcamp.sentialarm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.boostcamp.sentialarm.Util.BaseActivity;
import com.bumptech.glide.Glide;

public class EntranceActivity extends BaseActivity {

    private LinearLayout alarmRegistLayout;
    private LinearLayout alarmListLayout;
    private LinearLayout songListLayout;

    private ImageView entranceBackgroundImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);
        overridePendingTransition(android.R.anim.fade_in, 0);



        entranceBackgroundImageView = (ImageView) findViewById(R.id.entrance_background_iv);
        alarmRegistLayout = (LinearLayout) findViewById(R.id.entrance_alarm_layout);
        alarmListLayout = (LinearLayout) findViewById(R.id.entrance_alarmlist_layout);
        songListLayout = (LinearLayout) findViewById(R.id.entrance_songlist_layout);

        Glide.with(this).load(R.drawable.bg_entrance_background_2).into(entranceBackgroundImageView);



        alarmRegistLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("page",0);
                startActivity(intent);
            }
        });

        alarmListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("page",1);
                startActivity(intent);
            }
        });

        songListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("page",2);
                startActivity(intent);
            }
        });
    }
}
