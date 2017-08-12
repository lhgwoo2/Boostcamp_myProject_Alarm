package com.boostcamp.sentialarm;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.boostcamp.sentialarm.Alarm.AlarmDAO;
import com.boostcamp.sentialarm.Util.BaseActivity;
import com.rd.PageIndicatorView;

public class MainActivity extends BaseActivity {

    public MainFragmentAdapter mainFragmentAdapter;
    public ViewPager viewPager;

    public AlarmDAO alarmDAO=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getViewPager();
        getMainFragmentAdapter();
        viewPager.setAdapter(mainFragmentAdapter);

        PageIndicatorView pageIndicatorView = (PageIndicatorView) findViewById(R.id.pageIndicatorView);
        pageIndicatorView.setViewPager(viewPager);

        alarmDAO = new AlarmDAO();
        alarmDAO.creatAlarmRealm();


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
        alarmDAO.closeAlarmRealm();

        super.onDestroy();
    }
}
