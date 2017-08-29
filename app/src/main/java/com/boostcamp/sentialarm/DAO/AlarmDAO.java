package com.boostcamp.sentialarm.DAO;

import com.boostcamp.sentialarm.DTO.AlarmDTO;
import com.boostcamp.sentialarm.Util.ApplicationClass;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by 현기 on 2017-08-08.
 */

public class AlarmDAO {

   public Realm realm;

    //램 열기
    public void creatAlarmRealm(){
        realm = Realm.getInstance(ApplicationClass.alarmListConfig);
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
            // 저장 객체에 primary key - count 주기
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

    public boolean isEmptyAlarmList(){
        Number maxAlarm = realm.where(AlarmDTO.class).max("id");
        if(maxAlarm == null)
            return true;

        return false;
    }

    // 램 데이터베이스에서 삭제
    public void deleteAlarmData(long alamrID){

        final AlarmDTO alarmDTO = realm.where(AlarmDTO.class).equalTo("id", alamrID).findFirst();
        if (alarmDTO != null) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    alarmDTO.deleteFromRealm();
                }
            });
        }
    }


}
