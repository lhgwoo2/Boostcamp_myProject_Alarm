package com.boostcamp.sentialarm.Receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.boostcamp.alarmtest.API.Alarm.Alarm;
import com.boostcamp.alarmtest.AlarmPopActivity;
import com.boostcamp.alarmtest.DTO.AlarmDTO;

import io.realm.Realm;

/**
 * Created by 현기 on 2017-08-03.
 */

public class AlarmReceiver extends BroadcastReceiver {

    private int alarmID;
    private AlarmManager alarmManager;



    @Override
    public void onReceive(Context context, Intent intent) {

        alarmManager = Alarm.getAlarmManagerInstance(context);


        try {

            Toast.makeText(context,"알림",Toast.LENGTH_LONG).show();
            Log.i("알람리시버","알람왔다.");
            boolean[] week =intent.getBooleanArrayExtra("weekly");

            Intent nextIntent = new Intent(context, AlarmPopActivity.class);
            nextIntent.putExtra("weekly", week);
            PendingIntent pi = PendingIntent.getActivity(context, 123, nextIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            pi.send();

        } catch (PendingIntent.CanceledException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String reEnrollAlarm(long alarmID){

        Realm realm = Realm.getDefaultInstance();
        AlarmDTO alarmDTO = realm.where(AlarmDTO.class).equalTo("id", alarmID).findFirst();

        alarmManager.
    }

}
