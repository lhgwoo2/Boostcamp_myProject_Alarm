package com.boostcamp.sentialarm.API.Alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.boostcamp.alarmtest.DTO.AlarmDTO;
import com.boostcamp.alarmtest.Receiver.AlarmReceiver;

import java.util.Calendar;

/**
 * Created by 현기 on 2017-08-07.
 */

public class Alarm {

    public static AlarmManager alarmManager;
    public static PendingIntent pIntent;
    public Context context;

    public int ALARM_ID = 123;

    public static Alarm alarm = null;


    private Alarm(Context context) {
        this.context = context;
    }

    public static Alarm getAlarmInstance(Context context) {

        if (alarm == null) {
            alarm = new Alarm(context.getApplicationContext());
        }

        return alarm;
    }

    public static AlarmManager getAlarmManagerInstance(Context context) {
        if (alarmManager == null) {
            alarmManager = (AlarmManager) context
                    .getSystemService(Context.ALARM_SERVICE);
        }
        return alarmManager;
    }

    public void enrollAlarm(AlarmDTO alarmDTO) {

        Intent intent = new Intent(context.getApplicationContext(), AlarmReceiver.class);

        boolean[] week = setWeekAlarm(alarmDTO);
        intent.putExtra("weekly", week);
        intent
        pIntent = PendingIntent.getBroadcast(context.getApplicationContext(), alarmDTO.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);


        // android 버전에 따른 메소드 사용, 21 level 이상 doze모드 고려
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(getTime().getTimeInMillis(), pIntent), pIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, getTime().getTimeInMillis(), pIntent);
        } else {
            // repeating으로 매일 알람 체크 설정
            long oneday = 24 * 60 * 60 * 1000;// 24시간
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, getTime().getTimeInMillis(), oneday, pIntent);
        }

    }

    public boolean[] setWeekAlarm(AlarmDTO alarmDTO) {

        //default,일,월,화,수,목,금,토   sunday=1 이라서 0의 자리에는 아무 값이나 넣었음
        boolean week[] = new boolean[8];
        week[1] = alarmDTO.isSunday();
        week[2] = alarmDTO.isMonday();
        week[3] = alarmDTO.isTuesday();
        week[4] = alarmDTO.isWednesday();
        week[5] = alarmDTO.isThursday();
        week[6] = alarmDTO.isFriday();
        week[7] = alarmDTO.isSaturday();

        return week;
    }

    public void cancelAlarmManger() {
        if (pIntent != null) {
            alarmManager = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context.getApplicationContext(), AlarmReceiver.class);
            pIntent = PendingIntent.getBroadcast(context.getApplicationContext(), ALARM_ID, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            alarmManager.cancel(pIntent);
            pIntent.cancel();
            alarmManager = null;
            pIntent = null;
        }
    }

    private Calendar getTime() {
        Calendar cal = Calendar.getInstance();      //현재시간 가져오기

        // TODO 테스트를 위해 10초 뒤 알람 설정
        cal.set(Calendar.SECOND, cal.get(Calendar.SECOND) + 10); // 10초 뒤

        Long time = (long) cal.get(Calendar.SECOND) + 10;
        Log.i("알람전송시간", time.toString());

        return cal;
    }

}
