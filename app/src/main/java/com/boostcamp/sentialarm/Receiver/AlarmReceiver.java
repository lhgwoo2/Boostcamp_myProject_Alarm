package com.boostcamp.sentialarm.Receiver;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.boostcamp.sentialarm.Activity.AlarmPopActivity;
import com.boostcamp.sentialarm.Alarm.AlarmScheduler;
import com.boostcamp.sentialarm.DAO.AlarmDAO;
import com.boostcamp.sentialarm.DTO.AlarmDTO;
import com.boostcamp.sentialarm.Util.AlarmManagerUtil;

/**
 * Created by 현기 on 2017-08-03.
 */

public class AlarmReceiver extends BroadcastReceiver {

    public static final String KEY_ALARM_ID = "AlarmReceiver.KEY_ALARM_ID";
    private AlarmDAO alarmDAO;

    // 알람을 받으면, 다시 재등록한다.
    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            int alarmId = intent.getIntExtra(KEY_ALARM_ID, -1);
            if (alarmId != -1) {

                alarmDAO = new AlarmDAO();
                alarmDAO.creatAlarmRealm();     // 램 열기
                AlarmDTO alarmDTO = alarmDAO.getAlarm(alarmId);

                Log.i("알람실행", "알람 리시버");

                // 이 알람이 현재 요일에 작동하는지
                if (AlarmManagerUtil.checkWeekly(alarmDTO)) {
                    AlarmScheduler.registerAlarm(context, alarmDTO.getId(), alarmDTO.getAlarmHour(), alarmDTO.getAlarmMinute());
                    Intent nextIntent = new Intent(context, AlarmPopActivity.class);

                    nextIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  //새로운 태스크로 화면을 띄움.
                    nextIntent.putExtra("alarmID", alarmId);
                    PendingIntent pi = PendingIntent.getActivity(context, alarmId, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    pi.send();
                }
            }
        } catch (PendingIntent.CanceledException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally {
            alarmDAO.closeAlarmRealm();
        }

    }


}
