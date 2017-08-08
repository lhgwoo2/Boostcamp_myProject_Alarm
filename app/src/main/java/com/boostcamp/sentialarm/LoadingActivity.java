package com.boostcamp.sentialarm;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.WindowManager;

import com.boostcamp.sentialarm.Util.BaseAtivity.BaseActivity;

public class LoadingActivity extends BaseActivity {
    int SPLASH_TIME = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //메인 뷰 호출 전에 상태바가 없는 풀스크린으로 표시
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_loading);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                overridePendingTransition(0,android.R.anim.fade_in);
                startActivity(new Intent(LoadingActivity.this,MainActivity.class));
                finish();
            }
        }, SPLASH_TIME);
    }
}
