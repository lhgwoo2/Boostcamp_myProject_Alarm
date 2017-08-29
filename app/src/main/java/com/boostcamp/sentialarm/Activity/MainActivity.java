package com.boostcamp.sentialarm.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.boostcamp.sentialarm.Adapter.MainFragmentAdapter;
import com.boostcamp.sentialarm.R;
import com.boostcamp.sentialarm.Util.BaseActivity;
import com.bumptech.glide.Glide;

public class MainActivity extends BaseActivity {

    public MainFragmentAdapter mainFragmentAdapter;
    public ViewPager viewPager;
    private TabLayout mTabLayout;


    private ImageView mainBackgroundImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainBackgroundImageView = (ImageView) findViewById(R.id.main_background_iv);

        Glide.with(this).load(R.drawable.bg_main_background_6).into(mainBackgroundImageView);


        getViewPager();
        getMainFragmentAdapter();
        viewPager.setAdapter(mainFragmentAdapter);

        mTabLayout = (TabLayout)findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        LinearLayout homelayout = (LinearLayout) findViewById(R.id.home_layout);
        homelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),EntranceActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });


        Intent intent = getIntent();
        int page = intent.getIntExtra("page",0);

        //선택된 페이지로 이동
        getViewPager().setCurrentItem(page);
    }


    private void setupTabIcons() {
        mTabLayout.getTabAt(0).setIcon(R.drawable.ic_navi_alarm);
        mTabLayout.getTabAt(1).setIcon(R.drawable.ic_navi_alarmlist);
        mTabLayout.getTabAt(2).setIcon(R.drawable.ic_navi_songlist);
    }


    public MainFragmentAdapter getMainFragmentAdapter(){
        if (null == mainFragmentAdapter) {
            mainFragmentAdapter = new MainFragmentAdapter(getSupportFragmentManager());
        }
        return mainFragmentAdapter;
    }

    public ViewPager getViewPager() {
        if (null == viewPager) {
            viewPager = (ViewPager)findViewById(R.id.view_pager);
        }
        return viewPager;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
