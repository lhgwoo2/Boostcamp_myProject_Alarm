package com.boostcamp.sentialarm.AlarmSong;

import android.util.Log;

import com.boostcamp.sentialarm.API.Jamendo.DTO.MusicDTO;
import com.boostcamp.sentialarm.Util.ApplicationClass;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by 현기 on 2017-08-11.
 */

public class SongDAO {

    public Realm realm;

    public void createSongRealm() {
        realm = Realm.getInstance(ApplicationClass.songListConfig);
    }

    public RealmResults<SongDTO> getSongListFindAll(){
        RealmResults<SongDTO> songDTOs = realm.where(SongDTO.class).findAllSorted("id");
        return songDTOs;
    }

    public void setSongData(final MusicDTO musicDTO, final String songFileName) {

        // 송리스트에 데이터 저장
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Log.i("tests", "노래저장");

                // 저장 객체에 count 주기
                int nextId = incrementSongCount();
                Log.i("tests","id값 증가이후 = "+nextId);
                SongDTO songDTO = realm.createObject(SongDTO.class, nextId);

                Log.i("tests","song램 객체 생성");
                songDTO.setArtistName(musicDTO.getResults().get(0).getArtist_name());
                Log.i("tests","song 데이터 아티스트 이름");
                songDTO.setMusicTitle(musicDTO.getResults().get(0).getName());
                songDTO.setSongShareURL(musicDTO.getResults().get(0).getShareurl());
                songDTO.setPlayDate(new Date());
                songDTO.setFileName(songFileName);

                Log.i("tests","song 데이터 삽입완료");
            }
        });
    }

    // 알람 카운트 주기
    private int incrementSongCount(){
        Number maxId  = realm.where(SongDTO.class).max("id");
        return (maxId == null) ? 1 : maxId.intValue() + 1;
    }

    public void closeSongRealm() {
        if (realm != null) {
            realm.close();
        }
    }
}
