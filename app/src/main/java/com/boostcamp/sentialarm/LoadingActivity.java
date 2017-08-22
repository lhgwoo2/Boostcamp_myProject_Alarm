package com.boostcamp.sentialarm;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.boostcamp.sentialarm.Util.BaseActivity;
import com.bumptech.glide.Glide;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

public class LoadingActivity extends BaseActivity {
    int SPLASH_TIME = 3000;

    Animation fadeAnimation;

    private ImageView loadingBackgroundImageView;
    private TextView mainTextView;
    private TextView subTitle;
    private ImageView loadingImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_loading);
        loadingBackgroundImageView = (ImageView) findViewById(R.id.loading_background_iv);
        mainTextView = (TextView) findViewById(R.id.loading_name);
        subTitle = (TextView) findViewById(R.id.loading_subtitle_tv);
        loadingImage = (ImageView) findViewById(R.id.ic_loading_alarm);
        Glide.with(this).load(R.drawable.bg_entrance_background_2).into(loadingBackgroundImageView);

        fadeAnimation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
        fadeAnimation.setStartOffset(1000);
        fadeAnimation.setDuration(1700);
        subTitle.startAnimation(fadeAnimation);
        loadingImage.startAnimation(fadeAnimation);

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();

                //다음 화면 진행
                moveMainActivity();


            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(getApplicationContext(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();

                //거부당한 경우 앱 종료
                finish();
            }


        };


        new TedPermission(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();


    }

    private void moveMainActivity(){
        //메인 뷰 호출 전에 상태바가 없는 풀스크린으로 표시
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(LoadingActivity.this,EntranceActivity.class));
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);


            }
        }, SPLASH_TIME);
    }


}
