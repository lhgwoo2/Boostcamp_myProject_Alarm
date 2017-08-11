package com.boostcamp.sentialarm.Util.Application;

import android.app.Application;

import com.tsengvn.typekit.Typekit;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by 현기 on 2017-07-26.
 */

public class ApplicationClass extends Application {

    public static RealmConfiguration alarmListConfig;
    public static RealmConfiguration songListConfig;


    @Override
    public void onCreate() {
        super.onCreate();


        //realm DB 초기화
        Realm.init(this);

        //Realm.deleteRealm(Realm.getDefaultConfiguration());

        //폰트
        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "NanumGothic.ttf"))
                .addBold(Typekit.createFromAsset(this, "NanumGothicBold.ttf"))
                .addCustom1(Typekit.createFromAsset(this, "DXPnM-KSCpc-EUC-H.ttf"));


        alarmListConfig = new RealmConfiguration.Builder()
                .name("alarmList.realm")
                .schemaVersion(1)
                .build();


        songListConfig = new RealmConfiguration.Builder()
                .name("songList.realm")
                .schemaVersion(2)
                .build();

    }


}
