package com.boostcamp.sentialarm.Alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by 현기 on 2017-08-08.
 */


// 알람 등록을 위한 스케줄러 클래스

public class AlarmScheduler {

    public static void registerAlarm(Context context, int alarmId, int hourOfDay, int minute) {
        AlarmManager alarmManager = AlarmManagerUtil.getAlarmManager(context.getApplicationContext());
        AlarmManagerUtil.setOnceAlarm(hourOfDay, minute, getRepeatingAlarmPendingIntent(context.getApplicationContext(), alarmId));
    }

    private static PendingIntent getRepeatingAlarmPendingIntent(Context context, int alarmId) {
        int requestCode = alarmId;
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(AlarmReceiver.KEY_ALARM_ID, alarmId);

        return PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static void releaseAlarm(int alarmID, Context context){
        Log.i("알람해제", "알람번호"+alarmID);
        Intent intent  = new Intent(context.getApplicationContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), alarmID , intent, PendingIntent.FLAG_NO_CREATE); // pending Intent가 없으면 null, 있으면 가져온다.
        AlarmManager alarmManager =  AlarmManagerUtil.getAlarmManager(context);
        alarmManager.cancel(pendingIntent);
    }

}
