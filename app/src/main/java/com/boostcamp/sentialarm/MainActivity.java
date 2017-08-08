package com.boostcamp.sentialarm;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.boostcamp.sentialarm.Util.BaseAtivity.BaseActivity;

import io.realm.Realm;

public class MainActivity extends BaseActivity {

    public MainFragmentAdapter mainFragmentAdapter;
    public ViewPager viewPager;
    public Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getViewPager();
        getMainFragmentAdapter();
        viewPager.setAdapter(mainFragmentAdapter);

        //램 데이터베이스 초기화
        realm = Realm.getDefaultInstance();

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

        //램 종료
        realm.close();
        super.onDestroy();
    }
}
