package com.boostcamp.sentialarm.AlarmSong;

import android.util.Log;

import com.boostcamp.sentialarm.API.Jamendo.DTO.MusicDTO;
import com.boostcamp.sentialarm.API.MediaPlayer.MusicLocalDTO;
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


    public void setSongData(final MusicDTO musicDTO, final WeatherInfoDTO weatherInfoDTO, final String songFileName) {

        // 송리스트에 데이터 저장
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Log.i("tests", "노래저장");
                // 저장 객체에 count 주기
                int nextId = incrementSongCount();

                SongDTO songDTO = realm.createObject(SongDTO.class, nextId);
                WeatherInfoDTO realmWeatherInfoDTO = realm.copyToRealm(weatherInfoDTO);

                songDTO.setArtistName(musicDTO.getResults().get(0).getArtist_name());
                songDTO.setMusicTitle(musicDTO.getResults().get(0).getName());
                songDTO.setSongShareURL(musicDTO.getResults().get(0).getShareurl());
                songDTO.setPlayDate(new Date());
                songDTO.setFileName(songFileName);
                songDTO.setWeatherInfoDTO(realmWeatherInfoDTO);
                songDTO.setLocalSong(false);

                Log.i("tests","song 데이터 삽입완료");
            }
        });
    }

    public void setLocalSongData(final MusicLocalDTO localSongData){
        // 송리스트에 데이터 저장
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Log.i("tests", "노래저장");
                // 저장 객체에 count 주기
                int nextId = incrementSongCount();

                SongDTO songDTO = realm.createObject(SongDTO.class, nextId);
                WeatherInfoDTO realmWeatherInfoDTO = realm.createObject(WeatherInfoDTO.class);

                songDTO.setArtistName(localSongData.getArtist());
                songDTO.setMusicTitle(localSongData.getTitle());
                songDTO.setSongShareURL("content://media/external/audio/media/"+localSongData.getId());
                songDTO.setPlayDate(new Date());
                songDTO.setLocalSong(true);
                songDTO.setWeatherInfoDTO(realmWeatherInfoDTO);

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

    public SongDTO getSongDTO(int nextId){
        SongDTO songDTO = realm.where(SongDTO.class).equalTo("id",nextId).findFirst();
        return songDTO;
    }

    // 램 데이터베이스에서 삭제
    public void deleteSongData(int songID){

        final SongDTO songDTO = realm.where(SongDTO.class).equalTo("id", songID).findFirst();
        if (songDTO != null) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    songDTO.deleteFromRealm();
                }
            });
        }
    }
}
