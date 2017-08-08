package com.boostcamp.sentialarm.Alarm;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * Created by 현기 on 2017-08-08.
 */


// 알람 등록을 위한 스케줄러 클래스

public class AlarmScheduler {

    public static void registerAlarm(Context context, int alarmId, int hourOfDay, int minute) {
        AlarmManagerUtil.getAlarmManager(context);
        AlarmManagerUtil.setOnceAlarm(hourOfDay, minute, getRepeatingAlarmPendingIntent(context, alarmId));
    }

    private static PendingIntent getRepeatingAlarmPendingIntent(Context context, int alarmId) {
        int requestCode = alarmId;
        Intent intent = new Intent(context.getApplicationContext(), AlarmReceiver.class);
        intent.putExtra(AlarmReceiver.KEY_ALARM_ID, alarmId);

        return PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

}
