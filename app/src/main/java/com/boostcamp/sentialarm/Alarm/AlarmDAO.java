package com.boostcamp.sentialarm.Alarm;

import android.util.Log;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by 현기 on 2017-08-08.
 */

public class AlarmDAO {

   public Realm realm;

    //램 열기
    public void creatAlarmRealm(RealmConfiguration alarmListConfig){

        realm = Realm.getInstance(alarmListConfig);
    }

    // 램 닫기
    public void closeAlarmRealm(){
        if (realm != null) {
            realm.close();
            realm = null;
        }
    }

    public AlarmDTO getAlarm(int alarmID){
        AlarmDTO alarmDTO = realm.where(AlarmDTO.class).equalTo("id", alarmID).findFirst();
        return alarmDTO;
    }



    //알람 저장 - 램
    public AlarmDTO setEnrollAlarm(int hour, int minute, boolean alarmOnOff,
                               boolean isMonday, boolean isTuesday, boolean isWednesday, boolean isThursday, boolean isFriday, boolean isSaturday, boolean isSunday){

            realm.beginTransaction();
            // 저장 객체에 count 주기
            int nextId = incremnetAlarmCount();

            AlarmDTO alarmDTO = realm.createObject(AlarmDTO.class, nextId);

            alarmDTO.setAlarmHour(hour);
            alarmDTO.setAlarmMinute(minute);
            alarmDTO.setAlarmOnOff(true);        //알람 디폴트 ON
            alarmDTO.setMonday(isMonday);
            alarmDTO.setTuesday(isTuesday);
            alarmDTO.setWednesday(isWednesday);
            alarmDTO.setThursday(isThursday);
            alarmDTO.setFriday(isFriday);
            alarmDTO.setSaturday(isSaturday);
            alarmDTO.setSunday(isSunday);
            realm.commitTransaction();

            Log.i("디비 등록","디비알람시간: "+ String.valueOf(alarmDTO.getAlarmHour()+" : "+alarmDTO.getAlarmMinute()));

        return alarmDTO;
    }

    // 알람 카운트 주기
    private int incremnetAlarmCount(){
        Number maxId  = realm.where(AlarmDTO.class).max("id");
        return (maxId == null) ? 1 : maxId.intValue() + 1;
    }

    // 알람 정보 모두 등록
    public  RealmResults<AlarmDTO> getAllAlarm(){
        return realm.where(AlarmDTO.class).findAllSorted("id");
    }

    // 램 데이터베이스에서 삭제
    public void deleteAlarmData(long alamrID){

        AlarmDTO alarmDTO = realm.where(AlarmDTO.class).equalTo("id", alamrID).findFirst();
        if (alarmDTO != null) {
            realm.beginTransaction();
            alarmDTO.deleteFromRealm();
            realm.commitTransaction();
        }

    }


}
