package com.boostcamp.sentialarm.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.boostcamp.sentialarm.DTO.AlarmDTO;
import com.boostcamp.sentialarm.Alarm.AlarmScheduler;
import com.boostcamp.sentialarm.DAO.AlarmDAO;

import io.realm.RealmResults;

/**
 * Created by 현기 on 2017-08-07.
 *
 *  부팅시 데이터베이스에 있는 모든 알람 데이터를 가져와서 알람 매니저에 등록한다.
 */



public class BootLoadAlarmReceiver extends BroadcastReceiver {

    private AlarmDAO alarmDAO;


    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {

            alarmDAO = new AlarmDAO();
            try {
                alarmDAO.creatAlarmRealm();
                RealmResults<AlarmDTO> alarms = alarmDAO.getAllAlarm();

                Log.i("부트로더","알람 재등록");
                for(int i=0; i < alarms.size();i++){
                    AlarmDTO alarmDTO = alarms.get(i);
                    AlarmScheduler.registerAlarm(context.getApplicationContext(), alarmDTO.getId(), alarmDTO.getAlarmHour(), alarmDTO.getAlarmMinute());
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                alarmDAO.closeAlarmRealm();
            }
        }

    }
}
