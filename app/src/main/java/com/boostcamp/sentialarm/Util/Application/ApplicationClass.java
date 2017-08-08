package com.boostcamp.sentialarm.Util.Application;

import android.app.Application;

import com.tsengvn.typekit.Typekit;

import io.realm.Realm;

/**
 * Created by 현기 on 2017-07-26.
 */

public class ApplicationClass extends Application {
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
    }


}
