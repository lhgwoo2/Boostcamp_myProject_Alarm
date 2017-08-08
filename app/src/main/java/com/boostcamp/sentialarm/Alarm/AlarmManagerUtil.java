package com.boostcamp.sentialarm.Alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;

import java.util.Calendar;

/**
 * Created by 현기 on 2017-08-07.
 */

public class AlarmManagerUtil {

    public static AlarmManager alarmManager;


    public static void getAlarmManager(Context context){
        if(alarmManager == null){
            alarmManager = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        }
    }

    //알람 등록
    public static void setOnceAlarm(int hourOfDay, int minute, PendingIntent alarmPendingIntent) {
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M )
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, getTriggerAtMillis(hourOfDay, minute), alarmPendingIntent);

        else if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, getTriggerAtMillis(hourOfDay, minute), alarmPendingIntent);
        else
            alarmManager.set(AlarmManager.RTC_WAKEUP, getTriggerAtMillis(hourOfDay, minute), alarmPendingIntent);
    }

    // 현재시간과 비교하여 알람시간이 현재 시간을 넘었으면 다음날로 지정
    private static long getTriggerAtMillis(int hourOfDay, int minute) {
        Calendar currentCalendar = Calendar.getInstance();
        int currentHourOfDay = currentCalendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = currentCalendar.get(Calendar.MINUTE);

        if ( currentHourOfDay < hourOfDay || ( currentHourOfDay == hourOfDay && currentMinute < minute ) )
            return getTimeInMillis(false, hourOfDay, minute);
        else
            return getTimeInMillis(true, hourOfDay, minute);
    }

    // 알람시간을 지정하여 알람을 가져온다. 현재시간이 넘었으면 하루를 추가한다.
    private static long getTimeInMillis(boolean tomorrow, int hourOfDay, int minute) {
        Calendar calendar = (Calendar) Calendar.getInstance();

        if ( tomorrow )
            calendar.add(Calendar.DAY_OF_YEAR, 1);

        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis();
    }
    public static boolean checkWeekly(AlarmDTO alarmDTO){

        Calendar calendar = Calendar.getInstance();

        if(alarmDTO.isSunday() && (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)){
            return true;
        }else if(alarmDTO.isMonday() && (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY)){
            return true;
        }else if(alarmDTO.isTuesday() && (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY)){
            return true;
        }else if(alarmDTO.isWednesday() && (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY)){
            return true;
        }else if(alarmDTO.isThursday() && (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY)){
            return true;
        }else if(alarmDTO.isFriday() && (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY)){
            return true;
        }else if(alarmDTO.isSaturday() && (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)){
            return true;
        }

        return false;
    }

}
